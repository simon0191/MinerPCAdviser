/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.MpcaAdditionCategory;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.MpcaAdditionType;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaAdditionCategoryJpaController implements Serializable {

    public MpcaAdditionCategoryJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(MpcaAdditionCategory mpcaAdditionCategory) throws PreexistingEntityException, Exception {
        if (mpcaAdditionCategory.getMpcaAdditionTypeList() == null) {
            mpcaAdditionCategory.setMpcaAdditionTypeList(new ArrayList<MpcaAdditionType>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaAdditionType> attachedMpcaAdditionTypeList = new ArrayList<MpcaAdditionType>();
            for (MpcaAdditionType mpcaAdditionTypeListMpcaAdditionTypeToAttach : mpcaAdditionCategory.getMpcaAdditionTypeList()) {
                mpcaAdditionTypeListMpcaAdditionTypeToAttach = em.getReference(mpcaAdditionTypeListMpcaAdditionTypeToAttach.getClass(), mpcaAdditionTypeListMpcaAdditionTypeToAttach.getAddId());
                attachedMpcaAdditionTypeList.add(mpcaAdditionTypeListMpcaAdditionTypeToAttach);
            }
            mpcaAdditionCategory.setMpcaAdditionTypeList(attachedMpcaAdditionTypeList);
            em.persist(mpcaAdditionCategory);
            for (MpcaAdditionType mpcaAdditionTypeListMpcaAdditionType : mpcaAdditionCategory.getMpcaAdditionTypeList()) {
                MpcaAdditionCategory oldCategoryIdOfMpcaAdditionTypeListMpcaAdditionType = mpcaAdditionTypeListMpcaAdditionType.getCategoryId();
                mpcaAdditionTypeListMpcaAdditionType.setCategoryId(mpcaAdditionCategory);
                mpcaAdditionTypeListMpcaAdditionType = em.merge(mpcaAdditionTypeListMpcaAdditionType);
                if (oldCategoryIdOfMpcaAdditionTypeListMpcaAdditionType != null) {
                    oldCategoryIdOfMpcaAdditionTypeListMpcaAdditionType.getMpcaAdditionTypeList().remove(mpcaAdditionTypeListMpcaAdditionType);
                    oldCategoryIdOfMpcaAdditionTypeListMpcaAdditionType = em.merge(oldCategoryIdOfMpcaAdditionTypeListMpcaAdditionType);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaAdditionCategory(mpcaAdditionCategory.getCategoryId()) != null) {
                throw new PreexistingEntityException("MpcaAdditionCategory " + mpcaAdditionCategory + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaAdditionCategory mpcaAdditionCategory) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaAdditionCategory persistentMpcaAdditionCategory = em.find(MpcaAdditionCategory.class, mpcaAdditionCategory.getCategoryId());
            List<MpcaAdditionType> mpcaAdditionTypeListOld = persistentMpcaAdditionCategory.getMpcaAdditionTypeList();
            List<MpcaAdditionType> mpcaAdditionTypeListNew = mpcaAdditionCategory.getMpcaAdditionTypeList();
            List<MpcaAdditionType> attachedMpcaAdditionTypeListNew = new ArrayList<MpcaAdditionType>();
            for (MpcaAdditionType mpcaAdditionTypeListNewMpcaAdditionTypeToAttach : mpcaAdditionTypeListNew) {
                mpcaAdditionTypeListNewMpcaAdditionTypeToAttach = em.getReference(mpcaAdditionTypeListNewMpcaAdditionTypeToAttach.getClass(), mpcaAdditionTypeListNewMpcaAdditionTypeToAttach.getAddId());
                attachedMpcaAdditionTypeListNew.add(mpcaAdditionTypeListNewMpcaAdditionTypeToAttach);
            }
            mpcaAdditionTypeListNew = attachedMpcaAdditionTypeListNew;
            mpcaAdditionCategory.setMpcaAdditionTypeList(mpcaAdditionTypeListNew);
            mpcaAdditionCategory = em.merge(mpcaAdditionCategory);
            for (MpcaAdditionType mpcaAdditionTypeListOldMpcaAdditionType : mpcaAdditionTypeListOld) {
                if (!mpcaAdditionTypeListNew.contains(mpcaAdditionTypeListOldMpcaAdditionType)) {
                    mpcaAdditionTypeListOldMpcaAdditionType.setCategoryId(null);
                    mpcaAdditionTypeListOldMpcaAdditionType = em.merge(mpcaAdditionTypeListOldMpcaAdditionType);
                }
            }
            for (MpcaAdditionType mpcaAdditionTypeListNewMpcaAdditionType : mpcaAdditionTypeListNew) {
                if (!mpcaAdditionTypeListOld.contains(mpcaAdditionTypeListNewMpcaAdditionType)) {
                    MpcaAdditionCategory oldCategoryIdOfMpcaAdditionTypeListNewMpcaAdditionType = mpcaAdditionTypeListNewMpcaAdditionType.getCategoryId();
                    mpcaAdditionTypeListNewMpcaAdditionType.setCategoryId(mpcaAdditionCategory);
                    mpcaAdditionTypeListNewMpcaAdditionType = em.merge(mpcaAdditionTypeListNewMpcaAdditionType);
                    if (oldCategoryIdOfMpcaAdditionTypeListNewMpcaAdditionType != null && !oldCategoryIdOfMpcaAdditionTypeListNewMpcaAdditionType.equals(mpcaAdditionCategory)) {
                        oldCategoryIdOfMpcaAdditionTypeListNewMpcaAdditionType.getMpcaAdditionTypeList().remove(mpcaAdditionTypeListNewMpcaAdditionType);
                        oldCategoryIdOfMpcaAdditionTypeListNewMpcaAdditionType = em.merge(oldCategoryIdOfMpcaAdditionTypeListNewMpcaAdditionType);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaAdditionCategory.getCategoryId();
                if (findMpcaAdditionCategory(id) == null) {
                    throw new NonexistentEntityException("The mpcaAdditionCategory with id " + id + " no longer exists.");
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
            MpcaAdditionCategory mpcaAdditionCategory;
            try {
                mpcaAdditionCategory = em.getReference(MpcaAdditionCategory.class, id);
                mpcaAdditionCategory.getCategoryId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaAdditionCategory with id " + id + " no longer exists.", enfe);
            }
            List<MpcaAdditionType> mpcaAdditionTypeList = mpcaAdditionCategory.getMpcaAdditionTypeList();
            for (MpcaAdditionType mpcaAdditionTypeListMpcaAdditionType : mpcaAdditionTypeList) {
                mpcaAdditionTypeListMpcaAdditionType.setCategoryId(null);
                mpcaAdditionTypeListMpcaAdditionType = em.merge(mpcaAdditionTypeListMpcaAdditionType);
            }
            em.remove(mpcaAdditionCategory);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaAdditionCategory> findMpcaAdditionCategoryEntities() {
        return findMpcaAdditionCategoryEntities(true, -1, -1);
    }

    public List<MpcaAdditionCategory> findMpcaAdditionCategoryEntities(int maxResults, int firstResult) {
        return findMpcaAdditionCategoryEntities(false, maxResults, firstResult);
    }

    private List<MpcaAdditionCategory> findMpcaAdditionCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaAdditionCategory.class));
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

    public MpcaAdditionCategory findMpcaAdditionCategory(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaAdditionCategory.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaAdditionCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaAdditionCategory> rt = cq.from(MpcaAdditionCategory.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
