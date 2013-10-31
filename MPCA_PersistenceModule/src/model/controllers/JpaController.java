/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.controllers;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import model.entities.MpcaComment;

/**
 *
 * @author Antonio
 */
public class JpaController {
    
    protected static EntityManagerFactory emf = null;
    
    public static EntityManagerFactory getEntityManagerFactoryInstance() {
        if(emf == null) {
            emf = Persistence.createEntityManagerFactory("MPCA_PersistenceModulePU");
        }
        return emf;
    }

    public JpaController() {
        this.emf = getEntityManagerFactoryInstance();
    }
    
    public JpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
    
    public static List doQuery(String query, int maxResults, int firstResult) {  
        JpaController jpaController = new JpaController();
        EntityManager em = jpaController.getEntityManager();
        Query q = em.createQuery(query);
        if (maxResults > 0) {
            q.setMaxResults(maxResults);
        }
        if (firstResult > 0) {
            q.setFirstResult(firstResult);
        }
        return q.getResultList();
    }
    
}
