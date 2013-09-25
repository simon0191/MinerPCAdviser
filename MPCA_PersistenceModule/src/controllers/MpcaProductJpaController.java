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
import entities.MpcaProductAddition;
import java.util.ArrayList;
import java.util.List;
import entities.MpcaComment;
import entities.MpcaProduct;
import entities.MpcaProductIndex;
import entities.MpcaProductWebPage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaProductJpaController extends JpaController implements Serializable {

    public void create(MpcaProduct mpcaProduct) throws PreexistingEntityException, Exception {
        if (mpcaProduct.getMpcaProductAdditionList() == null) {
            mpcaProduct.setMpcaProductAdditionList(new ArrayList<MpcaProductAddition>());
        }
        if (mpcaProduct.getMpcaCommentList() == null) {
            mpcaProduct.setMpcaCommentList(new ArrayList<MpcaComment>());
        }
        if (mpcaProduct.getMpcaProductIndexList() == null) {
            mpcaProduct.setMpcaProductIndexList(new ArrayList<MpcaProductIndex>());
        }
        if (mpcaProduct.getMpcaProductWebPageList() == null) {
            mpcaProduct.setMpcaProductWebPageList(new ArrayList<MpcaProductWebPage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaProductAddition> attachedMpcaProductAdditionList = new ArrayList<MpcaProductAddition>();
            for (MpcaProductAddition mpcaProductAdditionListMpcaProductAdditionToAttach : mpcaProduct.getMpcaProductAdditionList()) {
                mpcaProductAdditionListMpcaProductAdditionToAttach = em.getReference(mpcaProductAdditionListMpcaProductAdditionToAttach.getClass(), mpcaProductAdditionListMpcaProductAdditionToAttach.getMpcaProductAdditionPK());
                attachedMpcaProductAdditionList.add(mpcaProductAdditionListMpcaProductAdditionToAttach);
            }
            mpcaProduct.setMpcaProductAdditionList(attachedMpcaProductAdditionList);
            List<MpcaComment> attachedMpcaCommentList = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListMpcaCommentToAttach : mpcaProduct.getMpcaCommentList()) {
                mpcaCommentListMpcaCommentToAttach = em.getReference(mpcaCommentListMpcaCommentToAttach.getClass(), mpcaCommentListMpcaCommentToAttach.getCommentId());
                attachedMpcaCommentList.add(mpcaCommentListMpcaCommentToAttach);
            }
            mpcaProduct.setMpcaCommentList(attachedMpcaCommentList);
            List<MpcaProductIndex> attachedMpcaProductIndexList = new ArrayList<MpcaProductIndex>();
            for (MpcaProductIndex mpcaProductIndexListMpcaProductIndexToAttach : mpcaProduct.getMpcaProductIndexList()) {
                mpcaProductIndexListMpcaProductIndexToAttach = em.getReference(mpcaProductIndexListMpcaProductIndexToAttach.getClass(), mpcaProductIndexListMpcaProductIndexToAttach.getMpcaProductIndexPK());
                attachedMpcaProductIndexList.add(mpcaProductIndexListMpcaProductIndexToAttach);
            }
            mpcaProduct.setMpcaProductIndexList(attachedMpcaProductIndexList);
            List<MpcaProductWebPage> attachedMpcaProductWebPageList = new ArrayList<MpcaProductWebPage>();
            for (MpcaProductWebPage mpcaProductWebPageListMpcaProductWebPageToAttach : mpcaProduct.getMpcaProductWebPageList()) {
                mpcaProductWebPageListMpcaProductWebPageToAttach = em.getReference(mpcaProductWebPageListMpcaProductWebPageToAttach.getClass(), mpcaProductWebPageListMpcaProductWebPageToAttach.getMpcaProductWebPagePK());
                attachedMpcaProductWebPageList.add(mpcaProductWebPageListMpcaProductWebPageToAttach);
            }
            mpcaProduct.setMpcaProductWebPageList(attachedMpcaProductWebPageList);
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
            for (MpcaComment mpcaCommentListMpcaComment : mpcaProduct.getMpcaCommentList()) {
                MpcaProduct oldProductIdOfMpcaCommentListMpcaComment = mpcaCommentListMpcaComment.getProductId();
                mpcaCommentListMpcaComment.setProductId(mpcaProduct);
                mpcaCommentListMpcaComment = em.merge(mpcaCommentListMpcaComment);
                if (oldProductIdOfMpcaCommentListMpcaComment != null) {
                    oldProductIdOfMpcaCommentListMpcaComment.getMpcaCommentList().remove(mpcaCommentListMpcaComment);
                    oldProductIdOfMpcaCommentListMpcaComment = em.merge(oldProductIdOfMpcaCommentListMpcaComment);
                }
            }
            for (MpcaProductIndex mpcaProductIndexListMpcaProductIndex : mpcaProduct.getMpcaProductIndexList()) {
                MpcaProduct oldMpcaProductOfMpcaProductIndexListMpcaProductIndex = mpcaProductIndexListMpcaProductIndex.getMpcaProduct();
                mpcaProductIndexListMpcaProductIndex.setMpcaProduct(mpcaProduct);
                mpcaProductIndexListMpcaProductIndex = em.merge(mpcaProductIndexListMpcaProductIndex);
                if (oldMpcaProductOfMpcaProductIndexListMpcaProductIndex != null) {
                    oldMpcaProductOfMpcaProductIndexListMpcaProductIndex.getMpcaProductIndexList().remove(mpcaProductIndexListMpcaProductIndex);
                    oldMpcaProductOfMpcaProductIndexListMpcaProductIndex = em.merge(oldMpcaProductOfMpcaProductIndexListMpcaProductIndex);
                }
            }
            for (MpcaProductWebPage mpcaProductWebPageListMpcaProductWebPage : mpcaProduct.getMpcaProductWebPageList()) {
                MpcaProduct oldMpcaProductOfMpcaProductWebPageListMpcaProductWebPage = mpcaProductWebPageListMpcaProductWebPage.getMpcaProduct();
                mpcaProductWebPageListMpcaProductWebPage.setMpcaProduct(mpcaProduct);
                mpcaProductWebPageListMpcaProductWebPage = em.merge(mpcaProductWebPageListMpcaProductWebPage);
                if (oldMpcaProductOfMpcaProductWebPageListMpcaProductWebPage != null) {
                    oldMpcaProductOfMpcaProductWebPageListMpcaProductWebPage.getMpcaProductWebPageList().remove(mpcaProductWebPageListMpcaProductWebPage);
                    oldMpcaProductOfMpcaProductWebPageListMpcaProductWebPage = em.merge(oldMpcaProductOfMpcaProductWebPageListMpcaProductWebPage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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

    public void edit(MpcaProduct mpcaProduct) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProduct persistentMpcaProduct = em.find(MpcaProduct.class, mpcaProduct.getProductId());
            List<MpcaProductAddition> mpcaProductAdditionListOld = persistentMpcaProduct.getMpcaProductAdditionList();
            List<MpcaProductAddition> mpcaProductAdditionListNew = mpcaProduct.getMpcaProductAdditionList();
            List<MpcaComment> mpcaCommentListOld = persistentMpcaProduct.getMpcaCommentList();
            List<MpcaComment> mpcaCommentListNew = mpcaProduct.getMpcaCommentList();
            List<MpcaProductIndex> mpcaProductIndexListOld = persistentMpcaProduct.getMpcaProductIndexList();
            List<MpcaProductIndex> mpcaProductIndexListNew = mpcaProduct.getMpcaProductIndexList();
            List<MpcaProductWebPage> mpcaProductWebPageListOld = persistentMpcaProduct.getMpcaProductWebPageList();
            List<MpcaProductWebPage> mpcaProductWebPageListNew = mpcaProduct.getMpcaProductWebPageList();
            List<String> illegalOrphanMessages = null;
            for (MpcaProductAddition mpcaProductAdditionListOldMpcaProductAddition : mpcaProductAdditionListOld) {
                if (!mpcaProductAdditionListNew.contains(mpcaProductAdditionListOldMpcaProductAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductAddition " + mpcaProductAdditionListOldMpcaProductAddition + " since its mpcaProduct field is not nullable.");
                }
            }
            for (MpcaComment mpcaCommentListOldMpcaComment : mpcaCommentListOld) {
                if (!mpcaCommentListNew.contains(mpcaCommentListOldMpcaComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaComment " + mpcaCommentListOldMpcaComment + " since its productId field is not nullable.");
                }
            }
            for (MpcaProductIndex mpcaProductIndexListOldMpcaProductIndex : mpcaProductIndexListOld) {
                if (!mpcaProductIndexListNew.contains(mpcaProductIndexListOldMpcaProductIndex)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductIndex " + mpcaProductIndexListOldMpcaProductIndex + " since its mpcaProduct field is not nullable.");
                }
            }
            for (MpcaProductWebPage mpcaProductWebPageListOldMpcaProductWebPage : mpcaProductWebPageListOld) {
                if (!mpcaProductWebPageListNew.contains(mpcaProductWebPageListOldMpcaProductWebPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductWebPage " + mpcaProductWebPageListOldMpcaProductWebPage + " since its mpcaProduct field is not nullable.");
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
            List<MpcaComment> attachedMpcaCommentListNew = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListNewMpcaCommentToAttach : mpcaCommentListNew) {
                mpcaCommentListNewMpcaCommentToAttach = em.getReference(mpcaCommentListNewMpcaCommentToAttach.getClass(), mpcaCommentListNewMpcaCommentToAttach.getCommentId());
                attachedMpcaCommentListNew.add(mpcaCommentListNewMpcaCommentToAttach);
            }
            mpcaCommentListNew = attachedMpcaCommentListNew;
            mpcaProduct.setMpcaCommentList(mpcaCommentListNew);
            List<MpcaProductIndex> attachedMpcaProductIndexListNew = new ArrayList<MpcaProductIndex>();
            for (MpcaProductIndex mpcaProductIndexListNewMpcaProductIndexToAttach : mpcaProductIndexListNew) {
                mpcaProductIndexListNewMpcaProductIndexToAttach = em.getReference(mpcaProductIndexListNewMpcaProductIndexToAttach.getClass(), mpcaProductIndexListNewMpcaProductIndexToAttach.getMpcaProductIndexPK());
                attachedMpcaProductIndexListNew.add(mpcaProductIndexListNewMpcaProductIndexToAttach);
            }
            mpcaProductIndexListNew = attachedMpcaProductIndexListNew;
            mpcaProduct.setMpcaProductIndexList(mpcaProductIndexListNew);
            List<MpcaProductWebPage> attachedMpcaProductWebPageListNew = new ArrayList<MpcaProductWebPage>();
            for (MpcaProductWebPage mpcaProductWebPageListNewMpcaProductWebPageToAttach : mpcaProductWebPageListNew) {
                mpcaProductWebPageListNewMpcaProductWebPageToAttach = em.getReference(mpcaProductWebPageListNewMpcaProductWebPageToAttach.getClass(), mpcaProductWebPageListNewMpcaProductWebPageToAttach.getMpcaProductWebPagePK());
                attachedMpcaProductWebPageListNew.add(mpcaProductWebPageListNewMpcaProductWebPageToAttach);
            }
            mpcaProductWebPageListNew = attachedMpcaProductWebPageListNew;
            mpcaProduct.setMpcaProductWebPageList(mpcaProductWebPageListNew);
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
            for (MpcaComment mpcaCommentListNewMpcaComment : mpcaCommentListNew) {
                if (!mpcaCommentListOld.contains(mpcaCommentListNewMpcaComment)) {
                    MpcaProduct oldProductIdOfMpcaCommentListNewMpcaComment = mpcaCommentListNewMpcaComment.getProductId();
                    mpcaCommentListNewMpcaComment.setProductId(mpcaProduct);
                    mpcaCommentListNewMpcaComment = em.merge(mpcaCommentListNewMpcaComment);
                    if (oldProductIdOfMpcaCommentListNewMpcaComment != null && !oldProductIdOfMpcaCommentListNewMpcaComment.equals(mpcaProduct)) {
                        oldProductIdOfMpcaCommentListNewMpcaComment.getMpcaCommentList().remove(mpcaCommentListNewMpcaComment);
                        oldProductIdOfMpcaCommentListNewMpcaComment = em.merge(oldProductIdOfMpcaCommentListNewMpcaComment);
                    }
                }
            }
            for (MpcaProductIndex mpcaProductIndexListNewMpcaProductIndex : mpcaProductIndexListNew) {
                if (!mpcaProductIndexListOld.contains(mpcaProductIndexListNewMpcaProductIndex)) {
                    MpcaProduct oldMpcaProductOfMpcaProductIndexListNewMpcaProductIndex = mpcaProductIndexListNewMpcaProductIndex.getMpcaProduct();
                    mpcaProductIndexListNewMpcaProductIndex.setMpcaProduct(mpcaProduct);
                    mpcaProductIndexListNewMpcaProductIndex = em.merge(mpcaProductIndexListNewMpcaProductIndex);
                    if (oldMpcaProductOfMpcaProductIndexListNewMpcaProductIndex != null && !oldMpcaProductOfMpcaProductIndexListNewMpcaProductIndex.equals(mpcaProduct)) {
                        oldMpcaProductOfMpcaProductIndexListNewMpcaProductIndex.getMpcaProductIndexList().remove(mpcaProductIndexListNewMpcaProductIndex);
                        oldMpcaProductOfMpcaProductIndexListNewMpcaProductIndex = em.merge(oldMpcaProductOfMpcaProductIndexListNewMpcaProductIndex);
                    }
                }
            }
            for (MpcaProductWebPage mpcaProductWebPageListNewMpcaProductWebPage : mpcaProductWebPageListNew) {
                if (!mpcaProductWebPageListOld.contains(mpcaProductWebPageListNewMpcaProductWebPage)) {
                    MpcaProduct oldMpcaProductOfMpcaProductWebPageListNewMpcaProductWebPage = mpcaProductWebPageListNewMpcaProductWebPage.getMpcaProduct();
                    mpcaProductWebPageListNewMpcaProductWebPage.setMpcaProduct(mpcaProduct);
                    mpcaProductWebPageListNewMpcaProductWebPage = em.merge(mpcaProductWebPageListNewMpcaProductWebPage);
                    if (oldMpcaProductOfMpcaProductWebPageListNewMpcaProductWebPage != null && !oldMpcaProductOfMpcaProductWebPageListNewMpcaProductWebPage.equals(mpcaProduct)) {
                        oldMpcaProductOfMpcaProductWebPageListNewMpcaProductWebPage.getMpcaProductWebPageList().remove(mpcaProductWebPageListNewMpcaProductWebPage);
                        oldMpcaProductOfMpcaProductWebPageListNewMpcaProductWebPage = em.merge(oldMpcaProductOfMpcaProductWebPageListNewMpcaProductWebPage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
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

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
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
            List<MpcaComment> mpcaCommentListOrphanCheck = mpcaProduct.getMpcaCommentList();
            for (MpcaComment mpcaCommentListOrphanCheckMpcaComment : mpcaCommentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaProduct (" + mpcaProduct + ") cannot be destroyed since the MpcaComment " + mpcaCommentListOrphanCheckMpcaComment + " in its mpcaCommentList field has a non-nullable productId field.");
            }
            List<MpcaProductIndex> mpcaProductIndexListOrphanCheck = mpcaProduct.getMpcaProductIndexList();
            for (MpcaProductIndex mpcaProductIndexListOrphanCheckMpcaProductIndex : mpcaProductIndexListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaProduct (" + mpcaProduct + ") cannot be destroyed since the MpcaProductIndex " + mpcaProductIndexListOrphanCheckMpcaProductIndex + " in its mpcaProductIndexList field has a non-nullable mpcaProduct field.");
            }
            List<MpcaProductWebPage> mpcaProductWebPageListOrphanCheck = mpcaProduct.getMpcaProductWebPageList();
            for (MpcaProductWebPage mpcaProductWebPageListOrphanCheckMpcaProductWebPage : mpcaProductWebPageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaProduct (" + mpcaProduct + ") cannot be destroyed since the MpcaProductWebPage " + mpcaProductWebPageListOrphanCheckMpcaProductWebPage + " in its mpcaProductWebPageList field has a non-nullable mpcaProduct field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mpcaProduct);
            em.getTransaction().commit();
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

    public MpcaProduct findProductByModel(String model) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("MpcaProduct.findByModel");
            q.setParameter("model", model);
            List<MpcaProduct> resultList = q.getResultList();
            MpcaProduct pro = null;
            if(!resultList.isEmpty()) {
                pro = resultList.get(0);
            }
            return pro;
        } finally {
            em.close();
        }
    }
    
}
