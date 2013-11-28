/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpca.controllers;

import com.mpca.controllers.exceptions.IllegalOrphanException;
import com.mpca.controllers.exceptions.NonexistentEntityException;
import com.mpca.controllers.exceptions.PreexistingEntityException;
import com.mpca.controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mpca.entities.MpcaAdditionCategory;
import com.mpca.entities.MpcaAdditionType;
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
public class MpcaAdditionTypeJpaController implements Serializable {

    public MpcaAdditionTypeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaAdditionType mpcaAdditionType) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (mpcaAdditionType.getMpcaProductAdditionList() == null) {
            mpcaAdditionType.setMpcaProductAdditionList(new ArrayList<MpcaProductAddition>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaAdditionCategory categoryId = mpcaAdditionType.getCategoryId();
            if (categoryId != null) {
                categoryId = em.getReference(categoryId.getClass(), categoryId.getCategoryId());
                mpcaAdditionType.setCategoryId(categoryId);
            }
            List<MpcaProductAddition> attachedMpcaProductAdditionList = new ArrayList<MpcaProductAddition>();
            for (MpcaProductAddition mpcaProductAdditionListMpcaProductAdditionToAttach : mpcaAdditionType.getMpcaProductAdditionList()) {
                mpcaProductAdditionListMpcaProductAdditionToAttach = em.getReference(mpcaProductAdditionListMpcaProductAdditionToAttach.getClass(), mpcaProductAdditionListMpcaProductAdditionToAttach.getMpcaProductAdditionPK());
                attachedMpcaProductAdditionList.add(mpcaProductAdditionListMpcaProductAdditionToAttach);
            }
            mpcaAdditionType.setMpcaProductAdditionList(attachedMpcaProductAdditionList);
            em.persist(mpcaAdditionType);
            if (categoryId != null) {
                categoryId.getMpcaAdditionTypeList().add(mpcaAdditionType);
                categoryId = em.merge(categoryId);
            }
            for (MpcaProductAddition mpcaProductAdditionListMpcaProductAddition : mpcaAdditionType.getMpcaProductAdditionList()) {
                MpcaAdditionType oldMpcaAdditionTypeOfMpcaProductAdditionListMpcaProductAddition = mpcaProductAdditionListMpcaProductAddition.getMpcaAdditionType();
                mpcaProductAdditionListMpcaProductAddition.setMpcaAdditionType(mpcaAdditionType);
                mpcaProductAdditionListMpcaProductAddition = em.merge(mpcaProductAdditionListMpcaProductAddition);
                if (oldMpcaAdditionTypeOfMpcaProductAdditionListMpcaProductAddition != null) {
                    oldMpcaAdditionTypeOfMpcaProductAdditionListMpcaProductAddition.getMpcaProductAdditionList().remove(mpcaProductAdditionListMpcaProductAddition);
                    oldMpcaAdditionTypeOfMpcaProductAdditionListMpcaProductAddition = em.merge(oldMpcaAdditionTypeOfMpcaProductAdditionListMpcaProductAddition);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findMpcaAdditionType(mpcaAdditionType.getAddId()) != null) {
                throw new PreexistingEntityException("MpcaAdditionType " + mpcaAdditionType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaAdditionType mpcaAdditionType) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            MpcaAdditionType persistentMpcaAdditionType = em.find(MpcaAdditionType.class, mpcaAdditionType.getAddId());
            MpcaAdditionCategory categoryIdOld = persistentMpcaAdditionType.getCategoryId();
            MpcaAdditionCategory categoryIdNew = mpcaAdditionType.getCategoryId();
            List<MpcaProductAddition> mpcaProductAdditionListOld = persistentMpcaAdditionType.getMpcaProductAdditionList();
            List<MpcaProductAddition> mpcaProductAdditionListNew = mpcaAdditionType.getMpcaProductAdditionList();
            List<String> illegalOrphanMessages = null;
            for (MpcaProductAddition mpcaProductAdditionListOldMpcaProductAddition : mpcaProductAdditionListOld) {
                if (!mpcaProductAdditionListNew.contains(mpcaProductAdditionListOldMpcaProductAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductAddition " + mpcaProductAdditionListOldMpcaProductAddition + " since its mpcaAdditionType field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (categoryIdNew != null) {
                categoryIdNew = em.getReference(categoryIdNew.getClass(), categoryIdNew.getCategoryId());
                mpcaAdditionType.setCategoryId(categoryIdNew);
            }
            List<MpcaProductAddition> attachedMpcaProductAdditionListNew = new ArrayList<MpcaProductAddition>();
            for (MpcaProductAddition mpcaProductAdditionListNewMpcaProductAdditionToAttach : mpcaProductAdditionListNew) {
                mpcaProductAdditionListNewMpcaProductAdditionToAttach = em.getReference(mpcaProductAdditionListNewMpcaProductAdditionToAttach.getClass(), mpcaProductAdditionListNewMpcaProductAdditionToAttach.getMpcaProductAdditionPK());
                attachedMpcaProductAdditionListNew.add(mpcaProductAdditionListNewMpcaProductAdditionToAttach);
            }
            mpcaProductAdditionListNew = attachedMpcaProductAdditionListNew;
            mpcaAdditionType.setMpcaProductAdditionList(mpcaProductAdditionListNew);
            mpcaAdditionType = em.merge(mpcaAdditionType);
            if (categoryIdOld != null && !categoryIdOld.equals(categoryIdNew)) {
                categoryIdOld.getMpcaAdditionTypeList().remove(mpcaAdditionType);
                categoryIdOld = em.merge(categoryIdOld);
            }
            if (categoryIdNew != null && !categoryIdNew.equals(categoryIdOld)) {
                categoryIdNew.getMpcaAdditionTypeList().add(mpcaAdditionType);
                categoryIdNew = em.merge(categoryIdNew);
            }
            for (MpcaProductAddition mpcaProductAdditionListNewMpcaProductAddition : mpcaProductAdditionListNew) {
                if (!mpcaProductAdditionListOld.contains(mpcaProductAdditionListNewMpcaProductAddition)) {
                    MpcaAdditionType oldMpcaAdditionTypeOfMpcaProductAdditionListNewMpcaProductAddition = mpcaProductAdditionListNewMpcaProductAddition.getMpcaAdditionType();
                    mpcaProductAdditionListNewMpcaProductAddition.setMpcaAdditionType(mpcaAdditionType);
                    mpcaProductAdditionListNewMpcaProductAddition = em.merge(mpcaProductAdditionListNewMpcaProductAddition);
                    if (oldMpcaAdditionTypeOfMpcaProductAdditionListNewMpcaProductAddition != null && !oldMpcaAdditionTypeOfMpcaProductAdditionListNewMpcaProductAddition.equals(mpcaAdditionType)) {
                        oldMpcaAdditionTypeOfMpcaProductAdditionListNewMpcaProductAddition.getMpcaProductAdditionList().remove(mpcaProductAdditionListNewMpcaProductAddition);
                        oldMpcaAdditionTypeOfMpcaProductAdditionListNewMpcaProductAddition = em.merge(oldMpcaAdditionTypeOfMpcaProductAdditionListNewMpcaProductAddition);
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
                Long id = mpcaAdditionType.getAddId();
                if (findMpcaAdditionType(id) == null) {
                    throw new NonexistentEntityException("The mpcaAdditionType with id " + id + " no longer exists.");
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
            MpcaAdditionType mpcaAdditionType;
            try {
                mpcaAdditionType = em.getReference(MpcaAdditionType.class, id);
                mpcaAdditionType.getAddId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaAdditionType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaProductAddition> mpcaProductAdditionListOrphanCheck = mpcaAdditionType.getMpcaProductAdditionList();
            for (MpcaProductAddition mpcaProductAdditionListOrphanCheckMpcaProductAddition : mpcaProductAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaAdditionType (" + mpcaAdditionType + ") cannot be destroyed since the MpcaProductAddition " + mpcaProductAdditionListOrphanCheckMpcaProductAddition + " in its mpcaProductAdditionList field has a non-nullable mpcaAdditionType field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MpcaAdditionCategory categoryId = mpcaAdditionType.getCategoryId();
            if (categoryId != null) {
                categoryId.getMpcaAdditionTypeList().remove(mpcaAdditionType);
                categoryId = em.merge(categoryId);
            }
            em.remove(mpcaAdditionType);
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

    public List<MpcaAdditionType> findMpcaAdditionTypeEntities() {
        return findMpcaAdditionTypeEntities(true, -1, -1);
    }

    public List<MpcaAdditionType> findMpcaAdditionTypeEntities(int maxResults, int firstResult) {
        return findMpcaAdditionTypeEntities(false, maxResults, firstResult);
    }

    private List<MpcaAdditionType> findMpcaAdditionTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaAdditionType.class));
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

    public MpcaAdditionType findMpcaAdditionType(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaAdditionType.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaAdditionTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaAdditionType> rt = cq.from(MpcaAdditionType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
