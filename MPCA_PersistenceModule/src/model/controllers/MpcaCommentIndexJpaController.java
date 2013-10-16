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
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.entities.MpcaProduct;

/**
 *
 * @author SimonXPS
 */
public class MpcaCommentIndexJpaController extends JpaController implements Serializable {

    public void create(MpcaCommentIndex mpcaCommentIndex) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaLabelType labelId = mpcaCommentIndex.getLabelId();
            if (labelId != null) {
                labelId = em.getReference(labelId.getClass(), labelId.getLabelId());
                mpcaCommentIndex.setLabelId(labelId);
            }
            MpcaIndexType mpcaIndexTypeIndexId = mpcaCommentIndex.getMpcaIndexTypeIndexId();
            if (mpcaIndexTypeIndexId != null) {
                mpcaIndexTypeIndexId = em.getReference(mpcaIndexTypeIndexId.getClass(), mpcaIndexTypeIndexId.getIndexId());
                mpcaCommentIndex.setMpcaIndexTypeIndexId(mpcaIndexTypeIndexId);
            }
            MpcaComment mpcaCommentCommentId = mpcaCommentIndex.getMpcaCommentCommentId();
            if (mpcaCommentCommentId != null) {
                mpcaCommentCommentId = em.getReference(mpcaCommentCommentId.getClass(), mpcaCommentCommentId.getCommentId());
                mpcaCommentIndex.setMpcaCommentCommentId(mpcaCommentCommentId);
            }
            em.persist(mpcaCommentIndex);
            if (labelId != null) {
                labelId.getMpcaCommentIndexList().add(mpcaCommentIndex);
                labelId = em.merge(labelId);
            }
            if (mpcaIndexTypeIndexId != null) {
                mpcaIndexTypeIndexId.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaIndexTypeIndexId = em.merge(mpcaIndexTypeIndexId);
            }
            if (mpcaCommentCommentId != null) {
                mpcaCommentCommentId.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaCommentCommentId = em.merge(mpcaCommentCommentId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaCommentIndex(mpcaCommentIndex.getCommentIndexId()) != null) {
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
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaCommentIndex persistentMpcaCommentIndex = em.find(MpcaCommentIndex.class, mpcaCommentIndex.getCommentIndexId());
            MpcaLabelType labelIdOld = persistentMpcaCommentIndex.getLabelId();
            MpcaLabelType labelIdNew = mpcaCommentIndex.getLabelId();
            MpcaIndexType mpcaIndexTypeIndexIdOld = persistentMpcaCommentIndex.getMpcaIndexTypeIndexId();
            MpcaIndexType mpcaIndexTypeIndexIdNew = mpcaCommentIndex.getMpcaIndexTypeIndexId();
            MpcaComment mpcaCommentCommentIdOld = persistentMpcaCommentIndex.getMpcaCommentCommentId();
            MpcaComment mpcaCommentCommentIdNew = mpcaCommentIndex.getMpcaCommentCommentId();
            if (labelIdNew != null) {
                labelIdNew = em.getReference(labelIdNew.getClass(), labelIdNew.getLabelId());
                mpcaCommentIndex.setLabelId(labelIdNew);
            }
            if (mpcaIndexTypeIndexIdNew != null) {
                mpcaIndexTypeIndexIdNew = em.getReference(mpcaIndexTypeIndexIdNew.getClass(), mpcaIndexTypeIndexIdNew.getIndexId());
                mpcaCommentIndex.setMpcaIndexTypeIndexId(mpcaIndexTypeIndexIdNew);
            }
            if (mpcaCommentCommentIdNew != null) {
                mpcaCommentCommentIdNew = em.getReference(mpcaCommentCommentIdNew.getClass(), mpcaCommentCommentIdNew.getCommentId());
                mpcaCommentIndex.setMpcaCommentCommentId(mpcaCommentCommentIdNew);
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
            if (mpcaIndexTypeIndexIdOld != null && !mpcaIndexTypeIndexIdOld.equals(mpcaIndexTypeIndexIdNew)) {
                mpcaIndexTypeIndexIdOld.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaIndexTypeIndexIdOld = em.merge(mpcaIndexTypeIndexIdOld);
            }
            if (mpcaIndexTypeIndexIdNew != null && !mpcaIndexTypeIndexIdNew.equals(mpcaIndexTypeIndexIdOld)) {
                mpcaIndexTypeIndexIdNew.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaIndexTypeIndexIdNew = em.merge(mpcaIndexTypeIndexIdNew);
            }
            if (mpcaCommentCommentIdOld != null && !mpcaCommentCommentIdOld.equals(mpcaCommentCommentIdNew)) {
                mpcaCommentCommentIdOld.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaCommentCommentIdOld = em.merge(mpcaCommentCommentIdOld);
            }
            if (mpcaCommentCommentIdNew != null && !mpcaCommentCommentIdNew.equals(mpcaCommentCommentIdOld)) {
                mpcaCommentCommentIdNew.getMpcaCommentIndexList().add(mpcaCommentIndex);
                mpcaCommentCommentIdNew = em.merge(mpcaCommentCommentIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaCommentIndex.getCommentIndexId();
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

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaCommentIndex mpcaCommentIndex;
            try {
                mpcaCommentIndex = em.getReference(MpcaCommentIndex.class, id);
                mpcaCommentIndex.getCommentIndexId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaCommentIndex with id " + id + " no longer exists.", enfe);
            }
            MpcaLabelType labelId = mpcaCommentIndex.getLabelId();
            if (labelId != null) {
                labelId.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                labelId = em.merge(labelId);
            }
            MpcaIndexType mpcaIndexTypeIndexId = mpcaCommentIndex.getMpcaIndexTypeIndexId();
            if (mpcaIndexTypeIndexId != null) {
                mpcaIndexTypeIndexId.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaIndexTypeIndexId = em.merge(mpcaIndexTypeIndexId);
            }
            MpcaComment mpcaCommentCommentId = mpcaCommentIndex.getMpcaCommentCommentId();
            if (mpcaCommentCommentId != null) {
                mpcaCommentCommentId.getMpcaCommentIndexList().remove(mpcaCommentIndex);
                mpcaCommentCommentId = em.merge(mpcaCommentCommentId);
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

    public MpcaCommentIndex findMpcaCommentIndex(Long id) {
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

    public int deleteByIndexType(MpcaIndexType indexType) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Query q = em.createNamedQuery("MpcaCommentIndex.DeleteByIndexTypeId");
            q.setParameter("indexTypeId", indexType.getIndexId());
            int deletedItemsCount = q.executeUpdate();
            em.getTransaction().commit();
            return deletedItemsCount;
        } finally {
            em.close();
        }
    }
    
    public List<MpcaCommentIndex> findMpcaCommentIndexByProductAndIndexType(MpcaProduct product, MpcaIndexType indexType) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaCommentIndex.DeleteByProductAndIndexType");
            q.setParameter("productId", product.getProductId());
            q.setParameter("indexId", indexType.getIndexId());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
