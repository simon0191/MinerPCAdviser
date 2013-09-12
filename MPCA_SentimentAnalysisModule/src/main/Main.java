/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import controllers.MpcaCommentJpaController;
import entities.MpcaComment;
import java.util.List;

/**
 *
 * @author simon
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("***********");
        MpcaCommentJpaController sumama = new MpcaCommentJpaController();
        List<MpcaComment> comments = sumama.findMpcaCommentByValueAndAddition("POSITIVE", "polarity");
        for(int i = 0;i<10;++i) {
            System.out.println(comments.get(i).getAuthor().getAuthorName());
        }
    }
}
