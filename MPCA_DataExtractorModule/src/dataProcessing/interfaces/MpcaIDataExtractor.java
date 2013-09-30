/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.interfaces;

import model.controllers.exceptions.PreexistingEntityException;
import dataProcessing.exceptions.MpcaCommentsExtractorNotFoundException;
import java.io.IOException;

/**
 *
 * @author Antonio
 */
public interface MpcaIDataExtractor {
    void updateFromFiles(String files) 
            throws IOException, MpcaCommentsExtractorNotFoundException, PreexistingEntityException, Exception;
    void updateFromDataBase();
}
