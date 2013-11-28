/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpca.controllers;

import com.mpca.controllers.exceptions.IllegalOrphanException;
import com.mpca.controllers.exceptions.NonexistentEntityException;
import com.mpca.controllers.exceptions.PreexistingEntityException;
import com.mpca.controllers.exceptions.RollbackFailureException;
import com.mpca.entities.MpcaProduct;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mpca.entities.MpcaProductAddition;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Antonio
 */
public class MpcaProductJpaController implements Serializable {

    public MpcaProductJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaProduct mpcaProduct) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (mpcaProduct.getMpcaProductAdditionList() == null) {
            mpcaProduct.setMpcaProductAdditionList(new ArrayList<MpcaProductAddition>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            List<MpcaProductAddition> attachedMpcaProductAdditionList = new ArrayList<MpcaProductAddition>();
            for (MpcaProductAddition mpcaProductAdditionListMpcaProductAdditionToAttach : mpcaProduct.getMpcaProductAdditionList()) {
                mpcaProductAdditionListMpcaProductAdditionToAttach = em.getReference(mpcaProductAdditionListMpcaProductAdditionToAttach.getClass(), mpcaProductAdditionListMpcaProductAdditionToAttach.getMpcaProductAdditionPK());
                attachedMpcaProductAdditionList.add(mpcaProductAdditionListMpcaProductAdditionToAttach);
            }
            mpcaProduct.setMpcaProductAdditionList(attachedMpcaProductAdditionList);
            em.persist(mpcaProduct);
            for (MpcaProductAddition mpcaProductAdditionListMpcaProductAddition : mpcaProduct.getMpcaProductAdditionList()) {
                MpcaProduct oldMpcaProductOfMpcaProductAdditionListMpcaProductAddition = mpcaProductAdditionListMpcaProductAddition.getMpcaProduct();
                mpcaProductAdditionListMpcaProductAddition.setMpcaProduct(mpcaProduct);
                mpcaProductAdditionListMpcaProductAddition = em.merge(mpcaProductAdditionListMpcaProductAddition);
                if (oldMpcaProductOfMpcaProductAdditionListMpcaProductAddition != null) {
                    oldMpcaProductOfMpcaProductAdditionListMpcaProductAddition.getMpcaProductAdditionList().remove(mpcaProductAdditionListMpcaProductAddition);
                    oldMpcaProductOfMpcaProductAdditionListMpcaProductAddition = em.merge(oldMpcaProductOfMpcaProductAdditionListMpcaProductAddition);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMpcaProduct(mpcaProduct.getProductId()) != null) {
                throw new PreexistingEntityException("MpcaProduct " + mpcaProduct + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaProduct mpcaProduct) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaProduct persistentMpcaProduct = em.find(MpcaProduct.class, mpcaProduct.getProductId());
            List<MpcaProductAddition> mpcaProductAdditionListOld = persistentMpcaProduct.getMpcaProductAdditionList();
            List<MpcaProductAddition> mpcaProductAdditionListNew = mpcaProduct.getMpcaProductAdditionList();
            List<String> illegalOrphanMessages = null;
            for (MpcaProductAddition mpcaProductAdditionListOldMpcaProductAddition : mpcaProductAdditionListOld) {
                if (!mpcaProductAdditionListNew.contains(mpcaProductAdditionListOldMpcaProductAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductAddition " + mpcaProductAdditionListOldMpcaProductAddition + " since its mpcaProduct field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaProductAddition> attachedMpcaProductAdditionListNew = new ArrayList<MpcaProductAddition>();
            for (MpcaProductAddition mpcaProductAdditionListNewMpcaProductAdditionToAttach : mpcaProductAdditionListNew) {
                mpcaProductAdditionListNewMpcaProductAdditionToAttach = em.getReference(mpcaProductAdditionListNewMpcaProductAdditionToAttach.getClass(), mpcaProductAdditionListNewMpcaProductAdditionToAttach.getMpcaProductAdditionPK());
                attachedMpcaProductAdditionListNew.add(mpcaProductAdditionListNewMpcaProductAdditionToAttach);
            }
            mpcaProductAdditionListNew = attachedMpcaProductAdditionListNew;
            mpcaProduct.setMpcaProductAdditionList(mpcaProductAdditionListNew);
            mpcaProduct = em.merge(mpcaProduct);
            for (MpcaProductAddition mpcaProductAdditionListNewMpcaProductAddition : mpcaProductAdditionListNew) {
                if (!mpcaProductAdditionListOld.contains(mpcaProductAdditionListNewMpcaProductAddition)) {
                    MpcaProduct oldMpcaProductOfMpcaProductAdditionListNewMpcaProductAddition = mpcaProductAdditionListNewMpcaProductAddition.getMpcaProduct();
                    mpcaProductAdditionListNewMpcaProductAddition.setMpcaProduct(mpcaProduct);
                    mpcaProductAdditionListNewMpcaProductAddition = em.merge(mpcaProductAdditionListNewMpcaProductAddition);
                    if (oldMpcaProductOfMpcaProductAdditionListNewMpcaProductAddition != null && !oldMpcaProductOfMpcaProductAdditionListNewMpcaProductAddition.equals(mpcaProduct)) {
                        oldMpcaProductOfMpcaProductAdditionListNewMpcaProductAddition.getMpcaProductAdditionList().remove(mpcaProductAdditionListNewMpcaProductAddition);
                        oldMpcaProductOfMpcaProductAdditionListNewMpcaProductAddition = em.merge(oldMpcaProductOfMpcaProductAdditionListNewMpcaProductAddition);
                    }
                }
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
                Long id = mpcaProduct.getProductId();
                if (findMpcaProduct(id) == null) {
                    throw new NonexistentEntityException("The mpcaProduct with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaProduct mpcaProduct;
            try {
                mpcaProduct = em.getReference(MpcaProduct.class, id);
                mpcaProduct.getProductId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaProduct with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaProductAddition> mpcaProductAdditionListOrphanCheck = mpcaProduct.getMpcaProductAdditionList();
            for (MpcaProductAddition mpcaProductAdditionListOrphanCheckMpcaProductAddition : mpcaProductAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaProduct (" + mpcaProduct + ") cannot be destroyed since the MpcaProductAddition " + mpcaProductAdditionListOrphanCheckMpcaProductAddition + " in its mpcaProductAdditionList field has a non-nullable mpcaProduct field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mpcaProduct);
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

    public List<MpcaProduct> findMpcaProductEntities() {
        return findMpcaProductEntities(true, -1, -1);
    }

    public List<MpcaProduct> findMpcaProductEntities(int maxResults, int firstResult) {
        return findMpcaProductEntities(false, maxResults, firstResult);
    }

    private List<MpcaProduct> findMpcaProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaProduct.class));
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

    public MpcaProduct findMpcaProduct(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaProduct.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaProduct> rt = cq.from(MpcaProduct.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
