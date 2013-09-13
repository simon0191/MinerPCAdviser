/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Brand;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Product;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class BrandJpaController implements Serializable {

    public BrandJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public BrandJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Brand brand) throws PreexistingEntityException, Exception {
        if (brand.getProductList() == null) {
            brand.setProductList(new ArrayList<Product>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Product> attachedProductList = new ArrayList<Product>();
            for (Product productListProductToAttach : brand.getProductList()) {
                productListProductToAttach = em.getReference(productListProductToAttach.getClass(), productListProductToAttach.getProductPK());
                attachedProductList.add(productListProductToAttach);
            }
            brand.setProductList(attachedProductList);
            em.persist(brand);
            for (Product productListProduct : brand.getProductList()) {
                Brand oldBrandOfProductListProduct = productListProduct.getBrand();
                productListProduct.setBrand(brand);
                productListProduct = em.merge(productListProduct);
                if (oldBrandOfProductListProduct != null) {
                    oldBrandOfProductListProduct.getProductList().remove(productListProduct);
                    oldBrandOfProductListProduct = em.merge(oldBrandOfProductListProduct);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findBrand(brand.getBrandId()) != null) {
                throw new PreexistingEntityException("Brand " + brand + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Brand brand) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Brand persistentBrand = em.find(Brand.class, brand.getBrandId());
            List<Product> productListOld = persistentBrand.getProductList();
            List<Product> productListNew = brand.getProductList();
            List<String> illegalOrphanMessages = null;
            for (Product productListOldProduct : productListOld) {
                if (!productListNew.contains(productListOldProduct)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Product " + productListOldProduct + " since its brand field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Product> attachedProductListNew = new ArrayList<Product>();
            for (Product productListNewProductToAttach : productListNew) {
                productListNewProductToAttach = em.getReference(productListNewProductToAttach.getClass(), productListNewProductToAttach.getProductPK());
                attachedProductListNew.add(productListNewProductToAttach);
            }
            productListNew = attachedProductListNew;
            brand.setProductList(productListNew);
            brand = em.merge(brand);
            for (Product productListNewProduct : productListNew) {
                if (!productListOld.contains(productListNewProduct)) {
                    Brand oldBrandOfProductListNewProduct = productListNewProduct.getBrand();
                    productListNewProduct.setBrand(brand);
                    productListNewProduct = em.merge(productListNewProduct);
                    if (oldBrandOfProductListNewProduct != null && !oldBrandOfProductListNewProduct.equals(brand)) {
                        oldBrandOfProductListNewProduct.getProductList().remove(productListNewProduct);
                        oldBrandOfProductListNewProduct = em.merge(oldBrandOfProductListNewProduct);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = brand.getBrandId();
                if (findBrand(id) == null) {
                    throw new NonexistentEntityException("The brand with id " + id + " no longer exists.");
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
            Brand brand;
            try {
                brand = em.getReference(Brand.class, id);
                brand.getBrandId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The brand with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Product> productListOrphanCheck = brand.getProductList();
            for (Product productListOrphanCheckProduct : productListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Brand (" + brand + ") cannot be destroyed since the Product " + productListOrphanCheckProduct + " in its productList field has a non-nullable brand field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(brand);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Brand> findBrandEntities() {
        return findBrandEntities(true, -1, -1);
    }

    public List<Brand> findBrandEntities(int maxResults, int firstResult) {
        return findBrandEntities(false, maxResults, firstResult);
    }

    private List<Brand> findBrandEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Brand.class));
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

    public Brand findBrand(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Brand.class, id);
        } finally {
            em.close();
        }
    }

    public int getBrandCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Brand> rt = cq.from(Brand.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Brand findBrandByName(String brand) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Brand.findByBrandName");
            q.setParameter("brandName", brand);
            List<Brand> bs = q.getResultList();
            Brand b = null;
            if(!bs.isEmpty()) {
                b = bs.get(0);
            }
            return b;
        } finally {
            em.close();
        }
    }
    
}
