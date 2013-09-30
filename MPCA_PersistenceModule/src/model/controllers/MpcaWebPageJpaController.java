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
import model.entities.MpcaComment;
import java.util.ArrayList;
import java.util.List;
import model.entities.MpcaProductWebPage;
import model.entities.MpcaWebPage;
import javax.persistence.EntityManager;

/**
 *
 * @author Antonio
 */
public class MpcaWebPageJpaController extends JpaController implements Serializable {

    public void create(MpcaWebPage mpcaWebPage) throws PreexistingEntityException, Exception {
        if (mpcaWebPage.getMpcaCommentList() == null) {
            mpcaWebPage.setMpcaCommentList(new ArrayList<MpcaComment>());
        }
        if (mpcaWebPage.getMpcaProductWebPageList() == null) {
            mpcaWebPage.setMpcaProductWebPageList(new ArrayList<MpcaProductWebPage>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaComment> attachedMpcaCommentList = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListMpcaCommentToAttach : mpcaWebPage.getMpcaCommentList()) {
                mpcaCommentListMpcaCommentToAttach = em.getReference(mpcaCommentListMpcaCommentToAttach.getClass(), mpcaCommentListMpcaCommentToAttach.getCommentId());
                attachedMpcaCommentList.add(mpcaCommentListMpcaCommentToAttach);
            }
            mpcaWebPage.setMpcaCommentList(attachedMpcaCommentList);
            List<MpcaProductWebPage> attachedMpcaProductWebPageList = new ArrayList<MpcaProductWebPage>();
            for (MpcaProductWebPage mpcaProductWebPageListMpcaProductWebPageToAttach : mpcaWebPage.getMpcaProductWebPageList()) {
                mpcaProductWebPageListMpcaProductWebPageToAttach = em.getReference(mpcaProductWebPageListMpcaProductWebPageToAttach.getClass(), mpcaProductWebPageListMpcaProductWebPageToAttach.getMpcaProductWebPagePK());
                attachedMpcaProductWebPageList.add(mpcaProductWebPageListMpcaProductWebPageToAttach);
            }
            mpcaWebPage.setMpcaProductWebPageList(attachedMpcaProductWebPageList);
            em.persist(mpcaWebPage);
            for (MpcaComment mpcaCommentListMpcaComment : mpcaWebPage.getMpcaCommentList()) {
                MpcaWebPage oldPageIdOfMpcaCommentListMpcaComment = mpcaCommentListMpcaComment.getPageId();
                mpcaCommentListMpcaComment.setPageId(mpcaWebPage);
                mpcaCommentListMpcaComment = em.merge(mpcaCommentListMpcaComment);
                if (oldPageIdOfMpcaCommentListMpcaComment != null) {
                    oldPageIdOfMpcaCommentListMpcaComment.getMpcaCommentList().remove(mpcaCommentListMpcaComment);
                    oldPageIdOfMpcaCommentListMpcaComment = em.merge(oldPageIdOfMpcaCommentListMpcaComment);
                }
            }
            for (MpcaProductWebPage mpcaProductWebPageListMpcaProductWebPage : mpcaWebPage.getMpcaProductWebPageList()) {
                MpcaWebPage oldMpcaWebPageOfMpcaProductWebPageListMpcaProductWebPage = mpcaProductWebPageListMpcaProductWebPage.getMpcaWebPage();
                mpcaProductWebPageListMpcaProductWebPage.setMpcaWebPage(mpcaWebPage);
                mpcaProductWebPageListMpcaProductWebPage = em.merge(mpcaProductWebPageListMpcaProductWebPage);
                if (oldMpcaWebPageOfMpcaProductWebPageListMpcaProductWebPage != null) {
                    oldMpcaWebPageOfMpcaProductWebPageListMpcaProductWebPage.getMpcaProductWebPageList().remove(mpcaProductWebPageListMpcaProductWebPage);
                    oldMpcaWebPageOfMpcaProductWebPageListMpcaProductWebPage = em.merge(oldMpcaWebPageOfMpcaProductWebPageListMpcaProductWebPage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaWebPage(mpcaWebPage.getPageId()) != null) {
                throw new PreexistingEntityException("MpcaWebPage " + mpcaWebPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaWebPage mpcaWebPage) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaWebPage persistentMpcaWebPage = em.find(MpcaWebPage.class, mpcaWebPage.getPageId());
            List<MpcaComment> mpcaCommentListOld = persistentMpcaWebPage.getMpcaCommentList();
            List<MpcaComment> mpcaCommentListNew = mpcaWebPage.getMpcaCommentList();
            List<MpcaProductWebPage> mpcaProductWebPageListOld = persistentMpcaWebPage.getMpcaProductWebPageList();
            List<MpcaProductWebPage> mpcaProductWebPageListNew = mpcaWebPage.getMpcaProductWebPageList();
            List<String> illegalOrphanMessages = null;
            for (MpcaComment mpcaCommentListOldMpcaComment : mpcaCommentListOld) {
                if (!mpcaCommentListNew.contains(mpcaCommentListOldMpcaComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaComment " + mpcaCommentListOldMpcaComment + " since its pageId field is not nullable.");
                }
            }
            for (MpcaProductWebPage mpcaProductWebPageListOldMpcaProductWebPage : mpcaProductWebPageListOld) {
                if (!mpcaProductWebPageListNew.contains(mpcaProductWebPageListOldMpcaProductWebPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaProductWebPage " + mpcaProductWebPageListOldMpcaProductWebPage + " since its mpcaWebPage field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaComment> attachedMpcaCommentListNew = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListNewMpcaCommentToAttach : mpcaCommentListNew) {
                mpcaCommentListNewMpcaCommentToAttach = em.getReference(mpcaCommentListNewMpcaCommentToAttach.getClass(), mpcaCommentListNewMpcaCommentToAttach.getCommentId());
                attachedMpcaCommentListNew.add(mpcaCommentListNewMpcaCommentToAttach);
            }
            mpcaCommentListNew = attachedMpcaCommentListNew;
            mpcaWebPage.setMpcaCommentList(mpcaCommentListNew);
            List<MpcaProductWebPage> attachedMpcaProductWebPageListNew = new ArrayList<MpcaProductWebPage>();
            for (MpcaProductWebPage mpcaProductWebPageListNewMpcaProductWebPageToAttach : mpcaProductWebPageListNew) {
                mpcaProductWebPageListNewMpcaProductWebPageToAttach = em.getReference(mpcaProductWebPageListNewMpcaProductWebPageToAttach.getClass(), mpcaProductWebPageListNewMpcaProductWebPageToAttach.getMpcaProductWebPagePK());
                attachedMpcaProductWebPageListNew.add(mpcaProductWebPageListNewMpcaProductWebPageToAttach);
            }
            mpcaProductWebPageListNew = attachedMpcaProductWebPageListNew;
            mpcaWebPage.setMpcaProductWebPageList(mpcaProductWebPageListNew);
            mpcaWebPage = em.merge(mpcaWebPage);
            for (MpcaComment mpcaCommentListNewMpcaComment : mpcaCommentListNew) {
                if (!mpcaCommentListOld.contains(mpcaCommentListNewMpcaComment)) {
                    MpcaWebPage oldPageIdOfMpcaCommentListNewMpcaComment = mpcaCommentListNewMpcaComment.getPageId();
                    mpcaCommentListNewMpcaComment.setPageId(mpcaWebPage);
                    mpcaCommentListNewMpcaComment = em.merge(mpcaCommentListNewMpcaComment);
                    if (oldPageIdOfMpcaCommentListNewMpcaComment != null && !oldPageIdOfMpcaCommentListNewMpcaComment.equals(mpcaWebPage)) {
                        oldPageIdOfMpcaCommentListNewMpcaComment.getMpcaCommentList().remove(mpcaCommentListNewMpcaComment);
                        oldPageIdOfMpcaCommentListNewMpcaComment = em.merge(oldPageIdOfMpcaCommentListNewMpcaComment);
                    }
                }
            }
            for (MpcaProductWebPage mpcaProductWebPageListNewMpcaProductWebPage : mpcaProductWebPageListNew) {
                if (!mpcaProductWebPageListOld.contains(mpcaProductWebPageListNewMpcaProductWebPage)) {
                    MpcaWebPage oldMpcaWebPageOfMpcaProductWebPageListNewMpcaProductWebPage = mpcaProductWebPageListNewMpcaProductWebPage.getMpcaWebPage();
                    mpcaProductWebPageListNewMpcaProductWebPage.setMpcaWebPage(mpcaWebPage);
                    mpcaProductWebPageListNewMpcaProductWebPage = em.merge(mpcaProductWebPageListNewMpcaProductWebPage);
                    if (oldMpcaWebPageOfMpcaProductWebPageListNewMpcaProductWebPage != null && !oldMpcaWebPageOfMpcaProductWebPageListNewMpcaProductWebPage.equals(mpcaWebPage)) {
                        oldMpcaWebPageOfMpcaProductWebPageListNewMpcaProductWebPage.getMpcaProductWebPageList().remove(mpcaProductWebPageListNewMpcaProductWebPage);
                        oldMpcaWebPageOfMpcaProductWebPageListNewMpcaProductWebPage = em.merge(oldMpcaWebPageOfMpcaProductWebPageListNewMpcaProductWebPage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = mpcaWebPage.getPageId();
                if (findMpcaWebPage(id) == null) {
                    throw new NonexistentEntityException("The mpcaWebPage with id " + id + " no longer exists.");
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
            MpcaWebPage mpcaWebPage;
            try {
                mpcaWebPage = em.getReference(MpcaWebPage.class, id);
                mpcaWebPage.getPageId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaWebPage with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaComment> mpcaCommentListOrphanCheck = mpcaWebPage.getMpcaCommentList();
            for (MpcaComment mpcaCommentListOrphanCheckMpcaComment : mpcaCommentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaWebPage (" + mpcaWebPage + ") cannot be destroyed since the MpcaComment " + mpcaCommentListOrphanCheckMpcaComment + " in its mpcaCommentList field has a non-nullable pageId field.");
            }
            List<MpcaProductWebPage> mpcaProductWebPageListOrphanCheck = mpcaWebPage.getMpcaProductWebPageList();
            for (MpcaProductWebPage mpcaProductWebPageListOrphanCheckMpcaProductWebPage : mpcaProductWebPageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This MpcaWebPage (" + mpcaWebPage + ") cannot be destroyed since the MpcaProductWebPage " + mpcaProductWebPageListOrphanCheckMpcaProductWebPage + " in its mpcaProductWebPageList field has a non-nullable mpcaWebPage field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(mpcaWebPage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaWebPage> findMpcaWebPageEntities() {
        return findMpcaWebPageEntities(true, -1, -1);
    }

    public List<MpcaWebPage> findMpcaWebPageEntities(int maxResults, int firstResult) {
        return findMpcaWebPageEntities(false, maxResults, firstResult);
    }

    private List<MpcaWebPage> findMpcaWebPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaWebPage.class));
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

    public MpcaWebPage findMpcaWebPage(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaWebPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaWebPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaWebPage> rt = cq.from(MpcaWebPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
