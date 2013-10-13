/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.controllers;

import model.controllers.exceptions.IllegalOrphanException;
import model.controllers.exceptions.NonexistentEntityException;
import model.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.entities.MpcaCommentIndex;
import model.entities.MpcaIndexType;
import java.util.ArrayList;
import java.util.List;
import model.entities.MpcaProductIndex;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author SimonXPS
 */
public class MpcaIndexTypeJpaController extends JpaController implements Serializable {

    public void create(MpcaIndexType mpcaIndexType) throws PreexistingEntityException, Exception {
        if (mpcaIndexType.getMpcaCommentIndexList() == null) {
            mpcaIndexType.setMpcaCommentIndexList(new ArrayList<MpcaCommentIndex>());
        }
        if (mpcaIndexType.getMpcaProductIndexList() == null) {
            mpcaIndexType.setMpcaProductIndexList(new ArrayList<MpcaProductIndex>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaCommentIndex> attachedMpcaCommentIndexList = new ArrayList<MpcaCommentIndex>();
            for (MpcaCommentIndex mpcaCommentIndexListMpcaCommentIndexToAttach : mpcaIndexType.getMpcaCommentIndexList()) {
                mpcaCommentIndexListMpcaCommentIndexToAttach = em.getReference(mpcaCommentIndexListMpcaCommentIndexToAttach.getClass(), mpcaCommentIndexListMpcaCommentIndexToAttach.getCommentIndexId());
                attachedMpcaCommentIndexList.add(mpcaCommentIndexListMpcaCommentIndexToAttach);
            }
            mpcaIndexType.setMpcaCommentIndexList(attachedMpcaCommentIndexList);
            List<MpcaProductIndex> attachedMpcaProductIndexList = new ArrayList<MpcaProductIndex>();
            for (MpcaProductIndex mpcaProductIndexListMpcaProductIndexToAttach : mpcaIndexType.getMpcaProductIndexList()) {
                mpcaProductIndexListMpcaProductIndexToAttach = em.getReference(mpcaProductIndexListMpcaProductIndexToAttach.getClass(), mpcaProductIndexListMpcaProductIndexToAttach.getProductIndexId());
                attachedMpcaProductIndexList.add(mpcaProductIndexListMpcaProductIndexToAttach);
            }
            mpcaIndexType.setMpcaProductIndexList(attachedMpcaProductIndexList);
            em.persist(mpcaIndexType);
            for (MpcaCommentIndex mpcaCommentIndexListMpcaCommentIndex : mpcaIndexType.getMpcaCommentIndexList()) {
                MpcaIndexType oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListMpcaCommentIndex = mpcaCommentIndexListMpcaCommentIndex.getMpcaIndexTypeIndexId();
                mpcaCommentIndexListMpcaCommentIndex.setMpcaIndexTypeIndexId(mpcaIndexType);
                mpcaCommentIndexListMpcaCommentIndex = em.merge(mpcaCommentIndexListMpcaCommentIndex);
                if (oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListMpcaCommentIndex != null) {
                    oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListMpcaCommentIndex.getMpcaCommentIndexList().remove(mpcaCommentIndexListMpcaCommentIndex);
                    oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListMpcaCommentIndex = em.merge(oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListMpcaCommentIndex);
                }
            }
            for (MpcaProductIndex mpcaProductIndexListMpcaProductIndex : mpcaIndexType.getMpcaProductIndexList()) {
                MpcaIndexType oldMpcaIndexTypeIndexIdOfMpcaProductIndexListMpcaProductIndex = mpcaProductIndexListMpcaProductIndex.getMpcaIndexTypeIndexId();
                mpcaProductIndexListMpcaProductIndex.setMpcaIndexTypeIndexId(mpcaIndexType);
                mpcaProductIndexListMpcaProductIndex = em.merge(mpcaProductIndexListMpcaProductIndex);
                if (oldMpcaIndexTypeIndexIdOfMpcaProductIndexListMpcaProductIndex != null) {
                    oldMpcaIndexTypeIndexIdOfMpcaProductIndexListMpcaProductIndex.getMpcaProductIndexList().remove(mpcaProductIndexListMpcaProductIndex);
                    oldMpcaIndexTypeIndexIdOfMpcaProductIndexListMpcaProductIndex = em.merge(oldMpcaIndexTypeIndexIdOfMpcaProductIndexListMpcaProductIndex);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaIndexType(mpcaIndexType.getIndexId()) != null) {
                throw new PreexistingEntityException("MpcaIndexType " + mpcaIndexType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaIndexType mpcaIndexType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaIndexType persistentMpcaIndexType = em.find(MpcaIndexType.class, mpcaIndexType.getIndexId());
            List<MpcaCommentIndex> mpcaCommentIndexListOld = persistentMpcaIndexType.getMpcaCommentIndexList();
            List<MpcaCommentIndex> mpcaCommentIndexListNew = mpcaIndexType.getMpcaCommentIndexList();
            List<MpcaProductIndex> mpcaProductIndexListOld = persistentMpcaIndexType.getMpcaProductIndexList();
            List<MpcaProductIndex> mpcaProductIndexListNew = mpcaIndexType.getMpcaProductIndexList();
            List<String> illegalOrphanMessages = null;
            for (MpcaCommentIndex mpcaCommentIndexListOldMpcaCommentIndex : mpcaCommentIndexListOld) {
                if (!mpcaCommentIndexListNew.contains(mpcaCommentIndexListOldMpcaCommentIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaCommentIndex " + mpcaCommentIndexListOldMpcaCommentIndex + " since its mpcaIndexTypeIndexId field is not nullable.");
                }
            }
            for (MpcaProductIndex mpcaProductIndexListOldMpcaProductIndex : mpcaProductIndexListOld) {
                if (!mpcaProductIndexListNew.contains(mpcaProductIndexListOldMpcaProductIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductIndex " + mpcaProductIndexListOldMpcaProductIndex + " since its mpcaIndexTypeIndexId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaCommentIndex> attachedMpcaCommentIndexListNew = new ArrayList<MpcaCommentIndex>();
            for (MpcaCommentIndex mpcaCommentIndexListNewMpcaCommentIndexToAttach : mpcaCommentIndexListNew) {
                mpcaCommentIndexListNewMpcaCommentIndexToAttach = em.getReference(mpcaCommentIndexListNewMpcaCommentIndexToAttach.getClass(), mpcaCommentIndexListNewMpcaCommentIndexToAttach.getCommentIndexId());
                attachedMpcaCommentIndexListNew.add(mpcaCommentIndexListNewMpcaCommentIndexToAttach);
            }
            mpcaCommentIndexListNew = attachedMpcaCommentIndexListNew;
            mpcaIndexType.setMpcaCommentIndexList(mpcaCommentIndexListNew);
            List<MpcaProductIndex> attachedMpcaProductIndexListNew = new ArrayList<MpcaProductIndex>();
            for (MpcaProductIndex mpcaProductIndexListNewMpcaProductIndexToAttach : mpcaProductIndexListNew) {
                mpcaProductIndexListNewMpcaProductIndexToAttach = em.getReference(mpcaProductIndexListNewMpcaProductIndexToAttach.getClass(), mpcaProductIndexListNewMpcaProductIndexToAttach.getProductIndexId());
                attachedMpcaProductIndexListNew.add(mpcaProductIndexListNewMpcaProductIndexToAttach);
            }
            mpcaProductIndexListNew = attachedMpcaProductIndexListNew;
            mpcaIndexType.setMpcaProductIndexList(mpcaProductIndexListNew);
            mpcaIndexType = em.merge(mpcaIndexType);
            for (MpcaCommentIndex mpcaCommentIndexListNewMpcaCommentIndex : mpcaCommentIndexListNew) {
                if (!mpcaCommentIndexListOld.contains(mpcaCommentIndexListNewMpcaCommentIndex)) {
                    MpcaIndexType oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListNewMpcaCommentIndex = mpcaCommentIndexListNewMpcaCommentIndex.getMpcaIndexTypeIndexId();
                    mpcaCommentIndexListNewMpcaCommentIndex.setMpcaIndexTypeIndexId(mpcaIndexType);
                    mpcaCommentIndexListNewMpcaCommentIndex = em.merge(mpcaCommentIndexListNewMpcaCommentIndex);
                    if (oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListNewMpcaCommentIndex != null && !oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListNewMpcaCommentIndex.equals(mpcaIndexType)) {
                        oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListNewMpcaCommentIndex.getMpcaCommentIndexList().remove(mpcaCommentIndexListNewMpcaCommentIndex);
                        oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListNewMpcaCommentIndex = em.merge(oldMpcaIndexTypeIndexIdOfMpcaCommentIndexListNewMpcaCommentIndex);
                    }
                }
            }
            for (MpcaProductIndex mpcaProductIndexListNewMpcaProductIndex : mpcaProductIndexListNew) {
                if (!mpcaProductIndexListOld.contains(mpcaProductIndexListNewMpcaProductIndex)) {
                    MpcaIndexType oldMpcaIndexTypeIndexIdOfMpcaProductIndexListNewMpcaProductIndex = mpcaProductIndexListNewMpcaProductIndex.getMpcaIndexTypeIndexId();
                    mpcaProductIndexListNewMpcaProductIndex.setMpcaIndexTypeIndexId(mpcaIndexType);
                    mpcaProductIndexListNewMpcaProductIndex = em.merge(mpcaProductIndexListNewMpcaProductIndex);
                    if (oldMpcaIndexTypeIndexIdOfMpcaProductIndexListNewMpcaProductIndex != null && !oldMpcaIndexTypeIndexIdOfMpcaProductIndexListNewMpcaProductIndex.equals(mpcaIndexType)) {
                        oldMpcaIndexTypeIndexIdOfMpcaProductIndexListNewMpcaProductIndex.getMpcaProductIndexList().remove(mpcaProductIndexListNewMpcaProductIndex);
                        oldMpcaIndexTypeIndexIdOfMpcaProductIndexListNewMpcaProductIndex = em.merge(oldMpcaIndexTypeIndexIdOfMpcaProductIndexListNewMpcaProductIndex);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaIndexType.getIndexId();
                if (findMpcaIndexType(id) == null) {
                    throw new NonexistentEntityException("The mpcaIndexType with id " + id + " no longer exists.");
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
            MpcaIndexType mpcaIndexType;
            try {
                mpcaIndexType = em.getReference(MpcaIndexType.class, id);
                mpcaIndexType.getIndexId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaIndexType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaCommentIndex> mpcaCommentIndexListOrphanCheck = mpcaIndexType.getMpcaCommentIndexList();
            for (MpcaCommentIndex mpcaCommentIndexListOrphanCheckMpcaCommentIndex : mpcaCommentIndexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaIndexType (" + mpcaIndexType + ") cannot be destroyed since the MpcaCommentIndex " + mpcaCommentIndexListOrphanCheckMpcaCommentIndex + " in its mpcaCommentIndexList field has a non-nullable mpcaIndexTypeIndexId field.");
            }
            List<MpcaProductIndex> mpcaProductIndexListOrphanCheck = mpcaIndexType.getMpcaProductIndexList();
            for (MpcaProductIndex mpcaProductIndexListOrphanCheckMpcaProductIndex : mpcaProductIndexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaIndexType (" + mpcaIndexType + ") cannot be destroyed since the MpcaProductIndex " + mpcaProductIndexListOrphanCheckMpcaProductIndex + " in its mpcaProductIndexList field has a non-nullable mpcaIndexTypeIndexId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mpcaIndexType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaIndexType> findMpcaIndexTypeEntities() {
        return findMpcaIndexTypeEntities(true, -1, -1);
    }

    public List<MpcaIndexType> findMpcaIndexTypeEntities(int maxResults, int firstResult) {
        return findMpcaIndexTypeEntities(false, maxResults, firstResult);
    }

    private List<MpcaIndexType> findMpcaIndexTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaIndexType.class));
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

    public MpcaIndexType findMpcaIndexType(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaIndexType.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaIndexTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaIndexType> rt = cq.from(MpcaIndexType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public MpcaIndexType findMpcaIndexTypeByName(String indexName) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaIndexType.findByIndexName");
            q.setParameter("indexName", indexName);
            MpcaIndexType indexType = null;
            List<MpcaIndexType> indexs = q.getResultList();
            if (!indexs.isEmpty()) {
                indexType = indexs.get(0);
            }
            return indexType;
        } finally {
            em.close();
        }
    }
}
