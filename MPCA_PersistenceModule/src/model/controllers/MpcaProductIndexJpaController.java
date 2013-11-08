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
import model.entities.MpcaProduct;
import model.entities.MpcaLabelType;
import model.entities.MpcaIndexType;
import model.entities.MpcaProductIndex;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Antonio
 */
public class MpcaProductIndexJpaController extends JpaController implements Serializable {

    public void create(MpcaProductIndex mpcaProductIndex) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProduct mpcaProductProductId = mpcaProductIndex.getMpcaProductProductId();
            if (mpcaProductProductId != null) {
                mpcaProductProductId = em.getReference(mpcaProductProductId.getClass(), mpcaProductProductId.getProductId());
                mpcaProductIndex.setMpcaProductProductId(mpcaProductProductId);
            }
            MpcaLabelType labelId = mpcaProductIndex.getLabelId();
            if (labelId != null) {
                labelId = em.getReference(labelId.getClass(), labelId.getLabelId());
                mpcaProductIndex.setLabelId(labelId);
            }
            MpcaIndexType mpcaIndexTypeIndexId = mpcaProductIndex.getMpcaIndexTypeIndexId();
            if (mpcaIndexTypeIndexId != null) {
                mpcaIndexTypeIndexId = em.getReference(mpcaIndexTypeIndexId.getClass(), mpcaIndexTypeIndexId.getIndexId());
                mpcaProductIndex.setMpcaIndexTypeIndexId(mpcaIndexTypeIndexId);
            }
            em.persist(mpcaProductIndex);
            if (mpcaProductProductId != null) {
                mpcaProductProductId.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaProductProductId = em.merge(mpcaProductProductId);
            }
            if (labelId != null) {
                labelId.getMpcaProductIndexList().add(mpcaProductIndex);
                labelId = em.merge(labelId);
            }
            if (mpcaIndexTypeIndexId != null) {
                mpcaIndexTypeIndexId.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaIndexTypeIndexId = em.merge(mpcaIndexTypeIndexId);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaProductIndex(mpcaProductIndex.getProductIndexId()) != null) {
                throw new PreexistingEntityException("MpcaProductIndex " + mpcaProductIndex + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaProductIndex mpcaProductIndex) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProductIndex persistentMpcaProductIndex = em.find(MpcaProductIndex.class, mpcaProductIndex.getProductIndexId());
            MpcaProduct mpcaProductProductIdOld = persistentMpcaProductIndex.getMpcaProductProductId();
            MpcaProduct mpcaProductProductIdNew = mpcaProductIndex.getMpcaProductProductId();
            MpcaLabelType labelIdOld = persistentMpcaProductIndex.getLabelId();
            MpcaLabelType labelIdNew = mpcaProductIndex.getLabelId();
            MpcaIndexType mpcaIndexTypeIndexIdOld = persistentMpcaProductIndex.getMpcaIndexTypeIndexId();
            MpcaIndexType mpcaIndexTypeIndexIdNew = mpcaProductIndex.getMpcaIndexTypeIndexId();
            if (mpcaProductProductIdNew != null) {
                mpcaProductProductIdNew = em.getReference(mpcaProductProductIdNew.getClass(), mpcaProductProductIdNew.getProductId());
                mpcaProductIndex.setMpcaProductProductId(mpcaProductProductIdNew);
            }
            if (labelIdNew != null) {
                labelIdNew = em.getReference(labelIdNew.getClass(), labelIdNew.getLabelId());
                mpcaProductIndex.setLabelId(labelIdNew);
            }
            if (mpcaIndexTypeIndexIdNew != null) {
                mpcaIndexTypeIndexIdNew = em.getReference(mpcaIndexTypeIndexIdNew.getClass(), mpcaIndexTypeIndexIdNew.getIndexId());
                mpcaProductIndex.setMpcaIndexTypeIndexId(mpcaIndexTypeIndexIdNew);
            }
            mpcaProductIndex = em.merge(mpcaProductIndex);
            if (mpcaProductProductIdOld != null && !mpcaProductProductIdOld.equals(mpcaProductProductIdNew)) {
                mpcaProductProductIdOld.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaProductProductIdOld = em.merge(mpcaProductProductIdOld);
            }
            if (mpcaProductProductIdNew != null && !mpcaProductProductIdNew.equals(mpcaProductProductIdOld)) {
                mpcaProductProductIdNew.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaProductProductIdNew = em.merge(mpcaProductProductIdNew);
            }
            if (labelIdOld != null && !labelIdOld.equals(labelIdNew)) {
                labelIdOld.getMpcaProductIndexList().remove(mpcaProductIndex);
                labelIdOld = em.merge(labelIdOld);
            }
            if (labelIdNew != null && !labelIdNew.equals(labelIdOld)) {
                labelIdNew.getMpcaProductIndexList().add(mpcaProductIndex);
                labelIdNew = em.merge(labelIdNew);
            }
            if (mpcaIndexTypeIndexIdOld != null && !mpcaIndexTypeIndexIdOld.equals(mpcaIndexTypeIndexIdNew)) {
                mpcaIndexTypeIndexIdOld.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaIndexTypeIndexIdOld = em.merge(mpcaIndexTypeIndexIdOld);
            }
            if (mpcaIndexTypeIndexIdNew != null && !mpcaIndexTypeIndexIdNew.equals(mpcaIndexTypeIndexIdOld)) {
                mpcaIndexTypeIndexIdNew.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaIndexTypeIndexIdNew = em.merge(mpcaIndexTypeIndexIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaProductIndex.getProductIndexId();
                if (findMpcaProductIndex(id) == null) {
                    throw new NonexistentEntityException("The mpcaProductIndex with id " + id + " no longer exists.");
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
            MpcaProductIndex mpcaProductIndex;
            try {
                mpcaProductIndex = em.getReference(MpcaProductIndex.class, id);
                mpcaProductIndex.getProductIndexId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaProductIndex with id " + id + " no longer exists.", enfe);
            }
            MpcaProduct mpcaProductProductId = mpcaProductIndex.getMpcaProductProductId();
            if (mpcaProductProductId != null) {
                mpcaProductProductId.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaProductProductId = em.merge(mpcaProductProductId);
            }
            MpcaLabelType labelId = mpcaProductIndex.getLabelId();
            if (labelId != null) {
                labelId.getMpcaProductIndexList().remove(mpcaProductIndex);
                labelId = em.merge(labelId);
            }
            MpcaIndexType mpcaIndexTypeIndexId = mpcaProductIndex.getMpcaIndexTypeIndexId();
            if (mpcaIndexTypeIndexId != null) {
                mpcaIndexTypeIndexId.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaIndexTypeIndexId = em.merge(mpcaIndexTypeIndexId);
            }
            em.remove(mpcaProductIndex);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaProductIndex> findMpcaProductIndexEntities() {
        return findMpcaProductIndexEntities(true, -1, -1);
    }

    public List<MpcaProductIndex> findMpcaProductIndexEntities(int maxResults, int firstResult) {
        return findMpcaProductIndexEntities(false, maxResults, firstResult);
    }

    private List<MpcaProductIndex> findMpcaProductIndexEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaProductIndex.class));
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

    public MpcaProductIndex findMpcaProductIndex(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaProductIndex.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaProductIndexCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaProductIndex> rt = cq.from(MpcaProductIndex.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public MpcaProductIndex findProductIndexByProductIndexAndLabel(MpcaProduct product, MpcaIndexType index, MpcaLabelType label) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaProductIndex.findByProductAndIndexTpeAndLabel");
            q.setParameter("productId", product.getProductId());
            q.setParameter("indexId", index.getIndexId());
            q.setParameter("labelId", label.getLabelId());
            List<MpcaProductIndex> pis = q.getResultList();
            MpcaProductIndex pi = null;
            if(!pis.isEmpty()) {
                pi = pis.get(0);
            }
            return pi;
        } finally {
            em.close();
        }
    }
    
    public List<MpcaProductIndex> findProductIndexByProductAndIndex(MpcaProduct product, MpcaIndexType index) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaProductIndex.findByProductAndIndexTpe");
            q.setParameter("productId", product.getProductId());
            q.setParameter("indexId", index.getIndexId());
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
