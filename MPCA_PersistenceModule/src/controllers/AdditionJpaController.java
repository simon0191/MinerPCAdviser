/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Addition;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.ProductAddition;
import java.util.ArrayList;
import java.util.List;
import entities.CommentAddition;
import java.math.BigDecimal;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import persistencemodule.PersistenceModule;

/**
 *
 * @author Antonio
 */
public class AdditionJpaController implements Serializable {

    public AdditionJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public AdditionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Addition addition) throws PreexistingEntityException, Exception {
        if (addition.getProductAdditionList() == null) {
            addition.setProductAdditionList(new ArrayList<ProductAddition>());
        }
        if (addition.getCommentAdditionList() == null) {
            addition.setCommentAdditionList(new ArrayList<CommentAddition>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<ProductAddition> attachedProductAdditionList = new ArrayList<ProductAddition>();
            for (ProductAddition productAdditionListProductAdditionToAttach : addition.getProductAdditionList()) {
                productAdditionListProductAdditionToAttach = em.getReference(productAdditionListProductAdditionToAttach.getClass(), productAdditionListProductAdditionToAttach.getProductAdditionPK());
                attachedProductAdditionList.add(productAdditionListProductAdditionToAttach);
            }
            addition.setProductAdditionList(attachedProductAdditionList);
            List<CommentAddition> attachedCommentAdditionList = new ArrayList<CommentAddition>();
            for (CommentAddition commentAdditionListCommentAdditionToAttach : addition.getCommentAdditionList()) {
                commentAdditionListCommentAdditionToAttach = em.getReference(commentAdditionListCommentAdditionToAttach.getClass(), commentAdditionListCommentAdditionToAttach.getCommentAdditionPK());
                attachedCommentAdditionList.add(commentAdditionListCommentAdditionToAttach);
            }
            addition.setCommentAdditionList(attachedCommentAdditionList);
            em.persist(addition);
            for (ProductAddition productAdditionListProductAddition : addition.getProductAdditionList()) {
                Addition oldAdditionOfProductAdditionListProductAddition = productAdditionListProductAddition.getAddition();
                productAdditionListProductAddition.setAddition(addition);
                productAdditionListProductAddition = em.merge(productAdditionListProductAddition);
                if (oldAdditionOfProductAdditionListProductAddition != null) {
                    oldAdditionOfProductAdditionListProductAddition.getProductAdditionList().remove(productAdditionListProductAddition);
                    oldAdditionOfProductAdditionListProductAddition = em.merge(oldAdditionOfProductAdditionListProductAddition);
                }
            }
            for (CommentAddition commentAdditionListCommentAddition : addition.getCommentAdditionList()) {
                Addition oldAdditionOfCommentAdditionListCommentAddition = commentAdditionListCommentAddition.getAddition();
                commentAdditionListCommentAddition.setAddition(addition);
                commentAdditionListCommentAddition = em.merge(commentAdditionListCommentAddition);
                if (oldAdditionOfCommentAdditionListCommentAddition != null) {
                    oldAdditionOfCommentAdditionListCommentAddition.getCommentAdditionList().remove(commentAdditionListCommentAddition);
                    oldAdditionOfCommentAdditionListCommentAddition = em.merge(oldAdditionOfCommentAdditionListCommentAddition);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAddition(addition.getAddId()) != null) {
                throw new PreexistingEntityException("Addition " + addition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Addition addition) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Addition persistentAddition = em.find(Addition.class, addition.getAddId());
            List<ProductAddition> productAdditionListOld = persistentAddition.getProductAdditionList();
            List<ProductAddition> productAdditionListNew = addition.getProductAdditionList();
            List<CommentAddition> commentAdditionListOld = persistentAddition.getCommentAdditionList();
            List<CommentAddition> commentAdditionListNew = addition.getCommentAdditionList();
            List<String> illegalOrphanMessages = null;
            for (ProductAddition productAdditionListOldProductAddition : productAdditionListOld) {
                if (!productAdditionListNew.contains(productAdditionListOldProductAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ProductAddition " + productAdditionListOldProductAddition + " since its addition field is not nullable.");
                }
            }
            for (CommentAddition commentAdditionListOldCommentAddition : commentAdditionListOld) {
                if (!commentAdditionListNew.contains(commentAdditionListOldCommentAddition)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain CommentAddition " + commentAdditionListOldCommentAddition + " since its addition field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<ProductAddition> attachedProductAdditionListNew = new ArrayList<ProductAddition>();
            for (ProductAddition productAdditionListNewProductAdditionToAttach : productAdditionListNew) {
                productAdditionListNewProductAdditionToAttach = em.getReference(productAdditionListNewProductAdditionToAttach.getClass(), productAdditionListNewProductAdditionToAttach.getProductAdditionPK());
                attachedProductAdditionListNew.add(productAdditionListNewProductAdditionToAttach);
            }
            productAdditionListNew = attachedProductAdditionListNew;
            addition.setProductAdditionList(productAdditionListNew);
            List<CommentAddition> attachedCommentAdditionListNew = new ArrayList<CommentAddition>();
            for (CommentAddition commentAdditionListNewCommentAdditionToAttach : commentAdditionListNew) {
                commentAdditionListNewCommentAdditionToAttach = em.getReference(commentAdditionListNewCommentAdditionToAttach.getClass(), commentAdditionListNewCommentAdditionToAttach.getCommentAdditionPK());
                attachedCommentAdditionListNew.add(commentAdditionListNewCommentAdditionToAttach);
            }
            commentAdditionListNew = attachedCommentAdditionListNew;
            addition.setCommentAdditionList(commentAdditionListNew);
            addition = em.merge(addition);
            for (ProductAddition productAdditionListNewProductAddition : productAdditionListNew) {
                if (!productAdditionListOld.contains(productAdditionListNewProductAddition)) {
                    Addition oldAdditionOfProductAdditionListNewProductAddition = productAdditionListNewProductAddition.getAddition();
                    productAdditionListNewProductAddition.setAddition(addition);
                    productAdditionListNewProductAddition = em.merge(productAdditionListNewProductAddition);
                    if (oldAdditionOfProductAdditionListNewProductAddition != null && !oldAdditionOfProductAdditionListNewProductAddition.equals(addition)) {
                        oldAdditionOfProductAdditionListNewProductAddition.getProductAdditionList().remove(productAdditionListNewProductAddition);
                        oldAdditionOfProductAdditionListNewProductAddition = em.merge(oldAdditionOfProductAdditionListNewProductAddition);
                    }
                }
            }
            for (CommentAddition commentAdditionListNewCommentAddition : commentAdditionListNew) {
                if (!commentAdditionListOld.contains(commentAdditionListNewCommentAddition)) {
                    Addition oldAdditionOfCommentAdditionListNewCommentAddition = commentAdditionListNewCommentAddition.getAddition();
                    commentAdditionListNewCommentAddition.setAddition(addition);
                    commentAdditionListNewCommentAddition = em.merge(commentAdditionListNewCommentAddition);
                    if (oldAdditionOfCommentAdditionListNewCommentAddition != null && !oldAdditionOfCommentAdditionListNewCommentAddition.equals(addition)) {
                        oldAdditionOfCommentAdditionListNewCommentAddition.getCommentAdditionList().remove(commentAdditionListNewCommentAddition);
                        oldAdditionOfCommentAdditionListNewCommentAddition = em.merge(oldAdditionOfCommentAdditionListNewCommentAddition);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = addition.getAddId();
                if (findAddition(id) == null) {
                    throw new NonexistentEntityException("The addition with id " + id + " no longer exists.");
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
            Addition addition;
            try {
                addition = em.getReference(Addition.class, id);
                addition.getAddId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The addition with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<ProductAddition> productAdditionListOrphanCheck = addition.getProductAdditionList();
            for (ProductAddition productAdditionListOrphanCheckProductAddition : productAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Addition (" + addition + ") cannot be destroyed since the ProductAddition " + productAdditionListOrphanCheckProductAddition + " in its productAdditionList field has a non-nullable addition field.");
            }
            List<CommentAddition> commentAdditionListOrphanCheck = addition.getCommentAdditionList();
            for (CommentAddition commentAdditionListOrphanCheckCommentAddition : commentAdditionListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Addition (" + addition + ") cannot be destroyed since the CommentAddition " + commentAdditionListOrphanCheckCommentAddition + " in its commentAdditionList field has a non-nullable addition field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(addition);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Addition> findAdditionEntities() {
        return findAdditionEntities(true, -1, -1);
    }

    public List<Addition> findAdditionEntities(int maxResults, int firstResult) {
        return findAdditionEntities(false, maxResults, firstResult);
    }

    private List<Addition> findAdditionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Addition.class));
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

    public Addition findAddition(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Addition.class, id);
        } finally {
            em.close();
        }
    }

    public int getAdditionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Addition> rt = cq.from(Addition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Addition findAdditionByType(String type) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNamedQuery("Addition.findByAddType");
            q.setParameter("addType", type);
            List<Addition> adds = q.getResultList();
            Addition add = null;
            if(!adds.isEmpty()) {
                add = adds.get(0);
            }
            return add;
        } finally {
            em.close();
        }
    }
    
}
