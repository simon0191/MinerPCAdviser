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
import entities.MpcaProduct;
import java.util.ArrayList;
import java.util.List;
import entities.MpcaComment;
import entities.MpcaWebPage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaWebPageJpaController implements Serializable {

    public MpcaWebPageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaWebPage mpcaWebPage) throws PreexistingEntityException, Exception {
        if (mpcaWebPage.getMpcaProductList() == null) {
            mpcaWebPage.setMpcaProductList(new ArrayList<MpcaProduct>());
        }
        if (mpcaWebPage.getMpcaCommentList() == null) {
            mpcaWebPage.setMpcaCommentList(new ArrayList<MpcaComment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaProduct> attachedMpcaProductList = new ArrayList<MpcaProduct>();
            for (MpcaProduct mpcaProductListMpcaProductToAttach : mpcaWebPage.getMpcaProductList()) {
                mpcaProductListMpcaProductToAttach = em.getReference(mpcaProductListMpcaProductToAttach.getClass(), mpcaProductListMpcaProductToAttach.getProductId());
                attachedMpcaProductList.add(mpcaProductListMpcaProductToAttach);
            }
            mpcaWebPage.setMpcaProductList(attachedMpcaProductList);
            List<MpcaComment> attachedMpcaCommentList = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListMpcaCommentToAttach : mpcaWebPage.getMpcaCommentList()) {
                mpcaCommentListMpcaCommentToAttach = em.getReference(mpcaCommentListMpcaCommentToAttach.getClass(), mpcaCommentListMpcaCommentToAttach.getCommentId());
                attachedMpcaCommentList.add(mpcaCommentListMpcaCommentToAttach);
            }
            mpcaWebPage.setMpcaCommentList(attachedMpcaCommentList);
            em.persist(mpcaWebPage);
            for (MpcaProduct mpcaProductListMpcaProduct : mpcaWebPage.getMpcaProductList()) {
                mpcaProductListMpcaProduct.getMpcaWebPageList().add(mpcaWebPage);
                mpcaProductListMpcaProduct = em.merge(mpcaProductListMpcaProduct);
            }
            for (MpcaComment mpcaCommentListMpcaComment : mpcaWebPage.getMpcaCommentList()) {
                MpcaWebPage oldPageIdOfMpcaCommentListMpcaComment = mpcaCommentListMpcaComment.getPageId();
                mpcaCommentListMpcaComment.setPageId(mpcaWebPage);
                mpcaCommentListMpcaComment = em.merge(mpcaCommentListMpcaComment);
                if (oldPageIdOfMpcaCommentListMpcaComment != null) {
                    oldPageIdOfMpcaCommentListMpcaComment.getMpcaCommentList().remove(mpcaCommentListMpcaComment);
                    oldPageIdOfMpcaCommentListMpcaComment = em.merge(oldPageIdOfMpcaCommentListMpcaComment);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaWebPage(mpcaWebPage.getPageId()) != null) {
                throw new PreexistingEntityException("MpcaWebPage " + mpcaWebPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaWebPage mpcaWebPage) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaWebPage persistentMpcaWebPage = em.find(MpcaWebPage.class, mpcaWebPage.getPageId());
            List<MpcaProduct> mpcaProductListOld = persistentMpcaWebPage.getMpcaProductList();
            List<MpcaProduct> mpcaProductListNew = mpcaWebPage.getMpcaProductList();
            List<MpcaComment> mpcaCommentListOld = persistentMpcaWebPage.getMpcaCommentList();
            List<MpcaComment> mpcaCommentListNew = mpcaWebPage.getMpcaCommentList();
            List<String> illegalOrphanMessages = null;
            for (MpcaComment mpcaCommentListOldMpcaComment : mpcaCommentListOld) {
                if (!mpcaCommentListNew.contains(mpcaCommentListOldMpcaComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaComment " + mpcaCommentListOldMpcaComment + " since its pageId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaProduct> attachedMpcaProductListNew = new ArrayList<MpcaProduct>();
            for (MpcaProduct mpcaProductListNewMpcaProductToAttach : mpcaProductListNew) {
                mpcaProductListNewMpcaProductToAttach = em.getReference(mpcaProductListNewMpcaProductToAttach.getClass(), mpcaProductListNewMpcaProductToAttach.getProductId());
                attachedMpcaProductListNew.add(mpcaProductListNewMpcaProductToAttach);
            }
            mpcaProductListNew = attachedMpcaProductListNew;
            mpcaWebPage.setMpcaProductList(mpcaProductListNew);
            List<MpcaComment> attachedMpcaCommentListNew = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListNewMpcaCommentToAttach : mpcaCommentListNew) {
                mpcaCommentListNewMpcaCommentToAttach = em.getReference(mpcaCommentListNewMpcaCommentToAttach.getClass(), mpcaCommentListNewMpcaCommentToAttach.getCommentId());
                attachedMpcaCommentListNew.add(mpcaCommentListNewMpcaCommentToAttach);
            }
            mpcaCommentListNew = attachedMpcaCommentListNew;
            mpcaWebPage.setMpcaCommentList(mpcaCommentListNew);
            mpcaWebPage = em.merge(mpcaWebPage);
            for (MpcaProduct mpcaProductListOldMpcaProduct : mpcaProductListOld) {
                if (!mpcaProductListNew.contains(mpcaProductListOldMpcaProduct)) {
                    mpcaProductListOldMpcaProduct.getMpcaWebPageList().remove(mpcaWebPage);
                    mpcaProductListOldMpcaProduct = em.merge(mpcaProductListOldMpcaProduct);
                }
            }
            for (MpcaProduct mpcaProductListNewMpcaProduct : mpcaProductListNew) {
                if (!mpcaProductListOld.contains(mpcaProductListNewMpcaProduct)) {
                    mpcaProductListNewMpcaProduct.getMpcaWebPageList().add(mpcaWebPage);
                    mpcaProductListNewMpcaProduct = em.merge(mpcaProductListNewMpcaProduct);
                }
            }
            for (MpcaComment mpcaCommentListNewMpcaComment : mpcaCommentListNew) {
                if (!mpcaCommentListOld.contains(mpcaCommentListNewMpcaComment)) {
                    MpcaWebPage oldPageIdOfMpcaCommentListNewMpcaComment = mpcaCommentListNewMpcaComment.getPageId();
                    mpcaCommentListNewMpcaComment.setPageId(mpcaWebPage);
                    mpcaCommentListNewMpcaComment = em.merge(mpcaCommentListNewMpcaComment);
                    if (oldPageIdOfMpcaCommentListNewMpcaComment != null && !oldPageIdOfMpcaCommentListNewMpcaComment.equals(mpcaWebPage)) {
                        oldPageIdOfMpcaCommentListNewMpcaComment.getMpcaCommentList().remove(mpcaCommentListNewMpcaComment);
                        oldPageIdOfMpcaCommentListNewMpcaComment = em.merge(oldPageIdOfMpcaCommentListNewMpcaComment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaWebPage.getPageId();
                if (findMpcaWebPage(id) == null) {
                    throw new NonexistentEntityException("The mpcaWebPage with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaWebPage mpcaWebPage;
            try {
                mpcaWebPage = em.getReference(MpcaWebPage.class, id);
                mpcaWebPage.getPageId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaWebPage with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaComment> mpcaCommentListOrphanCheck = mpcaWebPage.getMpcaCommentList();
            for (MpcaComment mpcaCommentListOrphanCheckMpcaComment : mpcaCommentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaWebPage (" + mpcaWebPage + ") cannot be destroyed since the MpcaComment " + mpcaCommentListOrphanCheckMpcaComment + " in its mpcaCommentList field has a non-nullable pageId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaProduct> mpcaProductList = mpcaWebPage.getMpcaProductList();
            for (MpcaProduct mpcaProductListMpcaProduct : mpcaProductList) {
                mpcaProductListMpcaProduct.getMpcaWebPageList().remove(mpcaWebPage);
                mpcaProductListMpcaProduct = em.merge(mpcaProductListMpcaProduct);
            }
            em.remove(mpcaWebPage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaWebPage> findMpcaWebPageEntities() {
        return findMpcaWebPageEntities(true, -1, -1);
    }

    public List<MpcaWebPage> findMpcaWebPageEntities(int maxResults, int firstResult) {
        return findMpcaWebPageEntities(false, maxResults, firstResult);
    }

    private List<MpcaWebPage> findMpcaWebPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaWebPage.class));
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

    public MpcaWebPage findMpcaWebPage(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaWebPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaWebPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaWebPage> rt = cq.from(MpcaWebPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
