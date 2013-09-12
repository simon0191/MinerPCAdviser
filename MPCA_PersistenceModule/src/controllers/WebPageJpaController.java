/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.MpcaComment;
import java.util.ArrayList;
import java.util.List;
import entities.ProductWebPage;
import entities.WebPage;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class WebPageJpaController implements Serializable {

    public WebPageJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public WebPageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(WebPage webPage) throws PreexistingEntityException, Exception {
        if (webPage.getMpcaCommentList() == null) {
            webPage.setMpcaCommentList(new ArrayList<MpcaComment>());
        }
        if (webPage.getProductWebPageList() == null) {
            webPage.setProductWebPageList(new ArrayList<ProductWebPage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaComment> attachedMpcaCommentList = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListMpcaCommentToAttach : webPage.getMpcaCommentList()) {
                mpcaCommentListMpcaCommentToAttach = em.getReference(mpcaCommentListMpcaCommentToAttach.getClass(), mpcaCommentListMpcaCommentToAttach.getMpcaCommentPK());
                attachedMpcaCommentList.add(mpcaCommentListMpcaCommentToAttach);
            }
            webPage.setMpcaCommentList(attachedMpcaCommentList);
            List<ProductWebPage> attachedProductWebPageList = new ArrayList<ProductWebPage>();
            for (ProductWebPage productWebPageListProductWebPageToAttach : webPage.getProductWebPageList()) {
                productWebPageListProductWebPageToAttach = em.getReference(productWebPageListProductWebPageToAttach.getClass(), productWebPageListProductWebPageToAttach.getProductWebPagePK());
                attachedProductWebPageList.add(productWebPageListProductWebPageToAttach);
            }
            webPage.setProductWebPageList(attachedProductWebPageList);
            em.persist(webPage);
            for (MpcaComment mpcaCommentListMpcaComment : webPage.getMpcaCommentList()) {
                WebPage oldWebPageOfMpcaCommentListMpcaComment = mpcaCommentListMpcaComment.getWebPage();
                mpcaCommentListMpcaComment.setWebPage(webPage);
                mpcaCommentListMpcaComment = em.merge(mpcaCommentListMpcaComment);
                if (oldWebPageOfMpcaCommentListMpcaComment != null) {
                    oldWebPageOfMpcaCommentListMpcaComment.getMpcaCommentList().remove(mpcaCommentListMpcaComment);
                    oldWebPageOfMpcaCommentListMpcaComment = em.merge(oldWebPageOfMpcaCommentListMpcaComment);
                }
            }
            for (ProductWebPage productWebPageListProductWebPage : webPage.getProductWebPageList()) {
                WebPage oldWebPageOfProductWebPageListProductWebPage = productWebPageListProductWebPage.getWebPage();
                productWebPageListProductWebPage.setWebPage(webPage);
                productWebPageListProductWebPage = em.merge(productWebPageListProductWebPage);
                if (oldWebPageOfProductWebPageListProductWebPage != null) {
                    oldWebPageOfProductWebPageListProductWebPage.getProductWebPageList().remove(productWebPageListProductWebPage);
                    oldWebPageOfProductWebPageListProductWebPage = em.merge(oldWebPageOfProductWebPageListProductWebPage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findWebPage(webPage.getPageId()) != null) {
                throw new PreexistingEntityException("WebPage " + webPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(WebPage webPage) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WebPage persistentWebPage = em.find(WebPage.class, webPage.getPageId());
            List<MpcaComment> mpcaCommentListOld = persistentWebPage.getMpcaCommentList();
            List<MpcaComment> mpcaCommentListNew = webPage.getMpcaCommentList();
            List<ProductWebPage> productWebPageListOld = persistentWebPage.getProductWebPageList();
            List<ProductWebPage> productWebPageListNew = webPage.getProductWebPageList();
            List<String> illegalOrphanMessages = null;
            for (MpcaComment mpcaCommentListOldMpcaComment : mpcaCommentListOld) {
                if (!mpcaCommentListNew.contains(mpcaCommentListOldMpcaComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaComment " + mpcaCommentListOldMpcaComment + " since its webPage field is not nullable.");
                }
            }
            for (ProductWebPage productWebPageListOldProductWebPage : productWebPageListOld) {
                if (!productWebPageListNew.contains(productWebPageListOldProductWebPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductWebPage " + productWebPageListOldProductWebPage + " since its webPage field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaComment> attachedMpcaCommentListNew = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListNewMpcaCommentToAttach : mpcaCommentListNew) {
                mpcaCommentListNewMpcaCommentToAttach = em.getReference(mpcaCommentListNewMpcaCommentToAttach.getClass(), mpcaCommentListNewMpcaCommentToAttach.getMpcaCommentPK());
                attachedMpcaCommentListNew.add(mpcaCommentListNewMpcaCommentToAttach);
            }
            mpcaCommentListNew = attachedMpcaCommentListNew;
            webPage.setMpcaCommentList(mpcaCommentListNew);
            List<ProductWebPage> attachedProductWebPageListNew = new ArrayList<ProductWebPage>();
            for (ProductWebPage productWebPageListNewProductWebPageToAttach : productWebPageListNew) {
                productWebPageListNewProductWebPageToAttach = em.getReference(productWebPageListNewProductWebPageToAttach.getClass(), productWebPageListNewProductWebPageToAttach.getProductWebPagePK());
                attachedProductWebPageListNew.add(productWebPageListNewProductWebPageToAttach);
            }
            productWebPageListNew = attachedProductWebPageListNew;
            webPage.setProductWebPageList(productWebPageListNew);
            webPage = em.merge(webPage);
            for (MpcaComment mpcaCommentListNewMpcaComment : mpcaCommentListNew) {
                if (!mpcaCommentListOld.contains(mpcaCommentListNewMpcaComment)) {
                    WebPage oldWebPageOfMpcaCommentListNewMpcaComment = mpcaCommentListNewMpcaComment.getWebPage();
                    mpcaCommentListNewMpcaComment.setWebPage(webPage);
                    mpcaCommentListNewMpcaComment = em.merge(mpcaCommentListNewMpcaComment);
                    if (oldWebPageOfMpcaCommentListNewMpcaComment != null && !oldWebPageOfMpcaCommentListNewMpcaComment.equals(webPage)) {
                        oldWebPageOfMpcaCommentListNewMpcaComment.getMpcaCommentList().remove(mpcaCommentListNewMpcaComment);
                        oldWebPageOfMpcaCommentListNewMpcaComment = em.merge(oldWebPageOfMpcaCommentListNewMpcaComment);
                    }
                }
            }
            for (ProductWebPage productWebPageListNewProductWebPage : productWebPageListNew) {
                if (!productWebPageListOld.contains(productWebPageListNewProductWebPage)) {
                    WebPage oldWebPageOfProductWebPageListNewProductWebPage = productWebPageListNewProductWebPage.getWebPage();
                    productWebPageListNewProductWebPage.setWebPage(webPage);
                    productWebPageListNewProductWebPage = em.merge(productWebPageListNewProductWebPage);
                    if (oldWebPageOfProductWebPageListNewProductWebPage != null && !oldWebPageOfProductWebPageListNewProductWebPage.equals(webPage)) {
                        oldWebPageOfProductWebPageListNewProductWebPage.getProductWebPageList().remove(productWebPageListNewProductWebPage);
                        oldWebPageOfProductWebPageListNewProductWebPage = em.merge(oldWebPageOfProductWebPageListNewProductWebPage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = webPage.getPageId();
                if (findWebPage(id) == null) {
                    throw new NonexistentEntityException("The webPage with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WebPage webPage;
            try {
                webPage = em.getReference(WebPage.class, id);
                webPage.getPageId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The webPage with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaComment> mpcaCommentListOrphanCheck = webPage.getMpcaCommentList();
            for (MpcaComment mpcaCommentListOrphanCheckMpcaComment : mpcaCommentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WebPage (" + webPage + ") cannot be destroyed since the MpcaComment " + mpcaCommentListOrphanCheckMpcaComment + " in its mpcaCommentList field has a non-nullable webPage field.");
            }
            List<ProductWebPage> productWebPageListOrphanCheck = webPage.getProductWebPageList();
            for (ProductWebPage productWebPageListOrphanCheckProductWebPage : productWebPageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This WebPage (" + webPage + ") cannot be destroyed since the ProductWebPage " + productWebPageListOrphanCheckProductWebPage + " in its productWebPageList field has a non-nullable webPage field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(webPage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<WebPage> findWebPageEntities() {
        return findWebPageEntities(true, -1, -1);
    }

    public List<WebPage> findWebPageEntities(int maxResults, int firstResult) {
        return findWebPageEntities(false, maxResults, firstResult);
    }

    private List<WebPage> findWebPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(WebPage.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public WebPage findWebPage(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(WebPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getWebPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<WebPage> rt = cq.from(WebPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
