/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencemodule;

import controllers.AdditionJpaController;
import controllers.CommentAdditionJpaController;
import controllers.MpcaCommentJpaController;
import controllers.WebPageJpaController;
import entities.Addition;
import entities.CommentAddition;
import entities.MpcaComment;
import entities.WebPage;
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
        MpcaCommentJpaController cac = new MpcaCommentJpaController();
        List<MpcaComment> ff = cac.findMpcaCommentByValueAndAddition("POSITIVE", "polarity");
        int i = 0;
        for (MpcaComment comment : ff) {
            System.out.println(comment);
            i++;
            if(i >= 10) break;
        }
    }
}
