/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import dataProcessing.sentimentAnalysis.MpcaProductIndexAnalysis;
import dataProcessing.sentimentAnalysis.MpcaProductResult;
import java.util.List;
import model.controllers.MpcaIndexTypeJpaController;
import model.entities.MpcaIndexType;

/**
 *
 * @author Antonio
 */
public class ProductIndexMain {
    public static void main(String[] args) {
        MpcaIndexType index = new MpcaIndexTypeJpaController().findMpcaIndexType(4l);
        List<MpcaProductResult> results = MpcaProductIndexAnalysis.calculateAllProductsIndex(index);
        for (MpcaProductResult result : results) {
            System.out.println(result);
            System.out.println("================================");
        }
    }
}
