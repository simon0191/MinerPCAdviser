/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.WebPage;
import entities.Product;
import entities.ProductWebPage;
import entities.ProductWebPagePK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class ProductWebPageJpaController implements Serializable {

    public ProductWebPageJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public ProductWebPageJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductWebPage productWebPage) throws PreexistingEntityException, Exception {
        if (productWebPage.getProductWebPagePK() == null) {
            productWebPage.setProductWebPagePK(new ProductWebPagePK());
        }
        productWebPage.getProductWebPagePK().setWebPagePageId(productWebPage.getWebPage().getPageId().toBigInteger());
        productWebPage.getProductWebPagePK().setProductProductId(productWebPage.getProduct().getProductPK().getProductId());
        productWebPage.getProductWebPagePK().setProductBrandId(productWebPage.getProduct().getProductPK().getBrandId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            WebPage webPage = productWebPage.getWebPage();
            if (webPage != null) {
                webPage = em.getReference(webPage.getClass(), webPage.getPageId());
                productWebPage.setWebPage(webPage);
            }
            Product product = productWebPage.getProduct();
            if (product != null) {
                product = em.getReference(product.getClass(), product.getProductPK());
                productWebPage.setProduct(product);
            }
            em.persist(productWebPage);
            if (webPage != null) {
                webPage.getProductWebPageList().add(productWebPage);
                webPage = em.merge(webPage);
            }
            if (product != null) {
                product.getProductWebPageList().add(productWebPage);
                product = em.merge(product);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductWebPage(productWebPage.getProductWebPagePK()) != null) {
                throw new PreexistingEntityException("ProductWebPage " + productWebPage + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductWebPage productWebPage) throws NonexistentEntityException, Exception {
        productWebPage.getProductWebPagePK().setWebPagePageId(productWebPage.getWebPage().getPageId().toBigInteger());
        productWebPage.getProductWebPagePK().setProductProductId(productWebPage.getProduct().getProductPK().getProductId());
        productWebPage.getProductWebPagePK().setProductBrandId(productWebPage.getProduct().getProductPK().getBrandId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductWebPage persistentProductWebPage = em.find(ProductWebPage.class, productWebPage.getProductWebPagePK());
            WebPage webPageOld = persistentProductWebPage.getWebPage();
            WebPage webPageNew = productWebPage.getWebPage();
            Product productOld = persistentProductWebPage.getProduct();
            Product productNew = productWebPage.getProduct();
            if (webPageNew != null) {
                webPageNew = em.getReference(webPageNew.getClass(), webPageNew.getPageId());
                productWebPage.setWebPage(webPageNew);
            }
            if (productNew != null) {
                productNew = em.getReference(productNew.getClass(), productNew.getProductPK());
                productWebPage.setProduct(productNew);
            }
            productWebPage = em.merge(productWebPage);
            if (webPageOld != null && !webPageOld.equals(webPageNew)) {
                webPageOld.getProductWebPageList().remove(productWebPage);
                webPageOld = em.merge(webPageOld);
            }
            if (webPageNew != null && !webPageNew.equals(webPageOld)) {
                webPageNew.getProductWebPageList().add(productWebPage);
                webPageNew = em.merge(webPageNew);
            }
            if (productOld != null && !productOld.equals(productNew)) {
                productOld.getProductWebPageList().remove(productWebPage);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld)) {
                productNew.getProductWebPageList().add(productWebPage);
                productNew = em.merge(productNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductWebPagePK id = productWebPage.getProductWebPagePK();
                if (findProductWebPage(id) == null) {
                    throw new NonexistentEntityException("The productWebPage with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductWebPagePK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductWebPage productWebPage;
            try {
                productWebPage = em.getReference(ProductWebPage.class, id);
                productWebPage.getProductWebPagePK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productWebPage with id " + id + " no longer exists.", enfe);
            }
            WebPage webPage = productWebPage.getWebPage();
            if (webPage != null) {
                webPage.getProductWebPageList().remove(productWebPage);
                webPage = em.merge(webPage);
            }
            Product product = productWebPage.getProduct();
            if (product != null) {
                product.getProductWebPageList().remove(productWebPage);
                product = em.merge(product);
            }
            em.remove(productWebPage);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductWebPage> findProductWebPageEntities() {
        return findProductWebPageEntities(true, -1, -1);
    }

    public List<ProductWebPage> findProductWebPageEntities(int maxResults, int firstResult) {
        return findProductWebPageEntities(false, maxResults, firstResult);
    }

    private List<ProductWebPage> findProductWebPageEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductWebPage.class));
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

    public ProductWebPage findProductWebPage(ProductWebPagePK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductWebPage.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductWebPageCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductWebPage> rt = cq.from(ProductWebPage.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
