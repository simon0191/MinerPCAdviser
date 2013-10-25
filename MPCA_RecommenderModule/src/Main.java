
import java.io.InputStreamReader;
import java.util.List;
import java.util.Scanner;
import model.controllers.MpcaAdditionTypeJpaController;
import model.controllers.MpcaProductAdditionJpaController;
import model.entities.MpcaAdditionType;
import model.entities.MpcaProductAddition;
import model.utils.MpcaIConstants;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SimonXPS
 */
public class Main {
    public static void main(String[] args) {
        MpcaAdditionTypeJpaController patc = new MpcaAdditionTypeJpaController();
        List<MpcaAdditionType> types = patc.findByAddCategory(MpcaIConstants.PRODUCT_TAG);
        int i = 0;
        for (MpcaAdditionType add : types) {
            System.out.println((i++)+": "+add.getAddType());
        }
        Scanner s = new Scanner(new InputStreamReader(System.in));
        int addTypePos = s.nextInt();
        System.out.println(addTypePos+": "+types.get(addTypePos).getAddType());
        MpcaProductAdditionJpaController pac = new MpcaProductAdditionJpaController();
        
        List<MpcaProductAddition> adds = pac.findByType(types.get(addTypePos).getAddType());
        int j = 0;
        for (MpcaProductAddition add : adds) {
            System.out.println((j++)+": "+add.getValue());
        }
        int addId = s.nextInt();
        
            
        
    }
    
}
