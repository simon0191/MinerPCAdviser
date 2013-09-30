/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.controllers;

import model.controllers.exceptions.NonexistentEntityException;
import model.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.entities.MpcaLabelType;
import model.entities.MpcaIndexType;
import model.entities.MpcaComment;
import model.entities.MpcaCommentIndex;
import model.entities.MpcaCommentIndexPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaCommentIndexJpaController extends JpaController implements Serializable {

    public void create(MpcaCommentIndex mpcaCommentIndex) throws PreexistingEntityException, Exception {
        if (mpcaCommentIndex.getMpcaCommentIndexPK() == null) {
            mpcaCommentIndex.setMpcaCommentIndexPK(new MpcaCommentIndexPK());
        }
        mpcaCommentIndex.getMpcaCommentIndexPK().setMpcaCommentCommentId(mpcaCommentIndex.getMpcaComment().getCommentId());
        mpcaCommentIndex.getMpcaCommentIndexPK().setMpcaIndexTypeIndexId(mpcaCommentIndex.getMpcaIndexType().getIndexId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaLabelType labelId = mpcaCommentIndex.getLabelId();
            if (labelId != null) {
                labelId = em.getReference(labelId.getClass(), labelId.getLabelId());
                mpcaCommentIndex.setLabelId(labelId);
            }
            MpcaIndexType mpcaIndexType = mpcaCommentIndex.getMpcaIndexType();
            if (mpcaIndexType != null) {
                mpcaIndexType = em.getReference(mpcaIndexType.getClass(), mpcaIndexType.getIndexId());
                mpcaCommentIndex.setMpcaIndexType(mpcaIndexType);
            }
            MpcaComment mpcaComment = mpcaCommentIndex.getMpcaComment();
            if (mpcaComment != null) {
                mpcaComment = em.getReference(mpcaComment.getClass(), mpcaComment.getCommentId());
                mpcaCommentIndex.setMpcaComment(mpcaComment);
            }
            em.persist(mpcaCommentIndex);
            if (labelId != null) {
                labelId.getMpcaCommentIndexList().add(mpcaCommentIndex);
                labelId = em.merge(labelId);
            }
            if (mpcaIndexType != null) {
                mpcaIndexType.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaIndexType = em.merge(mpcaIndexType);
            }
            if (mpcaComment != null) {
                mpcaComment.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaComment = em.merge(mpcaComment);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaCommentIndex(mpcaCommentIndex.getMpcaCommentIndexPK()) != null) {
                throw new PreexistingEntityException("MpcaCommentIndex " + mpcaCommentIndex + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaCommentIndex mpcaCommentIndex) throws NonexistentEntityException, Exception {
        mpcaCommentIndex.getMpcaCommentIndexPK().setMpcaCommentCommentId(mpcaCommentIndex.getMpcaComment().getCommentId());
        mpcaCommentIndex.getMpcaCommentIndexPK().setMpcaIndexTypeIndexId(mpcaCommentIndex.getMpcaIndexType().getIndexId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaCommentIndex persistentMpcaCommentIndex = em.find(MpcaCommentIndex.class, mpcaCommentIndex.getMpcaCommentIndexPK());
            MpcaLabelType labelIdOld = persistentMpcaCommentIndex.getLabelId();
            MpcaLabelType labelIdNew = mpcaCommentIndex.getLabelId();
            MpcaIndexType mpcaIndexTypeOld = persistentMpcaCommentIndex.getMpcaIndexType();
            MpcaIndexType mpcaIndexTypeNew = mpcaCommentIndex.getMpcaIndexType();
            MpcaComment mpcaCommentOld = persistentMpcaCommentIndex.getMpcaComment();
            MpcaComment mpcaCommentNew = mpcaCommentIndex.getMpcaComment();
            if (labelIdNew != null) {
                labelIdNew = em.getReference(labelIdNew.getClass(), labelIdNew.getLabelId());
                mpcaCommentIndex.setLabelId(labelIdNew);
            }
            if (mpcaIndexTypeNew != null) {
                mpcaIndexTypeNew = em.getReference(mpcaIndexTypeNew.getClass(), mpcaIndexTypeNew.getIndexId());
                mpcaCommentIndex.setMpcaIndexType(mpcaIndexTypeNew);
            }
            if (mpcaCommentNew != null) {
                mpcaCommentNew = em.getReference(mpcaCommentNew.getClass(), mpcaCommentNew.getCommentId());
                mpcaCommentIndex.setMpcaComment(mpcaCommentNew);
            }
            mpcaCommentIndex = em.merge(mpcaCommentIndex);
            if (labelIdOld != null && !labelIdOld.equals(labelIdNew)) {
                labelIdOld.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                labelIdOld = em.merge(labelIdOld);
            }
            if (labelIdNew != null && !labelIdNew.equals(labelIdOld)) {
                labelIdNew.getMpcaCommentIndexList().add(mpcaCommentIndex);
                labelIdNew = em.merge(labelIdNew);
            }
            if (mpcaIndexTypeOld != null && !mpcaIndexTypeOld.equals(mpcaIndexTypeNew)) {
                mpcaIndexTypeOld.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaIndexTypeOld = em.merge(mpcaIndexTypeOld);
            }
            if (mpcaIndexTypeNew != null && !mpcaIndexTypeNew.equals(mpcaIndexTypeOld)) {
                mpcaIndexTypeNew.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaIndexTypeNew = em.merge(mpcaIndexTypeNew);
            }
            if (mpcaCommentOld != null && !mpcaCommentOld.equals(mpcaCommentNew)) {
                mpcaCommentOld.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaCommentOld = em.merge(mpcaCommentOld);
            }
            if (mpcaCommentNew != null && !mpcaCommentNew.equals(mpcaCommentOld)) {
                mpcaCommentNew.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaCommentNew = em.merge(mpcaCommentNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MpcaCommentIndexPK id = mpcaCommentIndex.getMpcaCommentIndexPK();
                if (findMpcaCommentIndex(id) == null) {
                    throw new NonexistentEntityException("The mpcaCommentIndex with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MpcaCommentIndexPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaCommentIndex mpcaCommentIndex;
            try {
                mpcaCommentIndex = em.getReference(MpcaCommentIndex.class, id);
                mpcaCommentIndex.getMpcaCommentIndexPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaCommentIndex with id " + id + " no longer exists.", enfe);
            }
            MpcaLabelType labelId = mpcaCommentIndex.getLabelId();
            if (labelId != null) {
                labelId.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                labelId = em.merge(labelId);
            }
            MpcaIndexType mpcaIndexType = mpcaCommentIndex.getMpcaIndexType();
            if (mpcaIndexType != null) {
                mpcaIndexType.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaIndexType = em.merge(mpcaIndexType);
            }
            MpcaComment mpcaComment = mpcaCommentIndex.getMpcaComment();
            if (mpcaComment != null) {
                mpcaComment.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaComment = em.merge(mpcaComment);
            }
            em.remove(mpcaCommentIndex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaCommentIndex> findMpcaCommentIndexEntities() {
        return findMpcaCommentIndexEntities(true, -1, -1);
    }

    public List<MpcaCommentIndex> findMpcaCommentIndexEntities(int maxResults, int firstResult) {
        return findMpcaCommentIndexEntities(false, maxResults, firstResult);
    }

    private List<MpcaCommentIndex> findMpcaCommentIndexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaCommentIndex.class));
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

    public MpcaCommentIndex findMpcaCommentIndex(MpcaCommentIndexPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaCommentIndex.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaCommentIndexCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaCommentIndex> rt = cq.from(MpcaCommentIndex.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
