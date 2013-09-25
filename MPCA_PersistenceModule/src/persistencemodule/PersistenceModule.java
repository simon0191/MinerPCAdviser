/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencemodule;

import controllers.MpcaWebPageJpaController;
import entities.MpcaWebPage;

/**
 *
 * @author Antonio
 */
public class PersistenceModule {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        /*MpcaCommentJpaController cac = new MpcaCommentJpaController();
        List<MpcaComment> ff = cac.findMpcaCommentByValueAndAddition("POSITIVE", "polarity");
        int i = 0;
        for (MpcaComment comment : ff) {
            System.out.println(comment);
            i++;
            if(i >= 10) break;
        }*/
        //MpcaAdditionCategoryJpaController aa = new MpcaAdditionCategoryJpaController();
        MpcaWebPage wp = new MpcaWebPage("aja", "www.aja.com");
        MpcaWebPageJpaController wpc = new MpcaWebPageJpaController();
        wpc.create(wp);
    }
}
