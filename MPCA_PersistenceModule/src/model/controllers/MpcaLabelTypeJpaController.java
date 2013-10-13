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
import model.entities.MpcaLabelType;
import java.util.ArrayList;
import java.util.List;
import model.entities.MpcaProductIndex;
import java.math.BigDecimal;
import javax.persistence.EntityManager;

/**
 *
 * @author SimonXPS
 */
public class MpcaLabelTypeJpaController extends JpaController implements Serializable {

    public void create(MpcaLabelType mpcaLabelType) throws PreexistingEntityException, Exception {
        if (mpcaLabelType.getMpcaCommentIndexList() == null) {
            mpcaLabelType.setMpcaCommentIndexList(new ArrayList<MpcaCommentIndex>());
        }
        if (mpcaLabelType.getMpcaProductIndexList() == null) {
            mpcaLabelType.setMpcaProductIndexList(new ArrayList<MpcaProductIndex>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaCommentIndex> attachedMpcaCommentIndexList = new ArrayList<MpcaCommentIndex>();
            for (MpcaCommentIndex mpcaCommentIndexListMpcaCommentIndexToAttach : mpcaLabelType.getMpcaCommentIndexList()) {
                mpcaCommentIndexListMpcaCommentIndexToAttach = em.getReference(mpcaCommentIndexListMpcaCommentIndexToAttach.getClass(), mpcaCommentIndexListMpcaCommentIndexToAttach.getCommentIndexId());
                attachedMpcaCommentIndexList.add(mpcaCommentIndexListMpcaCommentIndexToAttach);
            }
            mpcaLabelType.setMpcaCommentIndexList(attachedMpcaCommentIndexList);
            List<MpcaProductIndex> attachedMpcaProductIndexList = new ArrayList<MpcaProductIndex>();
            for (MpcaProductIndex mpcaProductIndexListMpcaProductIndexToAttach : mpcaLabelType.getMpcaProductIndexList()) {
                mpcaProductIndexListMpcaProductIndexToAttach = em.getReference(mpcaProductIndexListMpcaProductIndexToAttach.getClass(), mpcaProductIndexListMpcaProductIndexToAttach.getProductIndexId());
                attachedMpcaProductIndexList.add(mpcaProductIndexListMpcaProductIndexToAttach);
            }
            mpcaLabelType.setMpcaProductIndexList(attachedMpcaProductIndexList);
            em.persist(mpcaLabelType);
            for (MpcaCommentIndex mpcaCommentIndexListMpcaCommentIndex : mpcaLabelType.getMpcaCommentIndexList()) {
                MpcaLabelType oldLabelIdOfMpcaCommentIndexListMpcaCommentIndex = mpcaCommentIndexListMpcaCommentIndex.getLabelId();
                mpcaCommentIndexListMpcaCommentIndex.setLabelId(mpcaLabelType);
                mpcaCommentIndexListMpcaCommentIndex = em.merge(mpcaCommentIndexListMpcaCommentIndex);
                if (oldLabelIdOfMpcaCommentIndexListMpcaCommentIndex != null) {
                    oldLabelIdOfMpcaCommentIndexListMpcaCommentIndex.getMpcaCommentIndexList().remove(mpcaCommentIndexListMpcaCommentIndex);
                    oldLabelIdOfMpcaCommentIndexListMpcaCommentIndex = em.merge(oldLabelIdOfMpcaCommentIndexListMpcaCommentIndex);
                }
            }
            for (MpcaProductIndex mpcaProductIndexListMpcaProductIndex : mpcaLabelType.getMpcaProductIndexList()) {
                MpcaLabelType oldLabelIdOfMpcaProductIndexListMpcaProductIndex = mpcaProductIndexListMpcaProductIndex.getLabelId();
                mpcaProductIndexListMpcaProductIndex.setLabelId(mpcaLabelType);
                mpcaProductIndexListMpcaProductIndex = em.merge(mpcaProductIndexListMpcaProductIndex);
                if (oldLabelIdOfMpcaProductIndexListMpcaProductIndex != null) {
                    oldLabelIdOfMpcaProductIndexListMpcaProductIndex.getMpcaProductIndexList().remove(mpcaProductIndexListMpcaProductIndex);
                    oldLabelIdOfMpcaProductIndexListMpcaProductIndex = em.merge(oldLabelIdOfMpcaProductIndexListMpcaProductIndex);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaLabelType(mpcaLabelType.getLabelId()) != null) {
                throw new PreexistingEntityException("MpcaLabelType " + mpcaLabelType + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaLabelType mpcaLabelType) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaLabelType persistentMpcaLabelType = em.find(MpcaLabelType.class, mpcaLabelType.getLabelId());
            List<MpcaCommentIndex> mpcaCommentIndexListOld = persistentMpcaLabelType.getMpcaCommentIndexList();
            List<MpcaCommentIndex> mpcaCommentIndexListNew = mpcaLabelType.getMpcaCommentIndexList();
            List<MpcaProductIndex> mpcaProductIndexListOld = persistentMpcaLabelType.getMpcaProductIndexList();
            List<MpcaProductIndex> mpcaProductIndexListNew = mpcaLabelType.getMpcaProductIndexList();
            List<String> illegalOrphanMessages = null;
            for (MpcaCommentIndex mpcaCommentIndexListOldMpcaCommentIndex : mpcaCommentIndexListOld) {
                if (!mpcaCommentIndexListNew.contains(mpcaCommentIndexListOldMpcaCommentIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaCommentIndex " + mpcaCommentIndexListOldMpcaCommentIndex + " since its labelId field is not nullable.");
                }
            }
            for (MpcaProductIndex mpcaProductIndexListOldMpcaProductIndex : mpcaProductIndexListOld) {
                if (!mpcaProductIndexListNew.contains(mpcaProductIndexListOldMpcaProductIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductIndex " + mpcaProductIndexListOldMpcaProductIndex + " since its labelId field is not nullable.");
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
            mpcaLabelType.setMpcaCommentIndexList(mpcaCommentIndexListNew);
            List<MpcaProductIndex> attachedMpcaProductIndexListNew = new ArrayList<MpcaProductIndex>();
            for (MpcaProductIndex mpcaProductIndexListNewMpcaProductIndexToAttach : mpcaProductIndexListNew) {
                mpcaProductIndexListNewMpcaProductIndexToAttach = em.getReference(mpcaProductIndexListNewMpcaProductIndexToAttach.getClass(), mpcaProductIndexListNewMpcaProductIndexToAttach.getProductIndexId());
                attachedMpcaProductIndexListNew.add(mpcaProductIndexListNewMpcaProductIndexToAttach);
            }
            mpcaProductIndexListNew = attachedMpcaProductIndexListNew;
            mpcaLabelType.setMpcaProductIndexList(mpcaProductIndexListNew);
            mpcaLabelType = em.merge(mpcaLabelType);
            for (MpcaCommentIndex mpcaCommentIndexListNewMpcaCommentIndex : mpcaCommentIndexListNew) {
                if (!mpcaCommentIndexListOld.contains(mpcaCommentIndexListNewMpcaCommentIndex)) {
                    MpcaLabelType oldLabelIdOfMpcaCommentIndexListNewMpcaCommentIndex = mpcaCommentIndexListNewMpcaCommentIndex.getLabelId();
                    mpcaCommentIndexListNewMpcaCommentIndex.setLabelId(mpcaLabelType);
                    mpcaCommentIndexListNewMpcaCommentIndex = em.merge(mpcaCommentIndexListNewMpcaCommentIndex);
                    if (oldLabelIdOfMpcaCommentIndexListNewMpcaCommentIndex != null && !oldLabelIdOfMpcaCommentIndexListNewMpcaCommentIndex.equals(mpcaLabelType)) {
                        oldLabelIdOfMpcaCommentIndexListNewMpcaCommentIndex.getMpcaCommentIndexList().remove(mpcaCommentIndexListNewMpcaCommentIndex);
                        oldLabelIdOfMpcaCommentIndexListNewMpcaCommentIndex = em.merge(oldLabelIdOfMpcaCommentIndexListNewMpcaCommentIndex);
                    }
                }
            }
            for (MpcaProductIndex mpcaProductIndexListNewMpcaProductIndex : mpcaProductIndexListNew) {
                if (!mpcaProductIndexListOld.contains(mpcaProductIndexListNewMpcaProductIndex)) {
                    MpcaLabelType oldLabelIdOfMpcaProductIndexListNewMpcaProductIndex = mpcaProductIndexListNewMpcaProductIndex.getLabelId();
                    mpcaProductIndexListNewMpcaProductIndex.setLabelId(mpcaLabelType);
                    mpcaProductIndexListNewMpcaProductIndex = em.merge(mpcaProductIndexListNewMpcaProductIndex);
                    if (oldLabelIdOfMpcaProductIndexListNewMpcaProductIndex != null && !oldLabelIdOfMpcaProductIndexListNewMpcaProductIndex.equals(mpcaLabelType)) {
                        oldLabelIdOfMpcaProductIndexListNewMpcaProductIndex.getMpcaProductIndexList().remove(mpcaProductIndexListNewMpcaProductIndex);
                        oldLabelIdOfMpcaProductIndexListNewMpcaProductIndex = em.merge(oldLabelIdOfMpcaProductIndexListNewMpcaProductIndex);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = mpcaLabelType.getLabelId();
                if (findMpcaLabelType(id) == null) {
                    throw new NonexistentEntityException("The mpcaLabelType with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaLabelType mpcaLabelType;
            try {
                mpcaLabelType = em.getReference(MpcaLabelType.class, id);
                mpcaLabelType.getLabelId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaLabelType with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaCommentIndex> mpcaCommentIndexListOrphanCheck = mpcaLabelType.getMpcaCommentIndexList();
            for (MpcaCommentIndex mpcaCommentIndexListOrphanCheckMpcaCommentIndex : mpcaCommentIndexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaLabelType (" + mpcaLabelType + ") cannot be destroyed since the MpcaCommentIndex " + mpcaCommentIndexListOrphanCheckMpcaCommentIndex + " in its mpcaCommentIndexList field has a non-nullable labelId field.");
            }
            List<MpcaProductIndex> mpcaProductIndexListOrphanCheck = mpcaLabelType.getMpcaProductIndexList();
            for (MpcaProductIndex mpcaProductIndexListOrphanCheckMpcaProductIndex : mpcaProductIndexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaLabelType (" + mpcaLabelType + ") cannot be destroyed since the MpcaProductIndex " + mpcaProductIndexListOrphanCheckMpcaProductIndex + " in its mpcaProductIndexList field has a non-nullable labelId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mpcaLabelType);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaLabelType> findMpcaLabelTypeEntities() {
        return findMpcaLabelTypeEntities(true, -1, -1);
    }

    public List<MpcaLabelType> findMpcaLabelTypeEntities(int maxResults, int firstResult) {
        return findMpcaLabelTypeEntities(false, maxResults, firstResult);
    }

    private List<MpcaLabelType> findMpcaLabelTypeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaLabelType.class));
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

    public MpcaLabelType findMpcaLabelType(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaLabelType.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaLabelTypeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaLabelType> rt = cq.from(MpcaLabelType.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public MpcaLabelType findMpcaLabelTypeByName(String labelName) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaLabelType.findByLabelName");
            q.setParameter("labelName", labelName);
            MpcaLabelType labelType = null;
            List<MpcaLabelType> labels = q.getResultList();
            if (!labels.isEmpty()) {
                labelType = labels.get(0);
            }
            return labelType;
        } finally {
            em.close();
        }
    }
}
