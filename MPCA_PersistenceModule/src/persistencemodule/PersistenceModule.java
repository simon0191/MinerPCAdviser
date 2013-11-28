/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package persistencemodule;

import java.math.BigDecimal;
import java.util.List;
import model.controllers.MpcaAdditionCategoryJpaController;
import model.controllers.MpcaProductJpaController;
import model.controllers.exceptions.PreexistingEntityException;
import model.entities.MpcaAdditionCategory;
import model.entities.MpcaComment;
import model.entities.MpcaCommentAddition;
import model.entities.MpcaProduct;
import model.entities.MpcaProductIndex;
import model.utils.MpcaIConstants;

/**
 *
 * @author Antonio
 */
public class PersistenceModule {
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        List<MpcaProduct> products = new MpcaProductJpaController().findMpcaProductEntities();
        for (MpcaProduct p : products) {
            List<MpcaComment> comments = p.getMpcaCommentList();
            double ave = 0;
            int size = comments.size();
            for (MpcaComment c : comments) {
                for (MpcaCommentAddition ca : c.getMpcaCommentAdditionList()) {
                    if(ca.getMpcaAdditionType().getAddType().equals(MpcaIConstants.ADDITION_RANK)) {
                        ave += Double.parseDouble(ca.getValue());
                        break;
                    }
                }
            }
            System.out.println("Ave = " + ave / size);
            for (MpcaProductIndex pi : p.getMpcaProductIndexList()) {
                if(pi.getMpcaIndexTypeIndexId().getIndexId() == 4 && pi.getLabelId().getLabelName().equals(MpcaIConstants.ADDITION_POSITIVE.toString())) {
                    double iv = pi.getIndexValue().doubleValue();
                    System.out.println("Index = " + (iv/2*10));
                }
            }
        }
    }
    
    private static MpcaAdditionCategory createOrGetCategory(String category) throws PreexistingEntityException, Exception {
        MpcaAdditionCategoryJpaController macc = new MpcaAdditionCategoryJpaController();
        MpcaAdditionCategory mac = macc.findMpcaAdditionCategoryByName(category);
        if(mac == null) {
            mac = new MpcaAdditionCategory();
            mac.setName(category);
            macc.create(mac);
            mac = macc.findMpcaAdditionCategoryByName(category);
            macc.getMpcaAdditionCategoryCount();
        }
        return mac;
    }
}
