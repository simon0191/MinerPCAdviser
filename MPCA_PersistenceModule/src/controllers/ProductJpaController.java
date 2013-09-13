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
import entities.Brand;
import entities.ProductAddition;
import java.util.ArrayList;
import java.util.List;
import entities.MpcaComment;
import entities.Product;
import entities.ProductPK;
import entities.ProductWebPage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class ProductJpaController implements Serializable {

    public ProductJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public ProductJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Product product) throws PreexistingEntityException, Exception {
        if (product.getProductPK() == null) {
            product.setProductPK(new ProductPK());
        }
        if (product.getProductAdditionList() == null) {
            product.setProductAdditionList(new ArrayList<ProductAddition>());
        }
        if (product.getMpcaCommentList() == null) {
            product.setMpcaCommentList(new ArrayList<MpcaComment>());
        }
        if (product.getProductWebPageList() == null) {
            product.setProductWebPageList(new ArrayList<ProductWebPage>());
        }
        product.getProductPK().setBrandId(product.getBrand().getBrandId().toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Brand brand = product.getBrand();
            if (brand != null) {
                brand = em.getReference(brand.getClass(), brand.getBrandId());
                product.setBrand(brand);
            }
            List<ProductAddition> attachedProductAdditionList = new ArrayList<ProductAddition>();
            for (ProductAddition productAdditionListProductAdditionToAttach : product.getProductAdditionList()) {
                productAdditionListProductAdditionToAttach = em.getReference(productAdditionListProductAdditionToAttach.getClass(), productAdditionListProductAdditionToAttach.getProductAdditionPK());
                attachedProductAdditionList.add(productAdditionListProductAdditionToAttach);
            }
            product.setProductAdditionList(attachedProductAdditionList);
            List<MpcaComment> attachedMpcaCommentList = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListMpcaCommentToAttach : product.getMpcaCommentList()) {
                mpcaCommentListMpcaCommentToAttach = em.getReference(mpcaCommentListMpcaCommentToAttach.getClass(), mpcaCommentListMpcaCommentToAttach.getMpcaCommentPK());
                attachedMpcaCommentList.add(mpcaCommentListMpcaCommentToAttach);
            }
            product.setMpcaCommentList(attachedMpcaCommentList);
            List<ProductWebPage> attachedProductWebPageList = new ArrayList<ProductWebPage>();
            for (ProductWebPage productWebPageListProductWebPageToAttach : product.getProductWebPageList()) {
                productWebPageListProductWebPageToAttach = em.getReference(productWebPageListProductWebPageToAttach.getClass(), productWebPageListProductWebPageToAttach.getProductWebPagePK());
                attachedProductWebPageList.add(productWebPageListProductWebPageToAttach);
            }
            product.setProductWebPageList(attachedProductWebPageList);
            em.persist(product);
            if (brand != null) {
                brand.getProductList().add(product);
                brand = em.merge(brand);
            }
            for (ProductAddition productAdditionListProductAddition : product.getProductAdditionList()) {
                Product oldProductOfProductAdditionListProductAddition = productAdditionListProductAddition.getProduct();
                productAdditionListProductAddition.setProduct(product);
                productAdditionListProductAddition = em.merge(productAdditionListProductAddition);
                if (oldProductOfProductAdditionListProductAddition != null) {
                    oldProductOfProductAdditionListProductAddition.getProductAdditionList().remove(productAdditionListProductAddition);
                    oldProductOfProductAdditionListProductAddition = em.merge(oldProductOfProductAdditionListProductAddition);
                }
            }
            for (MpcaComment mpcaCommentListMpcaComment : product.getMpcaCommentList()) {
                Product oldProductOfMpcaCommentListMpcaComment = mpcaCommentListMpcaComment.getProduct();
                mpcaCommentListMpcaComment.setProduct(product);
                mpcaCommentListMpcaComment = em.merge(mpcaCommentListMpcaComment);
                if (oldProductOfMpcaCommentListMpcaComment != null) {
                    oldProductOfMpcaCommentListMpcaComment.getMpcaCommentList().remove(mpcaCommentListMpcaComment);
                    oldProductOfMpcaCommentListMpcaComment = em.merge(oldProductOfMpcaCommentListMpcaComment);
                }
            }
            for (ProductWebPage productWebPageListProductWebPage : product.getProductWebPageList()) {
                Product oldProductOfProductWebPageListProductWebPage = productWebPageListProductWebPage.getProduct();
                productWebPageListProductWebPage.setProduct(product);
                productWebPageListProductWebPage = em.merge(productWebPageListProductWebPage);
                if (oldProductOfProductWebPageListProductWebPage != null) {
                    oldProductOfProductWebPageListProductWebPage.getProductWebPageList().remove(productWebPageListProductWebPage);
                    oldProductOfProductWebPageListProductWebPage = em.merge(oldProductOfProductWebPageListProductWebPage);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findProduct(product.getProductPK()) != null) {
                throw new PreexistingEntityException("Product " + product + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Product product) throws IllegalOrphanException, NonexistentEntityException, Exception {
        product.getProductPK().setBrandId(product.getBrand().getBrandId().toBigInteger());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product persistentProduct = em.find(Product.class, product.getProductPK());
            Brand brandOld = persistentProduct.getBrand();
            Brand brandNew = product.getBrand();
            List<ProductAddition> productAdditionListOld = persistentProduct.getProductAdditionList();
            List<ProductAddition> productAdditionListNew = product.getProductAdditionList();
            List<MpcaComment> mpcaCommentListOld = persistentProduct.getMpcaCommentList();
            List<MpcaComment> mpcaCommentListNew = product.getMpcaCommentList();
            List<ProductWebPage> productWebPageListOld = persistentProduct.getProductWebPageList();
            List<ProductWebPage> productWebPageListNew = product.getProductWebPageList();
            List<String> illegalOrphanMessages = null;
            for (ProductAddition productAdditionListOldProductAddition : productAdditionListOld) {
                if (!productAdditionListNew.contains(productAdditionListOldProductAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductAddition " + productAdditionListOldProductAddition + " since its product field is not nullable.");
                }
            }
            for (MpcaComment mpcaCommentListOldMpcaComment : mpcaCommentListOld) {
                if (!mpcaCommentListNew.contains(mpcaCommentListOldMpcaComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaComment " + mpcaCommentListOldMpcaComment + " since its product field is not nullable.");
                }
            }
            for (ProductWebPage productWebPageListOldProductWebPage : productWebPageListOld) {
                if (!productWebPageListNew.contains(productWebPageListOldProductWebPage)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductWebPage " + productWebPageListOldProductWebPage + " since its product field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (brandNew != null) {
                brandNew = em.getReference(brandNew.getClass(), brandNew.getBrandId());
                product.setBrand(brandNew);
            }
            List<ProductAddition> attachedProductAdditionListNew = new ArrayList<ProductAddition>();
            for (ProductAddition productAdditionListNewProductAdditionToAttach : productAdditionListNew) {
                productAdditionListNewProductAdditionToAttach = em.getReference(productAdditionListNewProductAdditionToAttach.getClass(), productAdditionListNewProductAdditionToAttach.getProductAdditionPK());
                attachedProductAdditionListNew.add(productAdditionListNewProductAdditionToAttach);
            }
            productAdditionListNew = attachedProductAdditionListNew;
            product.setProductAdditionList(productAdditionListNew);
            List<MpcaComment> attachedMpcaCommentListNew = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListNewMpcaCommentToAttach : mpcaCommentListNew) {
                mpcaCommentListNewMpcaCommentToAttach = em.getReference(mpcaCommentListNewMpcaCommentToAttach.getClass(), mpcaCommentListNewMpcaCommentToAttach.getMpcaCommentPK());
                attachedMpcaCommentListNew.add(mpcaCommentListNewMpcaCommentToAttach);
            }
            mpcaCommentListNew = attachedMpcaCommentListNew;
            product.setMpcaCommentList(mpcaCommentListNew);
            List<ProductWebPage> attachedProductWebPageListNew = new ArrayList<ProductWebPage>();
            for (ProductWebPage productWebPageListNewProductWebPageToAttach : productWebPageListNew) {
                productWebPageListNewProductWebPageToAttach = em.getReference(productWebPageListNewProductWebPageToAttach.getClass(), productWebPageListNewProductWebPageToAttach.getProductWebPagePK());
                attachedProductWebPageListNew.add(productWebPageListNewProductWebPageToAttach);
            }
            productWebPageListNew = attachedProductWebPageListNew;
            product.setProductWebPageList(productWebPageListNew);
            product = em.merge(product);
            if (brandOld != null && !brandOld.equals(brandNew)) {
                brandOld.getProductList().remove(product);
                brandOld = em.merge(brandOld);
            }
            if (brandNew != null && !brandNew.equals(brandOld)) {
                brandNew.getProductList().add(product);
                brandNew = em.merge(brandNew);
            }
            for (ProductAddition productAdditionListNewProductAddition : productAdditionListNew) {
                if (!productAdditionListOld.contains(productAdditionListNewProductAddition)) {
                    Product oldProductOfProductAdditionListNewProductAddition = productAdditionListNewProductAddition.getProduct();
                    productAdditionListNewProductAddition.setProduct(product);
                    productAdditionListNewProductAddition = em.merge(productAdditionListNewProductAddition);
                    if (oldProductOfProductAdditionListNewProductAddition != null && !oldProductOfProductAdditionListNewProductAddition.equals(product)) {
                        oldProductOfProductAdditionListNewProductAddition.getProductAdditionList().remove(productAdditionListNewProductAddition);
                        oldProductOfProductAdditionListNewProductAddition = em.merge(oldProductOfProductAdditionListNewProductAddition);
                    }
                }
            }
            for (MpcaComment mpcaCommentListNewMpcaComment : mpcaCommentListNew) {
                if (!mpcaCommentListOld.contains(mpcaCommentListNewMpcaComment)) {
                    Product oldProductOfMpcaCommentListNewMpcaComment = mpcaCommentListNewMpcaComment.getProduct();
                    mpcaCommentListNewMpcaComment.setProduct(product);
                    mpcaCommentListNewMpcaComment = em.merge(mpcaCommentListNewMpcaComment);
                    if (oldProductOfMpcaCommentListNewMpcaComment != null && !oldProductOfMpcaCommentListNewMpcaComment.equals(product)) {
                        oldProductOfMpcaCommentListNewMpcaComment.getMpcaCommentList().remove(mpcaCommentListNewMpcaComment);
                        oldProductOfMpcaCommentListNewMpcaComment = em.merge(oldProductOfMpcaCommentListNewMpcaComment);
                    }
                }
            }
            for (ProductWebPage productWebPageListNewProductWebPage : productWebPageListNew) {
                if (!productWebPageListOld.contains(productWebPageListNewProductWebPage)) {
                    Product oldProductOfProductWebPageListNewProductWebPage = productWebPageListNewProductWebPage.getProduct();
                    productWebPageListNewProductWebPage.setProduct(product);
                    productWebPageListNewProductWebPage = em.merge(productWebPageListNewProductWebPage);
                    if (oldProductOfProductWebPageListNewProductWebPage != null && !oldProductOfProductWebPageListNewProductWebPage.equals(product)) {
                        oldProductOfProductWebPageListNewProductWebPage.getProductWebPageList().remove(productWebPageListNewProductWebPage);
                        oldProductOfProductWebPageListNewProductWebPage = em.merge(oldProductOfProductWebPageListNewProductWebPage);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                ProductPK id = product.getProductPK();
                if (findProduct(id) == null) {
                    throw new NonexistentEntityException("The product with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ProductPK id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Product product;
            try {
                product = em.getReference(Product.class, id);
                product.getProductPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The product with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProductAddition> productAdditionListOrphanCheck = product.getProductAdditionList();
            for (ProductAddition productAdditionListOrphanCheckProductAddition : productAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the ProductAddition " + productAdditionListOrphanCheckProductAddition + " in its productAdditionList field has a non-nullable product field.");
            }
            List<MpcaComment> mpcaCommentListOrphanCheck = product.getMpcaCommentList();
            for (MpcaComment mpcaCommentListOrphanCheckMpcaComment : mpcaCommentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the MpcaComment " + mpcaCommentListOrphanCheckMpcaComment + " in its mpcaCommentList field has a non-nullable product field.");
            }
            List<ProductWebPage> productWebPageListOrphanCheck = product.getProductWebPageList();
            for (ProductWebPage productWebPageListOrphanCheckProductWebPage : productWebPageListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Product (" + product + ") cannot be destroyed since the ProductWebPage " + productWebPageListOrphanCheckProductWebPage + " in its productWebPageList field has a non-nullable product field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Brand brand = product.getBrand();
            if (brand != null) {
                brand.getProductList().remove(product);
                brand = em.merge(brand);
            }
            em.remove(product);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Product> findProductEntities() {
        return findProductEntities(true, -1, -1);
    }

    public List<Product> findProductEntities(int maxResults, int firstResult) {
        return findProductEntities(false, maxResults, firstResult);
    }

    private List<Product> findProductEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Product.class));
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

    public Product findProduct(ProductPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Product.class, id);
        } finally {
            em.close();
        }
    }

    public int getProductCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Product> rt = cq.from(Product.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Product findProductByModel(String model) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Product.findByModel");
            q.setParameter("model", model);
            List<Product> ps = q.getResultList();
            Product p = null;
            if(!ps.isEmpty()) {
                p = ps.get(0);
            }
            return p;
        } finally {
            em.close();
        }
    }
    
}
