/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpca.controllers;

import com.mpca.controllers.exceptions.NonexistentEntityException;
import com.mpca.controllers.exceptions.PreexistingEntityException;
import com.mpca.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mpca.entities.MpcaProduct;
import com.mpca.entities.MpcaAdditionType;
import com.mpca.entities.MpcaProductAddition;
import com.mpca.entities.MpcaProductAdditionPK;
import java.util.List;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Antonio
 */
public class MpcaProductAdditionJpaController implements Serializable {

    public MpcaProductAdditionJpaController() throws NamingException {
        Context ic = (Context) new InitialContext();
        this.em = (EntityManager) ic.lookup("java:comp/env/persistence/name");
        this.utx = (UserTransaction) ic.lookup("java:comp/env/UserTransaction");
    }
    private UserTransaction utx = null;
    private EntityManager em = null;

    public EntityManager getEntityManager() {
        return em;
    }

    public void create(MpcaProductAddition mpcaProductAddition) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (mpcaProductAddition.getMpcaProductAdditionPK() == null) {
            mpcaProductAddition.setMpcaProductAdditionPK(new MpcaProductAdditionPK());
        }
        mpcaProductAddition.getMpcaProductAdditionPK().setMpcaProductProductId(mpcaProductAddition.getMpcaProduct().getProductId());
        mpcaProductAddition.getMpcaProductAdditionPK().setMpcaAdditionTypeAddId(mpcaProductAddition.getMpcaAdditionType().getAddId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaProduct mpcaProduct = mpcaProductAddition.getMpcaProduct();
            if (mpcaProduct != null) {
                mpcaProduct = em.getReference(mpcaProduct.getClass(), mpcaProduct.getProductId());
                mpcaProductAddition.setMpcaProduct(mpcaProduct);
            }
            MpcaAdditionType mpcaAdditionType = mpcaProductAddition.getMpcaAdditionType();
            if (mpcaAdditionType != null) {
                mpcaAdditionType = em.getReference(mpcaAdditionType.getClass(), mpcaAdditionType.getAddId());
                mpcaProductAddition.setMpcaAdditionType(mpcaAdditionType);
            }
            em.persist(mpcaProductAddition);
            if (mpcaProduct != null) {
                mpcaProduct.getMpcaProductAdditionList().add(mpcaProductAddition);
                mpcaProduct = em.merge(mpcaProduct);
            }
            if (mpcaAdditionType != null) {
                mpcaAdditionType.getMpcaProductAdditionList().add(mpcaProductAddition);
                mpcaAdditionType = em.merge(mpcaAdditionType);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMpcaProductAddition(mpcaProductAddition.getMpcaProductAdditionPK()) != null) {
                throw new PreexistingEntityException("MpcaProductAddition " + mpcaProductAddition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaProductAddition mpcaProductAddition) throws NonexistentEntityException, RollbackFailureException, Exception {
        mpcaProductAddition.getMpcaProductAdditionPK().setMpcaProductProductId(mpcaProductAddition.getMpcaProduct().getProductId());
        mpcaProductAddition.getMpcaProductAdditionPK().setMpcaAdditionTypeAddId(mpcaProductAddition.getMpcaAdditionType().getAddId());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaProductAddition persistentMpcaProductAddition = em.find(MpcaProductAddition.class, mpcaProductAddition.getMpcaProductAdditionPK());
            MpcaProduct mpcaProductOld = persistentMpcaProductAddition.getMpcaProduct();
            MpcaProduct mpcaProductNew = mpcaProductAddition.getMpcaProduct();
            MpcaAdditionType mpcaAdditionTypeOld = persistentMpcaProductAddition.getMpcaAdditionType();
            MpcaAdditionType mpcaAdditionTypeNew = mpcaProductAddition.getMpcaAdditionType();
            if (mpcaProductNew != null) {
                mpcaProductNew = em.getReference(mpcaProductNew.getClass(), mpcaProductNew.getProductId());
                mpcaProductAddition.setMpcaProduct(mpcaProductNew);
            }
            if (mpcaAdditionTypeNew != null) {
                mpcaAdditionTypeNew = em.getReference(mpcaAdditionTypeNew.getClass(), mpcaAdditionTypeNew.getAddId());
                mpcaProductAddition.setMpcaAdditionType(mpcaAdditionTypeNew);
            }
            mpcaProductAddition = em.merge(mpcaProductAddition);
            if (mpcaProductOld != null && !mpcaProductOld.equals(mpcaProductNew)) {
                mpcaProductOld.getMpcaProductAdditionList().remove(mpcaProductAddition);
                mpcaProductOld = em.merge(mpcaProductOld);
            }
            if (mpcaProductNew != null && !mpcaProductNew.equals(mpcaProductOld)) {
                mpcaProductNew.getMpcaProductAdditionList().add(mpcaProductAddition);
                mpcaProductNew = em.merge(mpcaProductNew);
            }
            if (mpcaAdditionTypeOld != null && !mpcaAdditionTypeOld.equals(mpcaAdditionTypeNew)) {
                mpcaAdditionTypeOld.getMpcaProductAdditionList().remove(mpcaProductAddition);
                mpcaAdditionTypeOld = em.merge(mpcaAdditionTypeOld);
            }
            if (mpcaAdditionTypeNew != null && !mpcaAdditionTypeNew.equals(mpcaAdditionTypeOld)) {
                mpcaAdditionTypeNew.getMpcaProductAdditionList().add(mpcaProductAddition);
                mpcaAdditionTypeNew = em.merge(mpcaAdditionTypeNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MpcaProductAdditionPK id = mpcaProductAddition.getMpcaProductAdditionPK();
                if (findMpcaProductAddition(id) == null) {
                    throw new NonexistentEntityException("The mpcaProductAddition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MpcaProductAdditionPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaProductAddition mpcaProductAddition;
            try {
                mpcaProductAddition = em.getReference(MpcaProductAddition.class, id);
                mpcaProductAddition.getMpcaProductAdditionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaProductAddition with id " + id + " no longer exists.", enfe);
            }
            MpcaProduct mpcaProduct = mpcaProductAddition.getMpcaProduct();
            if (mpcaProduct != null) {
                mpcaProduct.getMpcaProductAdditionList().remove(mpcaProductAddition);
                mpcaProduct = em.merge(mpcaProduct);
            }
            MpcaAdditionType mpcaAdditionType = mpcaProductAddition.getMpcaAdditionType();
            if (mpcaAdditionType != null) {
                mpcaAdditionType.getMpcaProductAdditionList().remove(mpcaProductAddition);
                mpcaAdditionType = em.merge(mpcaAdditionType);
            }
            em.remove(mpcaProductAddition);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaProductAddition> findMpcaProductAdditionEntities() {
        return findMpcaProductAdditionEntities(true, -1, -1);
    }

    public List<MpcaProductAddition> findMpcaProductAdditionEntities(int maxResults, int firstResult) {
        return findMpcaProductAdditionEntities(false, maxResults, firstResult);
    }

    private List<MpcaProductAddition> findMpcaProductAdditionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaProductAddition.class));
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

    public MpcaProductAddition findMpcaProductAddition(MpcaProductAdditionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaProductAddition.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaProductAdditionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaProductAddition> rt = cq.from(MpcaProductAddition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<MpcaProductAddition> findByType(String type) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaProductAddition.findByType");
            q.setParameter("type", type);
            return q.getResultList();
        } finally {
            em.close();
        }
    }
    
}
