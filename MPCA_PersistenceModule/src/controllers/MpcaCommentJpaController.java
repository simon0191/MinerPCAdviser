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
import entities.WebPage;
import entities.Product;
import entities.Author;
import entities.CommentAddition;
import entities.MpcaComment;
import entities.MpcaCommentPK;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class MpcaCommentJpaController implements Serializable {

    public MpcaCommentJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public MpcaCommentJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaComment mpcaComment) throws PreexistingEntityException, Exception {
        
        if (mpcaComment.getMpcaCommentPK() == null) {
            mpcaComment.setMpcaCommentPK(new MpcaCommentPK());
        }
        if (mpcaComment.getCommentAdditionList() == null) {
            mpcaComment.setCommentAdditionList(new ArrayList<CommentAddition>());
        }
        
        mpcaComment.getMpcaCommentPK().setBrandId(mpcaComment.getProduct().getProductPK().getBrandId());
        mpcaComment.getMpcaCommentPK().setAuthorId(mpcaComment.getAuthor().getAuthorId().toBigInteger());
        mpcaComment.getMpcaCommentPK().setPageId(mpcaComment.getWebPage().getPageId().toBigInteger());
        mpcaComment.getMpcaCommentPK().setProductId(mpcaComment.getProduct().getProductPK().getProductId());
        
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WebPage webPage = mpcaComment.getWebPage();
            if (webPage != null) {
                webPage = em.getReference(webPage.getClass(), webPage.getPageId());
                mpcaComment.setWebPage(webPage);
            }
            Product product = mpcaComment.getProduct();
            if (product != null) {
                product = em.getReference(product.getClass(), product.getProductPK());
                mpcaComment.setProduct(product);
            }
            Author author = mpcaComment.getAuthor();
            if (author != null) {
                author = em.getReference(author.getClass(), author.getAuthorId());
                mpcaComment.setAuthor(author);
            }
            
            List<CommentAddition> attachedCommentAdditionList = new ArrayList<CommentAddition>();
            for (CommentAddition commentAdditionListCommentAdditionToAttach : mpcaComment.getCommentAdditionList()) {
                commentAdditionListCommentAdditionToAttach = em.getReference(commentAdditionListCommentAdditionToAttach.getClass(), commentAdditionListCommentAdditionToAttach.getCommentAdditionPK());
                attachedCommentAdditionList.add(commentAdditionListCommentAdditionToAttach);
            }
            
            mpcaComment.setCommentAdditionList(attachedCommentAdditionList);
            em.persist(mpcaComment);
            
            if (webPage != null) {
                webPage.getMpcaCommentList().add(mpcaComment);
                webPage = em.merge(webPage);
            }
            if (product != null) {
                product.getMpcaCommentList().add(mpcaComment);
                product = em.merge(product);
            }
            
            if (author != null) {
                author.getMpcaCommentList().add(mpcaComment);
                author = em.merge(author);
            }
            for (CommentAddition commentAdditionListCommentAddition : mpcaComment.getCommentAdditionList()) {
                MpcaComment oldMpcaCommentOfCommentAdditionListCommentAddition = commentAdditionListCommentAddition.getMpcaComment();
                commentAdditionListCommentAddition.setMpcaComment(mpcaComment);
                commentAdditionListCommentAddition = em.merge(commentAdditionListCommentAddition);
                if (oldMpcaCommentOfCommentAdditionListCommentAddition != null) {
                    oldMpcaCommentOfCommentAdditionListCommentAddition.getCommentAdditionList().remove(commentAdditionListCommentAddition);
                    oldMpcaCommentOfCommentAdditionListCommentAddition = em.merge(oldMpcaCommentOfCommentAdditionListCommentAddition);
                }
            }
            
            em.getTransaction().commit();
            
        } catch (Exception ex) {
            if (findMpcaComment(mpcaComment.getMpcaCommentPK()) != null) {
                throw new PreexistingEntityException("MpcaComment " + mpcaComment + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaComment mpcaComment) throws IllegalOrphanException, NonexistentEntityException, Exception {
        mpcaComment.getMpcaCommentPK().setBrandId(mpcaComment.getProduct().getProductPK().getBrandId());
        mpcaComment.getMpcaCommentPK().setAuthorId(mpcaComment.getAuthor().getAuthorId().toBigInteger());
        mpcaComment.getMpcaCommentPK().setPageId(mpcaComment.getWebPage().getPageId().toBigInteger());
        mpcaComment.getMpcaCommentPK().setProductId(mpcaComment.getProduct().getProductPK().getProductId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaComment persistentMpcaComment = em.find(MpcaComment.class, mpcaComment.getMpcaCommentPK());
            WebPage webPageOld = persistentMpcaComment.getWebPage();
            WebPage webPageNew = mpcaComment.getWebPage();
            Product productOld = persistentMpcaComment.getProduct();
            Product productNew = mpcaComment.getProduct();
            Author authorOld = persistentMpcaComment.getAuthor();
            Author authorNew = mpcaComment.getAuthor();
            List<CommentAddition> commentAdditionListOld = persistentMpcaComment.getCommentAdditionList();
            List<CommentAddition> commentAdditionListNew = mpcaComment.getCommentAdditionList();
            List<String> illegalOrphanMessages = null;
            for (CommentAddition commentAdditionListOldCommentAddition : commentAdditionListOld) {
                if (!commentAdditionListNew.contains(commentAdditionListOldCommentAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommentAddition " + commentAdditionListOldCommentAddition + " since its mpcaComment field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (webPageNew != null) {
                webPageNew = em.getReference(webPageNew.getClass(), webPageNew.getPageId());
                mpcaComment.setWebPage(webPageNew);
            }
            if (productNew != null) {
                productNew = em.getReference(productNew.getClass(), productNew.getProductPK());
                mpcaComment.setProduct(productNew);
            }
            if (authorNew != null) {
                authorNew = em.getReference(authorNew.getClass(), authorNew.getAuthorId());
                mpcaComment.setAuthor(authorNew);
            }
            List<CommentAddition> attachedCommentAdditionListNew = new ArrayList<CommentAddition>();
            for (CommentAddition commentAdditionListNewCommentAdditionToAttach : commentAdditionListNew) {
                commentAdditionListNewCommentAdditionToAttach = em.getReference(commentAdditionListNewCommentAdditionToAttach.getClass(), commentAdditionListNewCommentAdditionToAttach.getCommentAdditionPK());
                attachedCommentAdditionListNew.add(commentAdditionListNewCommentAdditionToAttach);
            }
            commentAdditionListNew = attachedCommentAdditionListNew;
            mpcaComment.setCommentAdditionList(commentAdditionListNew);
            mpcaComment = em.merge(mpcaComment);
            if (webPageOld != null && !webPageOld.equals(webPageNew)) {
                webPageOld.getMpcaCommentList().remove(mpcaComment);
                webPageOld = em.merge(webPageOld);
            }
            if (webPageNew != null && !webPageNew.equals(webPageOld)) {
                webPageNew.getMpcaCommentList().add(mpcaComment);
                webPageNew = em.merge(webPageNew);
            }
            if (productOld != null && !productOld.equals(productNew)) {
                productOld.getMpcaCommentList().remove(mpcaComment);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld)) {
                productNew.getMpcaCommentList().add(mpcaComment);
                productNew = em.merge(productNew);
            }
            if (authorOld != null && !authorOld.equals(authorNew)) {
                authorOld.getMpcaCommentList().remove(mpcaComment);
                authorOld = em.merge(authorOld);
            }
            if (authorNew != null && !authorNew.equals(authorOld)) {
                authorNew.getMpcaCommentList().add(mpcaComment);
                authorNew = em.merge(authorNew);
            }
            for (CommentAddition commentAdditionListNewCommentAddition : commentAdditionListNew) {
                if (!commentAdditionListOld.contains(commentAdditionListNewCommentAddition)) {
                    MpcaComment oldMpcaCommentOfCommentAdditionListNewCommentAddition = commentAdditionListNewCommentAddition.getMpcaComment();
                    commentAdditionListNewCommentAddition.setMpcaComment(mpcaComment);
                    commentAdditionListNewCommentAddition = em.merge(commentAdditionListNewCommentAddition);
                    if (oldMpcaCommentOfCommentAdditionListNewCommentAddition != null && !oldMpcaCommentOfCommentAdditionListNewCommentAddition.equals(mpcaComment)) {
                        oldMpcaCommentOfCommentAdditionListNewCommentAddition.getCommentAdditionList().remove(commentAdditionListNewCommentAddition);
                        oldMpcaCommentOfCommentAdditionListNewCommentAddition = em.merge(oldMpcaCommentOfCommentAdditionListNewCommentAddition);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MpcaCommentPK id = mpcaComment.getMpcaCommentPK();
                if (findMpcaComment(id) == null) {
                    throw new NonexistentEntityException("The mpcaComment with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MpcaCommentPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaComment mpcaComment;
            try {
                mpcaComment = em.getReference(MpcaComment.class, id);
                mpcaComment.getMpcaCommentPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaComment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<CommentAddition> commentAdditionListOrphanCheck = mpcaComment.getCommentAdditionList();
            for (CommentAddition commentAdditionListOrphanCheckCommentAddition : commentAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaComment (" + mpcaComment + ") cannot be destroyed since the CommentAddition " + commentAdditionListOrphanCheckCommentAddition + " in its commentAdditionList field has a non-nullable mpcaComment field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            WebPage webPage = mpcaComment.getWebPage();
            if (webPage != null) {
                webPage.getMpcaCommentList().remove(mpcaComment);
                webPage = em.merge(webPage);
            }
            Product product = mpcaComment.getProduct();
            if (product != null) {
                product.getMpcaCommentList().remove(mpcaComment);
                product = em.merge(product);
            }
            Author author = mpcaComment.getAuthor();
            if (author != null) {
                author.getMpcaCommentList().remove(mpcaComment);
                author = em.merge(author);
            }
            em.remove(mpcaComment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaComment> findMpcaCommentEntities() {
        return findMpcaCommentEntities(true, -1, -1);
    }

    public List<MpcaComment> findMpcaCommentEntities(int maxResults, int firstResult) {
        return findMpcaCommentEntities(false, maxResults, firstResult);
    }

    private List<MpcaComment> findMpcaCommentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaComment.class));
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

    public MpcaComment findMpcaComment(MpcaCommentPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaComment.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaCommentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaComment> rt = cq.from(MpcaComment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    public List<MpcaComment> findByAdditionAndValue(String addition, String value) {
        //additionValue
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("MpcaComment.findByAdditionAndValue");
        q.setParameter("additionValue", value);
        //List<MpcaComment> resultList = q.getResultList();
        
        return q.getResultList();
    }
}
