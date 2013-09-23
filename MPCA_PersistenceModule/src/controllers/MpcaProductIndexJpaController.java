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
import entities.MpcaProduct;
import entities.MpcaLabelType;
import entities.MpcaIndexType;
import entities.MpcaProductIndex;
import entities.MpcaProductIndexPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaProductIndexJpaController implements Serializable {

    public MpcaProductIndexJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaProductIndex mpcaProductIndex) throws PreexistingEntityException, Exception {
        if (mpcaProductIndex.getMpcaProductIndexPK() == null) {
            mpcaProductIndex.setMpcaProductIndexPK(new MpcaProductIndexPK());
        }
        mpcaProductIndex.getMpcaProductIndexPK().setMpcaProductProductId(mpcaProductIndex.getMpcaProduct().getProductId());
        mpcaProductIndex.getMpcaProductIndexPK().setMpcaIndexTypeIndexId(mpcaProductIndex.getMpcaIndexType().getIndexId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProduct mpcaProduct = mpcaProductIndex.getMpcaProduct();
            if (mpcaProduct != null) {
                mpcaProduct = em.getReference(mpcaProduct.getClass(), mpcaProduct.getProductId());
                mpcaProductIndex.setMpcaProduct(mpcaProduct);
            }
            MpcaLabelType labelId = mpcaProductIndex.getLabelId();
            if (labelId != null) {
                labelId = em.getReference(labelId.getClass(), labelId.getLabelId());
                mpcaProductIndex.setLabelId(labelId);
            }
            MpcaIndexType mpcaIndexType = mpcaProductIndex.getMpcaIndexType();
            if (mpcaIndexType != null) {
                mpcaIndexType = em.getReference(mpcaIndexType.getClass(), mpcaIndexType.getIndexId());
                mpcaProductIndex.setMpcaIndexType(mpcaIndexType);
            }
            em.persist(mpcaProductIndex);
            if (mpcaProduct != null) {
                mpcaProduct.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaProduct = em.merge(mpcaProduct);
            }
            if (labelId != null) {
                labelId.getMpcaProductIndexList().add(mpcaProductIndex);
                labelId = em.merge(labelId);
            }
            if (mpcaIndexType != null) {
                mpcaIndexType.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaIndexType = em.merge(mpcaIndexType);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaProductIndex(mpcaProductIndex.getMpcaProductIndexPK()) != null) {
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
        mpcaProductIndex.getMpcaProductIndexPK().setMpcaProductProductId(mpcaProductIndex.getMpcaProduct().getProductId());
        mpcaProductIndex.getMpcaProductIndexPK().setMpcaIndexTypeIndexId(mpcaProductIndex.getMpcaIndexType().getIndexId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProductIndex persistentMpcaProductIndex = em.find(MpcaProductIndex.class, mpcaProductIndex.getMpcaProductIndexPK());
            MpcaProduct mpcaProductOld = persistentMpcaProductIndex.getMpcaProduct();
            MpcaProduct mpcaProductNew = mpcaProductIndex.getMpcaProduct();
            MpcaLabelType labelIdOld = persistentMpcaProductIndex.getLabelId();
            MpcaLabelType labelIdNew = mpcaProductIndex.getLabelId();
            MpcaIndexType mpcaIndexTypeOld = persistentMpcaProductIndex.getMpcaIndexType();
            MpcaIndexType mpcaIndexTypeNew = mpcaProductIndex.getMpcaIndexType();
            if (mpcaProductNew != null) {
                mpcaProductNew = em.getReference(mpcaProductNew.getClass(), mpcaProductNew.getProductId());
                mpcaProductIndex.setMpcaProduct(mpcaProductNew);
            }
            if (labelIdNew != null) {
                labelIdNew = em.getReference(labelIdNew.getClass(), labelIdNew.getLabelId());
                mpcaProductIndex.setLabelId(labelIdNew);
            }
            if (mpcaIndexTypeNew != null) {
                mpcaIndexTypeNew = em.getReference(mpcaIndexTypeNew.getClass(), mpcaIndexTypeNew.getIndexId());
                mpcaProductIndex.setMpcaIndexType(mpcaIndexTypeNew);
            }
            mpcaProductIndex = em.merge(mpcaProductIndex);
            if (mpcaProductOld != null && !mpcaProductOld.equals(mpcaProductNew)) {
                mpcaProductOld.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaProductOld = em.merge(mpcaProductOld);
            }
            if (mpcaProductNew != null && !mpcaProductNew.equals(mpcaProductOld)) {
                mpcaProductNew.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaProductNew = em.merge(mpcaProductNew);
            }
            if (labelIdOld != null && !labelIdOld.equals(labelIdNew)) {
                labelIdOld.getMpcaProductIndexList().remove(mpcaProductIndex);
                labelIdOld = em.merge(labelIdOld);
            }
            if (labelIdNew != null && !labelIdNew.equals(labelIdOld)) {
                labelIdNew.getMpcaProductIndexList().add(mpcaProductIndex);
                labelIdNew = em.merge(labelIdNew);
            }
            if (mpcaIndexTypeOld != null && !mpcaIndexTypeOld.equals(mpcaIndexTypeNew)) {
                mpcaIndexTypeOld.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaIndexTypeOld = em.merge(mpcaIndexTypeOld);
            }
            if (mpcaIndexTypeNew != null && !mpcaIndexTypeNew.equals(mpcaIndexTypeOld)) {
                mpcaIndexTypeNew.getMpcaProductIndexList().add(mpcaProductIndex);
                mpcaIndexTypeNew = em.merge(mpcaIndexTypeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MpcaProductIndexPK id = mpcaProductIndex.getMpcaProductIndexPK();
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

    public void destroy(MpcaProductIndexPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProductIndex mpcaProductIndex;
            try {
                mpcaProductIndex = em.getReference(MpcaProductIndex.class, id);
                mpcaProductIndex.getMpcaProductIndexPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaProductIndex with id " + id + " no longer exists.", enfe);
            }
            MpcaProduct mpcaProduct = mpcaProductIndex.getMpcaProduct();
            if (mpcaProduct != null) {
                mpcaProduct.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaProduct = em.merge(mpcaProduct);
            }
            MpcaLabelType labelId = mpcaProductIndex.getLabelId();
            if (labelId != null) {
                labelId.getMpcaProductIndexList().remove(mpcaProductIndex);
                labelId = em.merge(labelId);
            }
            MpcaIndexType mpcaIndexType = mpcaProductIndex.getMpcaIndexType();
            if (mpcaIndexType != null) {
                mpcaIndexType.getMpcaProductIndexList().remove(mpcaProductIndex);
                mpcaIndexType = em.merge(mpcaIndexType);
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

    public MpcaProductIndex findMpcaProductIndex(MpcaProductIndexPK id) {
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
    
}
