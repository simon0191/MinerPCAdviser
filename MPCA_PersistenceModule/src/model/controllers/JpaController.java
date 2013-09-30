/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
    
}
