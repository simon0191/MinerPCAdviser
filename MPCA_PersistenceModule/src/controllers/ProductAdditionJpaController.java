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
import entities.Product;
import entities.Addition;
import entities.ProductAddition;
import entities.ProductAdditionPK;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class ProductAdditionJpaController implements Serializable {

    public ProductAdditionJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public ProductAdditionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ProductAddition productAddition) throws PreexistingEntityException, Exception {
        if (productAddition.getProductAdditionPK() == null) {
            productAddition.setProductAdditionPK(new ProductAdditionPK());
        }
        productAddition.getProductAdditionPK().setAdditionAddId(productAddition.getAddition().getAddId().toBigInteger());
        productAddition.getProductAdditionPK().setProductBrandId(productAddition.getProduct().getProductPK().getBrandId());
        productAddition.getProductAdditionPK().setProductProductId(productAddition.getProduct().getProductPK().getProductId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product product = productAddition.getProduct();
            if (product != null) {
                product = em.getReference(product.getClass(), product.getProductPK());
                productAddition.setProduct(product);
            }
            Addition addition = productAddition.getAddition();
            if (addition != null) {
                addition = em.getReference(addition.getClass(), addition.getAddId());
                productAddition.setAddition(addition);
            }
            em.persist(productAddition);
            if (product != null) {
                product.getProductAdditionList().add(productAddition);
                product = em.merge(product);
            }
            if (addition != null) {
                addition.getProductAdditionList().add(productAddition);
                addition = em.merge(addition);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProductAddition(productAddition.getProductAdditionPK()) != null) {
                throw new PreexistingEntityException("ProductAddition " + productAddition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ProductAddition productAddition) throws NonexistentEntityException, Exception {
        productAddition.getProductAdditionPK().setAdditionAddId(productAddition.getAddition().getAddId().toBigInteger());
        productAddition.getProductAdditionPK().setProductBrandId(productAddition.getProduct().getProductPK().getBrandId());
        productAddition.getProductAdditionPK().setProductProductId(productAddition.getProduct().getProductPK().getProductId());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductAddition persistentProductAddition = em.find(ProductAddition.class, productAddition.getProductAdditionPK());
            Product productOld = persistentProductAddition.getProduct();
            Product productNew = productAddition.getProduct();
            Addition additionOld = persistentProductAddition.getAddition();
            Addition additionNew = productAddition.getAddition();
            if (productNew != null) {
                productNew = em.getReference(productNew.getClass(), productNew.getProductPK());
                productAddition.setProduct(productNew);
            }
            if (additionNew != null) {
                additionNew = em.getReference(additionNew.getClass(), additionNew.getAddId());
                productAddition.setAddition(additionNew);
            }
            productAddition = em.merge(productAddition);
            if (productOld != null && !productOld.equals(productNew)) {
                productOld.getProductAdditionList().remove(productAddition);
                productOld = em.merge(productOld);
            }
            if (productNew != null && !productNew.equals(productOld)) {
                productNew.getProductAdditionList().add(productAddition);
                productNew = em.merge(productNew);
            }
            if (additionOld != null && !additionOld.equals(additionNew)) {
                additionOld.getProductAdditionList().remove(productAddition);
                additionOld = em.merge(additionOld);
            }
            if (additionNew != null && !additionNew.equals(additionOld)) {
                additionNew.getProductAdditionList().add(productAddition);
                additionNew = em.merge(additionNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductAdditionPK id = productAddition.getProductAdditionPK();
                if (findProductAddition(id) == null) {
                    throw new NonexistentEntityException("The productAddition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductAdditionPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            ProductAddition productAddition;
            try {
                productAddition = em.getReference(ProductAddition.class, id);
                productAddition.getProductAdditionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The productAddition with id " + id + " no longer exists.", enfe);
            }
            Product product = productAddition.getProduct();
            if (product != null) {
                product.getProductAdditionList().remove(productAddition);
                product = em.merge(product);
            }
            Addition addition = productAddition.getAddition();
            if (addition != null) {
                addition.getProductAdditionList().remove(productAddition);
                addition = em.merge(addition);
            }
            em.remove(productAddition);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ProductAddition> findProductAdditionEntities() {
        return findProductAdditionEntities(true, -1, -1);
    }

    public List<ProductAddition> findProductAdditionEntities(int maxResults, int firstResult) {
        return findProductAdditionEntities(false, maxResults, firstResult);
    }

    private List<ProductAddition> findProductAdditionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ProductAddition.class));
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

    public ProductAddition findProductAddition(ProductAdditionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ProductAddition.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductAdditionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ProductAddition> rt = cq.from(ProductAddition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
