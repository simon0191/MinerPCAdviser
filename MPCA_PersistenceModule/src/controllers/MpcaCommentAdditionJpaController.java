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
import entities.MpcaAdditionType;
import entities.MpcaCommentAddition;
import entities.MpcaCommentAdditionPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaCommentAdditionJpaController implements Serializable {

    public MpcaCommentAdditionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaCommentAddition mpcaCommentAddition) throws PreexistingEntityException, Exception {
        if (mpcaCommentAddition.getMpcaCommentAdditionPK() == null) {
            mpcaCommentAddition.setMpcaCommentAdditionPK(new MpcaCommentAdditionPK());
        }
        mpcaCommentAddition.getMpcaCommentAdditionPK().setMpcaCommentCommentId(mpcaCommentAddition.getMpcaComment().getCommentId());
        mpcaCommentAddition.getMpcaCommentAdditionPK().setMpcaAdditionTypeAddId(mpcaCommentAddition.getMpcaAdditionType().getAddId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaComment mpcaComment = mpcaCommentAddition.getMpcaComment();
            if (mpcaComment != null) {
                mpcaComment = em.getReference(mpcaComment.getClass(), mpcaComment.getCommentId());
                mpcaCommentAddition.setMpcaComment(mpcaComment);
            }
            MpcaAdditionType mpcaAdditionType = mpcaCommentAddition.getMpcaAdditionType();
            if (mpcaAdditionType != null) {
                mpcaAdditionType = em.getReference(mpcaAdditionType.getClass(), mpcaAdditionType.getAddId());
                mpcaCommentAddition.setMpcaAdditionType(mpcaAdditionType);
            }
            em.persist(mpcaCommentAddition);
            if (mpcaComment != null) {
                mpcaComment.getMpcaCommentAdditionList().add(mpcaCommentAddition);
                mpcaComment = em.merge(mpcaComment);
            }
            if (mpcaAdditionType != null) {
                mpcaAdditionType.getMpcaCommentAdditionList().add(mpcaCommentAddition);
                mpcaAdditionType = em.merge(mpcaAdditionType);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaCommentAddition(mpcaCommentAddition.getMpcaCommentAdditionPK()) != null) {
                throw new PreexistingEntityException("MpcaCommentAddition " + mpcaCommentAddition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaCommentAddition mpcaCommentAddition) throws NonexistentEntityException, Exception {
        mpcaCommentAddition.getMpcaCommentAdditionPK().setMpcaCommentCommentId(mpcaCommentAddition.getMpcaComment().getCommentId());
        mpcaCommentAddition.getMpcaCommentAdditionPK().setMpcaAdditionTypeAddId(mpcaCommentAddition.getMpcaAdditionType().getAddId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaCommentAddition persistentMpcaCommentAddition = em.find(MpcaCommentAddition.class, mpcaCommentAddition.getMpcaCommentAdditionPK());
            MpcaComment mpcaCommentOld = persistentMpcaCommentAddition.getMpcaComment();
            MpcaComment mpcaCommentNew = mpcaCommentAddition.getMpcaComment();
            MpcaAdditionType mpcaAdditionTypeOld = persistentMpcaCommentAddition.getMpcaAdditionType();
            MpcaAdditionType mpcaAdditionTypeNew = mpcaCommentAddition.getMpcaAdditionType();
            if (mpcaCommentNew != null) {
                mpcaCommentNew = em.getReference(mpcaCommentNew.getClass(), mpcaCommentNew.getCommentId());
                mpcaCommentAddition.setMpcaComment(mpcaCommentNew);
            }
            if (mpcaAdditionTypeNew != null) {
                mpcaAdditionTypeNew = em.getReference(mpcaAdditionTypeNew.getClass(), mpcaAdditionTypeNew.getAddId());
                mpcaCommentAddition.setMpcaAdditionType(mpcaAdditionTypeNew);
            }
            mpcaCommentAddition = em.merge(mpcaCommentAddition);
            if (mpcaCommentOld != null && !mpcaCommentOld.equals(mpcaCommentNew)) {
                mpcaCommentOld.getMpcaCommentAdditionList().remove(mpcaCommentAddition);
                mpcaCommentOld = em.merge(mpcaCommentOld);
            }
            if (mpcaCommentNew != null && !mpcaCommentNew.equals(mpcaCommentOld)) {
                mpcaCommentNew.getMpcaCommentAdditionList().add(mpcaCommentAddition);
                mpcaCommentNew = em.merge(mpcaCommentNew);
            }
            if (mpcaAdditionTypeOld != null && !mpcaAdditionTypeOld.equals(mpcaAdditionTypeNew)) {
                mpcaAdditionTypeOld.getMpcaCommentAdditionList().remove(mpcaCommentAddition);
                mpcaAdditionTypeOld = em.merge(mpcaAdditionTypeOld);
            }
            if (mpcaAdditionTypeNew != null && !mpcaAdditionTypeNew.equals(mpcaAdditionTypeOld)) {
                mpcaAdditionTypeNew.getMpcaCommentAdditionList().add(mpcaCommentAddition);
                mpcaAdditionTypeNew = em.merge(mpcaAdditionTypeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MpcaCommentAdditionPK id = mpcaCommentAddition.getMpcaCommentAdditionPK();
                if (findMpcaCommentAddition(id) == null) {
                    throw new NonexistentEntityException("The mpcaCommentAddition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MpcaCommentAdditionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaCommentAddition mpcaCommentAddition;
            try {
                mpcaCommentAddition = em.getReference(MpcaCommentAddition.class, id);
                mpcaCommentAddition.getMpcaCommentAdditionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaCommentAddition with id " + id + " no longer exists.", enfe);
            }
            MpcaComment mpcaComment = mpcaCommentAddition.getMpcaComment();
            if (mpcaComment != null) {
                mpcaComment.getMpcaCommentAdditionList().remove(mpcaCommentAddition);
                mpcaComment = em.merge(mpcaComment);
            }
            MpcaAdditionType mpcaAdditionType = mpcaCommentAddition.getMpcaAdditionType();
            if (mpcaAdditionType != null) {
                mpcaAdditionType.getMpcaCommentAdditionList().remove(mpcaCommentAddition);
                mpcaAdditionType = em.merge(mpcaAdditionType);
            }
            em.remove(mpcaCommentAddition);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaCommentAddition> findMpcaCommentAdditionEntities() {
        return findMpcaCommentAdditionEntities(true, -1, -1);
    }

    public List<MpcaCommentAddition> findMpcaCommentAdditionEntities(int maxResults, int firstResult) {
        return findMpcaCommentAdditionEntities(false, maxResults, firstResult);
    }

    private List<MpcaCommentAddition> findMpcaCommentAdditionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaCommentAddition.class));
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

    public MpcaCommentAddition findMpcaCommentAddition(MpcaCommentAdditionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaCommentAddition.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaCommentAdditionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaCommentAddition> rt = cq.from(MpcaCommentAddition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
