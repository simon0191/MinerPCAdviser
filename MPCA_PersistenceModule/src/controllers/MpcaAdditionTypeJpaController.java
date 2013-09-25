/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.MpcaAdditionCategory;
import entities.MpcaAdditionType;
import entities.MpcaProductAddition;
import java.util.ArrayList;
import java.util.List;
import entities.MpcaCommentAddition;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaAdditionTypeJpaController extends JpaController implements Serializable {

    public void create(MpcaAdditionType mpcaAdditionType) throws PreexistingEntityException, Exception {
        if (mpcaAdditionType.getMpcaProductAdditionList() == null) {
            mpcaAdditionType.setMpcaProductAdditionList(new ArrayList<MpcaProductAddition>());
        }
        if (mpcaAdditionType.getMpcaCommentAdditionList() == null) {
            mpcaAdditionType.setMpcaCommentAdditionList(new ArrayList<MpcaCommentAddition>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
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
            List<MpcaCommentAddition> attachedMpcaCommentAdditionList = new ArrayList<MpcaCommentAddition>();
            for (MpcaCommentAddition mpcaCommentAdditionListMpcaCommentAdditionToAttach : mpcaAdditionType.getMpcaCommentAdditionList()) {
                mpcaCommentAdditionListMpcaCommentAdditionToAttach = em.getReference(mpcaCommentAdditionListMpcaCommentAdditionToAttach.getClass(), mpcaCommentAdditionListMpcaCommentAdditionToAttach.getMpcaCommentAdditionPK());
                attachedMpcaCommentAdditionList.add(mpcaCommentAdditionListMpcaCommentAdditionToAttach);
            }
            mpcaAdditionType.setMpcaCommentAdditionList(attachedMpcaCommentAdditionList);
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
            for (MpcaCommentAddition mpcaCommentAdditionListMpcaCommentAddition : mpcaAdditionType.getMpcaCommentAdditionList()) {
                MpcaAdditionType oldMpcaAdditionTypeOfMpcaCommentAdditionListMpcaCommentAddition = mpcaCommentAdditionListMpcaCommentAddition.getMpcaAdditionType();
                mpcaCommentAdditionListMpcaCommentAddition.setMpcaAdditionType(mpcaAdditionType);
                mpcaCommentAdditionListMpcaCommentAddition = em.merge(mpcaCommentAdditionListMpcaCommentAddition);
                if (oldMpcaAdditionTypeOfMpcaCommentAdditionListMpcaCommentAddition != null) {
                    oldMpcaAdditionTypeOfMpcaCommentAdditionListMpcaCommentAddition.getMpcaCommentAdditionList().remove(mpcaCommentAdditionListMpcaCommentAddition);
                    oldMpcaAdditionTypeOfMpcaCommentAdditionListMpcaCommentAddition = em.merge(oldMpcaAdditionTypeOfMpcaCommentAdditionListMpcaCommentAddition);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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

    public void edit(MpcaAdditionType mpcaAdditionType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaAdditionType persistentMpcaAdditionType = em.find(MpcaAdditionType.class, mpcaAdditionType.getAddId());
            MpcaAdditionCategory categoryIdOld = persistentMpcaAdditionType.getCategoryId();
            MpcaAdditionCategory categoryIdNew = mpcaAdditionType.getCategoryId();
            List<MpcaProductAddition> mpcaProductAdditionListOld = persistentMpcaAdditionType.getMpcaProductAdditionList();
            List<MpcaProductAddition> mpcaProductAdditionListNew = mpcaAdditionType.getMpcaProductAdditionList();
            List<MpcaCommentAddition> mpcaCommentAdditionListOld = persistentMpcaAdditionType.getMpcaCommentAdditionList();
            List<MpcaCommentAddition> mpcaCommentAdditionListNew = mpcaAdditionType.getMpcaCommentAdditionList();
            List<String> illegalOrphanMessages = null;
            for (MpcaProductAddition mpcaProductAdditionListOldMpcaProductAddition : mpcaProductAdditionListOld) {
                if (!mpcaProductAdditionListNew.contains(mpcaProductAdditionListOldMpcaProductAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductAddition " + mpcaProductAdditionListOldMpcaProductAddition + " since its mpcaAdditionType field is not nullable.");
                }
            }
            for (MpcaCommentAddition mpcaCommentAdditionListOldMpcaCommentAddition : mpcaCommentAdditionListOld) {
                if (!mpcaCommentAdditionListNew.contains(mpcaCommentAdditionListOldMpcaCommentAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaCommentAddition " + mpcaCommentAdditionListOldMpcaCommentAddition + " since its mpcaAdditionType field is not nullable.");
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
            List<MpcaCommentAddition> attachedMpcaCommentAdditionListNew = new ArrayList<MpcaCommentAddition>();
            for (MpcaCommentAddition mpcaCommentAdditionListNewMpcaCommentAdditionToAttach : mpcaCommentAdditionListNew) {
                mpcaCommentAdditionListNewMpcaCommentAdditionToAttach = em.getReference(mpcaCommentAdditionListNewMpcaCommentAdditionToAttach.getClass(), mpcaCommentAdditionListNewMpcaCommentAdditionToAttach.getMpcaCommentAdditionPK());
                attachedMpcaCommentAdditionListNew.add(mpcaCommentAdditionListNewMpcaCommentAdditionToAttach);
            }
            mpcaCommentAdditionListNew = attachedMpcaCommentAdditionListNew;
            mpcaAdditionType.setMpcaCommentAdditionList(mpcaCommentAdditionListNew);
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
            for (MpcaCommentAddition mpcaCommentAdditionListNewMpcaCommentAddition : mpcaCommentAdditionListNew) {
                if (!mpcaCommentAdditionListOld.contains(mpcaCommentAdditionListNewMpcaCommentAddition)) {
                    MpcaAdditionType oldMpcaAdditionTypeOfMpcaCommentAdditionListNewMpcaCommentAddition = mpcaCommentAdditionListNewMpcaCommentAddition.getMpcaAdditionType();
                    mpcaCommentAdditionListNewMpcaCommentAddition.setMpcaAdditionType(mpcaAdditionType);
                    mpcaCommentAdditionListNewMpcaCommentAddition = em.merge(mpcaCommentAdditionListNewMpcaCommentAddition);
                    if (oldMpcaAdditionTypeOfMpcaCommentAdditionListNewMpcaCommentAddition != null && !oldMpcaAdditionTypeOfMpcaCommentAdditionListNewMpcaCommentAddition.equals(mpcaAdditionType)) {
                        oldMpcaAdditionTypeOfMpcaCommentAdditionListNewMpcaCommentAddition.getMpcaCommentAdditionList().remove(mpcaCommentAdditionListNewMpcaCommentAddition);
                        oldMpcaAdditionTypeOfMpcaCommentAdditionListNewMpcaCommentAddition = em.merge(oldMpcaAdditionTypeOfMpcaCommentAdditionListNewMpcaCommentAddition);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
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
            List<MpcaCommentAddition> mpcaCommentAdditionListOrphanCheck = mpcaAdditionType.getMpcaCommentAdditionList();
            for (MpcaCommentAddition mpcaCommentAdditionListOrphanCheckMpcaCommentAddition : mpcaCommentAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaAdditionType (" + mpcaAdditionType + ") cannot be destroyed since the MpcaCommentAddition " + mpcaCommentAdditionListOrphanCheckMpcaCommentAddition + " in its mpcaCommentAdditionList field has a non-nullable mpcaAdditionType field.");
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
            em.getTransaction().commit();
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

    public MpcaAdditionType findAdditionByType(String type) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaAdditionType.findByAddType");
            q.setParameter("addType", type);
            List<MpcaAdditionType> resultList = q.getResultList();
            MpcaAdditionType add = null;
            if(!resultList.isEmpty()) {
                add = resultList.get(0);
            }
            return add;
        } finally {
            em.close();
        }
    }
    
}
