/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.MpcaComment;
import entities.Addition;
import entities.CommentAddition;
import entities.CommentAdditionPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class CommentAdditionJpaController implements Serializable {

    public CommentAdditionJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public CommentAdditionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CommentAddition commentAddition) throws PreexistingEntityException, Exception {
        if (commentAddition.getCommentAdditionPK() == null) {
            commentAddition.setCommentAdditionPK(new CommentAdditionPK());
        }
        commentAddition.getCommentAdditionPK().setAdditionAddId(commentAddition.getAddition().getAddId().toBigInteger());
        commentAddition.getCommentAdditionPK().setCommentBrandId(commentAddition.getMpcaComment().getMpcaCommentPK().getBrandId());
        commentAddition.getCommentAdditionPK().setCommentPageId(commentAddition.getMpcaComment().getMpcaCommentPK().getPageId());
        commentAddition.getCommentAdditionPK().setCommentAuthorId(commentAddition.getMpcaComment().getMpcaCommentPK().getAuthorId());
        commentAddition.getCommentAdditionPK().setCommentProductId(commentAddition.getMpcaComment().getMpcaCommentPK().getProductId());
        commentAddition.getCommentAdditionPK().setCommentCommentId(commentAddition.getMpcaComment().getMpcaCommentPK().getCommentId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaComment mpcaComment = commentAddition.getMpcaComment();
            if (mpcaComment != null) {
                mpcaComment = em.getReference(mpcaComment.getClass(), mpcaComment.getMpcaCommentPK());
                commentAddition.setMpcaComment(mpcaComment);
            }
            Addition addition = commentAddition.getAddition();
            if (addition != null) {
                addition = em.getReference(addition.getClass(), addition.getAddId());
                commentAddition.setAddition(addition);
            }
            em.persist(commentAddition);
            if (mpcaComment != null) {
                mpcaComment.getCommentAdditionList().add(commentAddition);
                mpcaComment = em.merge(mpcaComment);
            }
            if (addition != null) {
                addition.getCommentAdditionList().add(commentAddition);
                addition = em.merge(addition);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findCommentAddition(commentAddition.getCommentAdditionPK()) != null) {
                throw new PreexistingEntityException("CommentAddition " + commentAddition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CommentAddition commentAddition) throws NonexistentEntityException, Exception {
        commentAddition.getCommentAdditionPK().setAdditionAddId(commentAddition.getAddition().getAddId().toBigInteger());
        commentAddition.getCommentAdditionPK().setCommentBrandId(commentAddition.getMpcaComment().getMpcaCommentPK().getBrandId());
        commentAddition.getCommentAdditionPK().setCommentPageId(commentAddition.getMpcaComment().getMpcaCommentPK().getPageId());
        commentAddition.getCommentAdditionPK().setCommentAuthorId(commentAddition.getMpcaComment().getMpcaCommentPK().getAuthorId());
        commentAddition.getCommentAdditionPK().setCommentProductId(commentAddition.getMpcaComment().getMpcaCommentPK().getProductId());
        commentAddition.getCommentAdditionPK().setCommentCommentId(commentAddition.getMpcaComment().getMpcaCommentPK().getCommentId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommentAddition persistentCommentAddition = em.find(CommentAddition.class, commentAddition.getCommentAdditionPK());
            MpcaComment mpcaCommentOld = persistentCommentAddition.getMpcaComment();
            MpcaComment mpcaCommentNew = commentAddition.getMpcaComment();
            Addition additionOld = persistentCommentAddition.getAddition();
            Addition additionNew = commentAddition.getAddition();
            if (mpcaCommentNew != null) {
                mpcaCommentNew = em.getReference(mpcaCommentNew.getClass(), mpcaCommentNew.getMpcaCommentPK());
                commentAddition.setMpcaComment(mpcaCommentNew);
            }
            if (additionNew != null) {
                additionNew = em.getReference(additionNew.getClass(), additionNew.getAddId());
                commentAddition.setAddition(additionNew);
            }
            commentAddition = em.merge(commentAddition);
            if (mpcaCommentOld != null && !mpcaCommentOld.equals(mpcaCommentNew)) {
                mpcaCommentOld.getCommentAdditionList().remove(commentAddition);
                mpcaCommentOld = em.merge(mpcaCommentOld);
            }
            if (mpcaCommentNew != null && !mpcaCommentNew.equals(mpcaCommentOld)) {
                mpcaCommentNew.getCommentAdditionList().add(commentAddition);
                mpcaCommentNew = em.merge(mpcaCommentNew);
            }
            if (additionOld != null && !additionOld.equals(additionNew)) {
                additionOld.getCommentAdditionList().remove(commentAddition);
                additionOld = em.merge(additionOld);
            }
            if (additionNew != null && !additionNew.equals(additionOld)) {
                additionNew.getCommentAdditionList().add(commentAddition);
                additionNew = em.merge(additionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                CommentAdditionPK id = commentAddition.getCommentAdditionPK();
                if (findCommentAddition(id) == null) {
                    throw new NonexistentEntityException("The commentAddition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(CommentAdditionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CommentAddition commentAddition;
            try {
                commentAddition = em.getReference(CommentAddition.class, id);
                commentAddition.getCommentAdditionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The commentAddition with id " + id + " no longer exists.", enfe);
            }
            MpcaComment mpcaComment = commentAddition.getMpcaComment();
            if (mpcaComment != null) {
                mpcaComment.getCommentAdditionList().remove(commentAddition);
                mpcaComment = em.merge(mpcaComment);
            }
            Addition addition = commentAddition.getAddition();
            if (addition != null) {
                addition.getCommentAdditionList().remove(commentAddition);
                addition = em.merge(addition);
            }
            em.remove(commentAddition);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CommentAddition> findCommentAdditionEntities() {
        return findCommentAdditionEntities(true, -1, -1);
    }

    public List<CommentAddition> findCommentAdditionEntities(int maxResults, int firstResult) {
        return findCommentAdditionEntities(false, maxResults, firstResult);
    }

    private List<CommentAddition> findCommentAdditionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CommentAddition.class));
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

    public CommentAddition findCommentAddition(CommentAdditionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CommentAddition.class, id);
        } finally {
            em.close();
        }
    }

    public int getCommentAdditionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CommentAddition> rt = cq.from(CommentAddition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
