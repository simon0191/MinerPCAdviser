/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import controllers.exceptions.IllegalOrphanException;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import entities.Author;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.MpcaComment;
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
public class AuthorJpaController implements Serializable {

    public AuthorJpaController() {
        this.emf = PersistenceModule.getEntityManagerFactoryInstance();
    }
    public AuthorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Author author) throws PreexistingEntityException, Exception {
        if (author.getMpcaCommentList() == null) {
            author.setMpcaCommentList(new ArrayList<MpcaComment>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<MpcaComment> attachedMpcaCommentList = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListMpcaCommentToAttach : author.getMpcaCommentList()) {
                mpcaCommentListMpcaCommentToAttach = em.getReference(mpcaCommentListMpcaCommentToAttach.getClass(), mpcaCommentListMpcaCommentToAttach.getMpcaCommentPK());
                attachedMpcaCommentList.add(mpcaCommentListMpcaCommentToAttach);
            }
            author.setMpcaCommentList(attachedMpcaCommentList);
            em.persist(author);
            for (MpcaComment mpcaCommentListMpcaComment : author.getMpcaCommentList()) {
                Author oldAuthorOfMpcaCommentListMpcaComment = mpcaCommentListMpcaComment.getAuthor();
                mpcaCommentListMpcaComment.setAuthor(author);
                mpcaCommentListMpcaComment = em.merge(mpcaCommentListMpcaComment);
                if (oldAuthorOfMpcaCommentListMpcaComment != null) {
                    oldAuthorOfMpcaCommentListMpcaComment.getMpcaCommentList().remove(mpcaCommentListMpcaComment);
                    oldAuthorOfMpcaCommentListMpcaComment = em.merge(oldAuthorOfMpcaCommentListMpcaComment);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAuthor(author.getAuthorId()) != null) {
                throw new PreexistingEntityException("Author " + author + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Author author) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Author persistentAuthor = em.find(Author.class, author.getAuthorId());
            List<MpcaComment> mpcaCommentListOld = persistentAuthor.getMpcaCommentList();
            List<MpcaComment> mpcaCommentListNew = author.getMpcaCommentList();
            List<String> illegalOrphanMessages = null;
            for (MpcaComment mpcaCommentListOldMpcaComment : mpcaCommentListOld) {
                if (!mpcaCommentListNew.contains(mpcaCommentListOldMpcaComment)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain MpcaComment " + mpcaCommentListOldMpcaComment + " since its author field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<MpcaComment> attachedMpcaCommentListNew = new ArrayList<MpcaComment>();
            for (MpcaComment mpcaCommentListNewMpcaCommentToAttach : mpcaCommentListNew) {
                mpcaCommentListNewMpcaCommentToAttach = em.getReference(mpcaCommentListNewMpcaCommentToAttach.getClass(), mpcaCommentListNewMpcaCommentToAttach.getMpcaCommentPK());
                attachedMpcaCommentListNew.add(mpcaCommentListNewMpcaCommentToAttach);
            }
            mpcaCommentListNew = attachedMpcaCommentListNew;
            author.setMpcaCommentList(mpcaCommentListNew);
            author = em.merge(author);
            for (MpcaComment mpcaCommentListNewMpcaComment : mpcaCommentListNew) {
                if (!mpcaCommentListOld.contains(mpcaCommentListNewMpcaComment)) {
                    Author oldAuthorOfMpcaCommentListNewMpcaComment = mpcaCommentListNewMpcaComment.getAuthor();
                    mpcaCommentListNewMpcaComment.setAuthor(author);
                    mpcaCommentListNewMpcaComment = em.merge(mpcaCommentListNewMpcaComment);
                    if (oldAuthorOfMpcaCommentListNewMpcaComment != null && !oldAuthorOfMpcaCommentListNewMpcaComment.equals(author)) {
                        oldAuthorOfMpcaCommentListNewMpcaComment.getMpcaCommentList().remove(mpcaCommentListNewMpcaComment);
                        oldAuthorOfMpcaCommentListNewMpcaComment = em.merge(oldAuthorOfMpcaCommentListNewMpcaComment);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = author.getAuthorId();
                if (findAuthor(id) == null) {
                    throw new NonexistentEntityException("The author with id " + id + " no longer exists.");
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
            Author author;
            try {
                author = em.getReference(Author.class, id);
                author.getAuthorId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The author with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<MpcaComment> mpcaCommentListOrphanCheck = author.getMpcaCommentList();
            for (MpcaComment mpcaCommentListOrphanCheckMpcaComment : mpcaCommentListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Author (" + author + ") cannot be destroyed since the MpcaComment " + mpcaCommentListOrphanCheckMpcaComment + " in its mpcaCommentList field has a non-nullable author field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(author);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Author> findAuthorEntities() {
        return findAuthorEntities(true, -1, -1);
    }

    public List<Author> findAuthorEntities(int maxResults, int firstResult) {
        return findAuthorEntities(false, maxResults, firstResult);
    }

    private List<Author> findAuthorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Author.class));
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

    public Author findAuthor(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Author.class, id);
        } finally {
            em.close();
        }
    }

    public int getAuthorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Author> rt = cq.from(Author.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Author findAuthorByName(String name) {
        EntityManager em = getEntityManager();
        Query q = em.createNamedQuery("Author.findByAuthorName");
        q.setParameter("authorName", name);
        Author a = null;
        List<Author> as = q.getResultList();
        if(!as.isEmpty()) {
            a = as.get(0);
        }
        return a;
    }
    
}
