
import controllers.MpcaCommentJpaController;
import entities.MpcaComment;
import java.util.List;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author simon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        MpcaCommentJpaController sumama = new MpcaCommentJpaController();
        List<MpcaComment> comments = sumama.findMpcaCommentEntities();
        for(int i = 0;i<10;++i) {
            System.out.println(comments.toString());
        }
    }
}
