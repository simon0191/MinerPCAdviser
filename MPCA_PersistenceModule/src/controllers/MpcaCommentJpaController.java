/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.MpcaComment;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.MpcaWebPage;
import entities.MpcaProduct;
import entities.MpcaCommentIndex;
import java.util.ArrayList;
import java.util.List;
import entities.MpcaCommentAddition;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaCommentJpaController extends JpaController implements Serializable {

    public void create(MpcaComment mpcaComment) throws PreexistingEntityException, Exception {
        if (mpcaComment.getMpcaCommentIndexList() == null) {
            mpcaComment.setMpcaCommentIndexList(new ArrayList<MpcaCommentIndex>());
        }
        if (mpcaComment.getMpcaCommentAdditionList() == null) {
            mpcaComment.setMpcaCommentAdditionList(new ArrayList<MpcaCommentAddition>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaWebPage pageId = mpcaComment.getPageId();
            if (pageId != null) {
                pageId = em.getReference(pageId.getClass(), pageId.getPageId());
                mpcaComment.setPageId(pageId);
            }
            MpcaProduct productId = mpcaComment.getProductId();
            if (productId != null) {
                productId = em.getReference(productId.getClass(), productId.getProductId());
                mpcaComment.setProductId(productId);
            }
            List<MpcaCommentIndex> attachedMpcaCommentIndexList = new ArrayList<MpcaCommentIndex>();
            for (MpcaCommentIndex mpcaCommentIndexListMpcaCommentIndexToAttach : mpcaComment.getMpcaCommentIndexList()) {
                mpcaCommentIndexListMpcaCommentIndexToAttach = em.getReference(mpcaCommentIndexListMpcaCommentIndexToAttach.getClass(), mpcaCommentIndexListMpcaCommentIndexToAttach.getMpcaCommentIndexPK());
                attachedMpcaCommentIndexList.add(mpcaCommentIndexListMpcaCommentIndexToAttach);
            }
            mpcaComment.setMpcaCommentIndexList(attachedMpcaCommentIndexList);
            List<MpcaCommentAddition> attachedMpcaCommentAdditionList = new ArrayList<MpcaCommentAddition>();
            for (MpcaCommentAddition mpcaCommentAdditionListMpcaCommentAdditionToAttach : mpcaComment.getMpcaCommentAdditionList()) {
                mpcaCommentAdditionListMpcaCommentAdditionToAttach = em.getReference(mpcaCommentAdditionListMpcaCommentAdditionToAttach.getClass(), mpcaCommentAdditionListMpcaCommentAdditionToAttach.getMpcaCommentAdditionPK());
                attachedMpcaCommentAdditionList.add(mpcaCommentAdditionListMpcaCommentAdditionToAttach);
            }
            mpcaComment.setMpcaCommentAdditionList(attachedMpcaCommentAdditionList);
            em.persist(mpcaComment);
            if (pageId != null) {
                pageId.getMpcaCommentList().add(mpcaComment);
                pageId = em.merge(pageId);
            }
            if (productId != null) {
                productId.getMpcaCommentList().add(mpcaComment);
                productId = em.merge(productId);
            }
            for (MpcaCommentIndex mpcaCommentIndexListMpcaCommentIndex : mpcaComment.getMpcaCommentIndexList()) {
                MpcaComment oldMpcaCommentOfMpcaCommentIndexListMpcaCommentIndex = mpcaCommentIndexListMpcaCommentIndex.getMpcaComment();
                mpcaCommentIndexListMpcaCommentIndex.setMpcaComment(mpcaComment);
                mpcaCommentIndexListMpcaCommentIndex = em.merge(mpcaCommentIndexListMpcaCommentIndex);
                if (oldMpcaCommentOfMpcaCommentIndexListMpcaCommentIndex != null) {
                    oldMpcaCommentOfMpcaCommentIndexListMpcaCommentIndex.getMpcaCommentIndexList().remove(mpcaCommentIndexListMpcaCommentIndex);
                    oldMpcaCommentOfMpcaCommentIndexListMpcaCommentIndex = em.merge(oldMpcaCommentOfMpcaCommentIndexListMpcaCommentIndex);
                }
            }
            for (MpcaCommentAddition mpcaCommentAdditionListMpcaCommentAddition : mpcaComment.getMpcaCommentAdditionList()) {
                MpcaComment oldMpcaCommentOfMpcaCommentAdditionListMpcaCommentAddition = mpcaCommentAdditionListMpcaCommentAddition.getMpcaComment();
                mpcaCommentAdditionListMpcaCommentAddition.setMpcaComment(mpcaComment);
                mpcaCommentAdditionListMpcaCommentAddition = em.merge(mpcaCommentAdditionListMpcaCommentAddition);
                if (oldMpcaCommentOfMpcaCommentAdditionListMpcaCommentAddition != null) {
                    oldMpcaCommentOfMpcaCommentAdditionListMpcaCommentAddition.getMpcaCommentAdditionList().remove(mpcaCommentAdditionListMpcaCommentAddition);
                    oldMpcaCommentOfMpcaCommentAdditionListMpcaCommentAddition = em.merge(oldMpcaCommentOfMpcaCommentAdditionListMpcaCommentAddition);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaComment(mpcaComment.getCommentId()) != null) {
                throw new PreexistingEntityException("MpcaComment " + mpcaComment + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaComment mpcaComment) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaComment persistentMpcaComment = em.find(MpcaComment.class, mpcaComment.getCommentId());
            MpcaWebPage pageIdOld = persistentMpcaComment.getPageId();
            MpcaWebPage pageIdNew = mpcaComment.getPageId();
            MpcaProduct productIdOld = persistentMpcaComment.getProductId();
            MpcaProduct productIdNew = mpcaComment.getProductId();
            List<MpcaCommentIndex> mpcaCommentIndexListOld = persistentMpcaComment.getMpcaCommentIndexList();
            List<MpcaCommentIndex> mpcaCommentIndexListNew = mpcaComment.getMpcaCommentIndexList();
            List<MpcaCommentAddition> mpcaCommentAdditionListOld = persistentMpcaComment.getMpcaCommentAdditionList();
            List<MpcaCommentAddition> mpcaCommentAdditionListNew = mpcaComment.getMpcaCommentAdditionList();
            List<String> illegalOrphanMessages = null;
            for (MpcaCommentIndex mpcaCommentIndexListOldMpcaCommentIndex : mpcaCommentIndexListOld) {
                if (!mpcaCommentIndexListNew.contains(mpcaCommentIndexListOldMpcaCommentIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaCommentIndex " + mpcaCommentIndexListOldMpcaCommentIndex + " since its mpcaComment field is not nullable.");
                }
            }
            for (MpcaCommentAddition mpcaCommentAdditionListOldMpcaCommentAddition : mpcaCommentAdditionListOld) {
                if (!mpcaCommentAdditionListNew.contains(mpcaCommentAdditionListOldMpcaCommentAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaCommentAddition " + mpcaCommentAdditionListOldMpcaCommentAddition + " since its mpcaComment field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pageIdNew != null) {
                pageIdNew = em.getReference(pageIdNew.getClass(), pageIdNew.getPageId());
                mpcaComment.setPageId(pageIdNew);
            }
            if (productIdNew != null) {
                productIdNew = em.getReference(productIdNew.getClass(), productIdNew.getProductId());
                mpcaComment.setProductId(productIdNew);
            }
            List<MpcaCommentIndex> attachedMpcaCommentIndexListNew = new ArrayList<MpcaCommentIndex>();
            for (MpcaCommentIndex mpcaCommentIndexListNewMpcaCommentIndexToAttach : mpcaCommentIndexListNew) {
                mpcaCommentIndexListNewMpcaCommentIndexToAttach = em.getReference(mpcaCommentIndexListNewMpcaCommentIndexToAttach.getClass(), mpcaCommentIndexListNewMpcaCommentIndexToAttach.getMpcaCommentIndexPK());
                attachedMpcaCommentIndexListNew.add(mpcaCommentIndexListNewMpcaCommentIndexToAttach);
            }
            mpcaCommentIndexListNew = attachedMpcaCommentIndexListNew;
            mpcaComment.setMpcaCommentIndexList(mpcaCommentIndexListNew);
            List<MpcaCommentAddition> attachedMpcaCommentAdditionListNew = new ArrayList<MpcaCommentAddition>();
            for (MpcaCommentAddition mpcaCommentAdditionListNewMpcaCommentAdditionToAttach : mpcaCommentAdditionListNew) {
                mpcaCommentAdditionListNewMpcaCommentAdditionToAttach = em.getReference(mpcaCommentAdditionListNewMpcaCommentAdditionToAttach.getClass(), mpcaCommentAdditionListNewMpcaCommentAdditionToAttach.getMpcaCommentAdditionPK());
                attachedMpcaCommentAdditionListNew.add(mpcaCommentAdditionListNewMpcaCommentAdditionToAttach);
            }
            mpcaCommentAdditionListNew = attachedMpcaCommentAdditionListNew;
            mpcaComment.setMpcaCommentAdditionList(mpcaCommentAdditionListNew);
            mpcaComment = em.merge(mpcaComment);
            if (pageIdOld != null && !pageIdOld.equals(pageIdNew)) {
                pageIdOld.getMpcaCommentList().remove(mpcaComment);
                pageIdOld = em.merge(pageIdOld);
            }
            if (pageIdNew != null && !pageIdNew.equals(pageIdOld)) {
                pageIdNew.getMpcaCommentList().add(mpcaComment);
                pageIdNew = em.merge(pageIdNew);
            }
            if (productIdOld != null && !productIdOld.equals(productIdNew)) {
                productIdOld.getMpcaCommentList().remove(mpcaComment);
                productIdOld = em.merge(productIdOld);
            }
            if (productIdNew != null && !productIdNew.equals(productIdOld)) {
                productIdNew.getMpcaCommentList().add(mpcaComment);
                productIdNew = em.merge(productIdNew);
            }
            for (MpcaCommentIndex mpcaCommentIndexListNewMpcaCommentIndex : mpcaCommentIndexListNew) {
                if (!mpcaCommentIndexListOld.contains(mpcaCommentIndexListNewMpcaCommentIndex)) {
                    MpcaComment oldMpcaCommentOfMpcaCommentIndexListNewMpcaCommentIndex = mpcaCommentIndexListNewMpcaCommentIndex.getMpcaComment();
                    mpcaCommentIndexListNewMpcaCommentIndex.setMpcaComment(mpcaComment);
                    mpcaCommentIndexListNewMpcaCommentIndex = em.merge(mpcaCommentIndexListNewMpcaCommentIndex);
                    if (oldMpcaCommentOfMpcaCommentIndexListNewMpcaCommentIndex != null && !oldMpcaCommentOfMpcaCommentIndexListNewMpcaCommentIndex.equals(mpcaComment)) {
                        oldMpcaCommentOfMpcaCommentIndexListNewMpcaCommentIndex.getMpcaCommentIndexList().remove(mpcaCommentIndexListNewMpcaCommentIndex);
                        oldMpcaCommentOfMpcaCommentIndexListNewMpcaCommentIndex = em.merge(oldMpcaCommentOfMpcaCommentIndexListNewMpcaCommentIndex);
                    }
                }
            }
            for (MpcaCommentAddition mpcaCommentAdditionListNewMpcaCommentAddition : mpcaCommentAdditionListNew) {
                if (!mpcaCommentAdditionListOld.contains(mpcaCommentAdditionListNewMpcaCommentAddition)) {
                    MpcaComment oldMpcaCommentOfMpcaCommentAdditionListNewMpcaCommentAddition = mpcaCommentAdditionListNewMpcaCommentAddition.getMpcaComment();
                    mpcaCommentAdditionListNewMpcaCommentAddition.setMpcaComment(mpcaComment);
                    mpcaCommentAdditionListNewMpcaCommentAddition = em.merge(mpcaCommentAdditionListNewMpcaCommentAddition);
                    if (oldMpcaCommentOfMpcaCommentAdditionListNewMpcaCommentAddition != null && !oldMpcaCommentOfMpcaCommentAdditionListNewMpcaCommentAddition.equals(mpcaComment)) {
                        oldMpcaCommentOfMpcaCommentAdditionListNewMpcaCommentAddition.getMpcaCommentAdditionList().remove(mpcaCommentAdditionListNewMpcaCommentAddition);
                        oldMpcaCommentOfMpcaCommentAdditionListNewMpcaCommentAddition = em.merge(oldMpcaCommentOfMpcaCommentAdditionListNewMpcaCommentAddition);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaComment.getCommentId();
                if (findMpcaComment(id) == null) {
                    throw new NonexistentEntityException("The mpcaComment with id " + id + " no longer exists.");
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
            MpcaComment mpcaComment;
            try {
                mpcaComment = em.getReference(MpcaComment.class, id);
                mpcaComment.getCommentId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaComment with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaCommentIndex> mpcaCommentIndexListOrphanCheck = mpcaComment.getMpcaCommentIndexList();
            for (MpcaCommentIndex mpcaCommentIndexListOrphanCheckMpcaCommentIndex : mpcaCommentIndexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaComment (" + mpcaComment + ") cannot be destroyed since the MpcaCommentIndex " + mpcaCommentIndexListOrphanCheckMpcaCommentIndex + " in its mpcaCommentIndexList field has a non-nullable mpcaComment field.");
            }
            List<MpcaCommentAddition> mpcaCommentAdditionListOrphanCheck = mpcaComment.getMpcaCommentAdditionList();
            for (MpcaCommentAddition mpcaCommentAdditionListOrphanCheckMpcaCommentAddition : mpcaCommentAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaComment (" + mpcaComment + ") cannot be destroyed since the MpcaCommentAddition " + mpcaCommentAdditionListOrphanCheckMpcaCommentAddition + " in its mpcaCommentAdditionList field has a non-nullable mpcaComment field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            MpcaWebPage pageId = mpcaComment.getPageId();
            if (pageId != null) {
                pageId.getMpcaCommentList().remove(mpcaComment);
                pageId = em.merge(pageId);
            }
            MpcaProduct productId = mpcaComment.getProductId();
            if (productId != null) {
                productId.getMpcaCommentList().remove(mpcaComment);
                productId = em.merge(productId);
            }
            em.remove(mpcaComment);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaComment> findMpcaCommentEntities() {
        return findMpcaCommentEntities(true, -1, -1);
    }

    public List<MpcaComment> findMpcaCommentEntities(int maxResults, int firstResult) {
        return findMpcaCommentEntities(false, maxResults, firstResult);
    }

    private List<MpcaComment> findMpcaCommentEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaComment.class));
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

    public MpcaComment findMpcaComment(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaComment.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaCommentCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaComment> rt = cq.from(MpcaComment.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public List<MpcaComment> findMpcaCommentByAdditionAndValue(String addType, String value) {
        return findMpcaCommentByAdditionAndValue(addType, value, true, -1, -1);
    }

    public List<MpcaComment> findMpcaCommentByAdditionAndValue(String addType, String value, int maxResults, int firstResult) {
        return findMpcaCommentByAdditionAndValue(addType, value, false, maxResults, firstResult);
    }
    
    private List<MpcaComment> findMpcaCommentByAdditionAndValue(String addType, String value, boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaComment.findByAdditionAndValue");
            q.setParameter("addType", addType);
            q.setParameter("value", value);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }
}
