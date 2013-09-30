/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.controllers;

import model.controllers.exceptions.NonexistentEntityException;
import model.controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.entities.MpcaWebPage;
import model.entities.MpcaProduct;
import model.entities.MpcaProductWebPage;
import model.entities.MpcaProductWebPagePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Antonio
 */
public class MpcaProductWebPageJpaController extends JpaController implements Serializable {

    public void create(MpcaProductWebPage mpcaProductWebPage) throws PreexistingEntityException, Exception {
        if (mpcaProductWebPage.getMpcaProductWebPagePK() == null) {
            mpcaProductWebPage.setMpcaProductWebPagePK(new MpcaProductWebPagePK());
        }
        mpcaProductWebPage.getMpcaProductWebPagePK().setMpcaProductProductId(mpcaProductWebPage.getMpcaProduct().getProductId());
        mpcaProductWebPage.getMpcaProductWebPagePK().setMpcaWebPagePageId(mpcaProductWebPage.getMpcaWebPage().getPageId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaWebPage mpcaWebPage = mpcaProductWebPage.getMpcaWebPage();
            if (mpcaWebPage != null) {
                mpcaWebPage = em.getReference(mpcaWebPage.getClass(), mpcaWebPage.getPageId());
                mpcaProductWebPage.setMpcaWebPage(mpcaWebPage);
            }
            MpcaProduct mpcaProduct = mpcaProductWebPage.getMpcaProduct();
            if (mpcaProduct != null) {
                mpcaProduct = em.getReference(mpcaProduct.getClass(), mpcaProduct.getProductId());
                mpcaProductWebPage.setMpcaProduct(mpcaProduct);
            }
            em.persist(mpcaProductWebPage);
            if (mpcaWebPage != null) {
                mpcaWebPage.getMpcaProductWebPageList().add(mpcaProductWebPage);
                mpcaWebPage = em.merge(mpcaWebPage);
            }
            if (mpcaProduct != null) {
                mpcaProduct.getMpcaProductWebPageList().add(mpcaProductWebPage);
                mpcaProduct = em.merge(mpcaProduct);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMpcaProductWebPage(mpcaProductWebPage.getMpcaProductWebPagePK()) != null) {
                throw new PreexistingEntityException("MpcaProductWebPage " + mpcaProductWebPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(MpcaProductWebPage mpcaProductWebPage) throws NonexistentEntityException, Exception {
        mpcaProductWebPage.getMpcaProductWebPagePK().setMpcaProductProductId(mpcaProductWebPage.getMpcaProduct().getProductId());
        mpcaProductWebPage.getMpcaProductWebPagePK().setMpcaWebPagePageId(mpcaProductWebPage.getMpcaWebPage().getPageId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProductWebPage persistentMpcaProductWebPage = em.find(MpcaProductWebPage.class, mpcaProductWebPage.getMpcaProductWebPagePK());
            MpcaWebPage mpcaWebPageOld = persistentMpcaProductWebPage.getMpcaWebPage();
            MpcaWebPage mpcaWebPageNew = mpcaProductWebPage.getMpcaWebPage();
            MpcaProduct mpcaProductOld = persistentMpcaProductWebPage.getMpcaProduct();
            MpcaProduct mpcaProductNew = mpcaProductWebPage.getMpcaProduct();
            if (mpcaWebPageNew != null) {
                mpcaWebPageNew = em.getReference(mpcaWebPageNew.getClass(), mpcaWebPageNew.getPageId());
                mpcaProductWebPage.setMpcaWebPage(mpcaWebPageNew);
            }
            if (mpcaProductNew != null) {
                mpcaProductNew = em.getReference(mpcaProductNew.getClass(), mpcaProductNew.getProductId());
                mpcaProductWebPage.setMpcaProduct(mpcaProductNew);
            }
            mpcaProductWebPage = em.merge(mpcaProductWebPage);
            if (mpcaWebPageOld != null && !mpcaWebPageOld.equals(mpcaWebPageNew)) {
                mpcaWebPageOld.getMpcaProductWebPageList().remove(mpcaProductWebPage);
                mpcaWebPageOld = em.merge(mpcaWebPageOld);
            }
            if (mpcaWebPageNew != null && !mpcaWebPageNew.equals(mpcaWebPageOld)) {
                mpcaWebPageNew.getMpcaProductWebPageList().add(mpcaProductWebPage);
                mpcaWebPageNew = em.merge(mpcaWebPageNew);
            }
            if (mpcaProductOld != null && !mpcaProductOld.equals(mpcaProductNew)) {
                mpcaProductOld.getMpcaProductWebPageList().remove(mpcaProductWebPage);
                mpcaProductOld = em.merge(mpcaProductOld);
            }
            if (mpcaProductNew != null && !mpcaProductNew.equals(mpcaProductOld)) {
                mpcaProductNew.getMpcaProductWebPageList().add(mpcaProductWebPage);
                mpcaProductNew = em.merge(mpcaProductNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                MpcaProductWebPagePK id = mpcaProductWebPage.getMpcaProductWebPagePK();
                if (findMpcaProductWebPage(id) == null) {
                    throw new NonexistentEntityException("The mpcaProductWebPage with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(MpcaProductWebPagePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            MpcaProductWebPage mpcaProductWebPage;
            try {
                mpcaProductWebPage = em.getReference(MpcaProductWebPage.class, id);
                mpcaProductWebPage.getMpcaProductWebPagePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The mpcaProductWebPage with id " + id + " no longer exists.", enfe);
            }
            MpcaWebPage mpcaWebPage = mpcaProductWebPage.getMpcaWebPage();
            if (mpcaWebPage != null) {
                mpcaWebPage.getMpcaProductWebPageList().remove(mpcaProductWebPage);
                mpcaWebPage = em.merge(mpcaWebPage);
            }
            MpcaProduct mpcaProduct = mpcaProductWebPage.getMpcaProduct();
            if (mpcaProduct != null) {
                mpcaProduct.getMpcaProductWebPageList().remove(mpcaProductWebPage);
                mpcaProduct = em.merge(mpcaProduct);
            }
            em.remove(mpcaProductWebPage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<MpcaProductWebPage> findMpcaProductWebPageEntities() {
        return findMpcaProductWebPageEntities(true, -1, -1);
    }

    public List<MpcaProductWebPage> findMpcaProductWebPageEntities(int maxResults, int firstResult) {
        return findMpcaProductWebPageEntities(false, maxResults, firstResult);
    }

    private List<MpcaProductWebPage> findMpcaProductWebPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(MpcaProductWebPage.class));
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

    public MpcaProductWebPage findMpcaProductWebPage(MpcaProductWebPagePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(MpcaProductWebPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getMpcaProductWebPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<MpcaProductWebPage> rt = cq.from(MpcaProductWebPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
