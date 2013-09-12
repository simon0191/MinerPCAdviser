/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencemodule;

import controllers.ProductJpaController;
import controllers.WebPageJpaController;
import entities.Brand;
import entities.Product;
import entities.ProductPK;
import entities.WebPage;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Antonio
 */
public class PersistenceModule {
    
    private static EntityManagerFactory em = null;
    
    public static EntityManagerFactory getEntityManagerFactoryInstance() {
        if(em == null) {
            em = Persistence.createEntityManagerFactory("PersistenceModulePU");
        }
        return em;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        em = Persistence.createEntityManagerFactory("PersistenceModulePU");
        WebPageJpaController wpc = new WebPageJpaController(em);
        //        ProductJpaController pc = new ProductJpaController(em);
        //
        //        WebPage wp;
        //        /*wp = new WebPage("Amazon", "www.amazon.com");
        //        wpc.create(wp);
        //        */
        //        wp = new WebPage(BigDecimal.ONE);
        //        Brand brand = new Brand(BigDecimal.ONE);
        //        Product p = pc.findProduct(new ProductPK(BigInteger.ONE, BigInteger.ONE));
        //        System.out.println(p);
        List<WebPage> webPages = wpc.findWebPageEntities();
        for (WebPage webPage : webPages) {
            System.out.println(webPage.getPageName());
        }
    }
}
