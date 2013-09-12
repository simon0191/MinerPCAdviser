/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractormodule;

import dataextractor.MPCA_DataExtractor;
import interfaces.IDataExtractor;

/**
 *
 * @author Antonio
 */
public class DataExtractorModule {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        IDataExtractor de = MPCA_DataExtractor.getInstance();
        de.updateFromFiles("files.descriptor");
    }
}
