/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import utils.Polarity;

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
    
    public static final String URL_EXTENSION = ".urls";
    public static final String DESCRIPTOR_EXTENSION = ".descriptor";
    public static final String BRAND_TAG = "brand";
    public static final String MODEL_TAG = "model";
    public static final String HARD_DRIVE_TAG = "HD";
    public static final String RAM_TAG = "ram";
    public static final String TOTAL_PAGES_TAG = "totalPages";
    public static final String NEXT_PAGE_TAG = "nextPage";
    public static final String COMMENTS_TAG = "comments";
    public static final String PRODUCT_TAG = "product";
    public static final String AUTHOR_TAG = "author";
    public static final String TIGERDIRECT_NAME = "tigerdirect";
    public static final String AMAZON_NAME = "amazon";
    public static final String NEWEGG_NAME = "newegg";
    public static final String NA_AUTHOR = "N/A";
    public static final String ADDITION_TITLE = "title";
    public static final String ADDITION_RANK = "rank";
    public static final String ADDITION_POLARITY = "polarity";
    public static final Polarity ADDITION_POSITIVE = Polarity.POSITIVE;
    public static final Polarity ADDITION_NEGATIVE = Polarity.NEGATIVE;
    public static final Polarity ADDITION_NEUTRAL = Polarity.NEUTRAL;
    
}
