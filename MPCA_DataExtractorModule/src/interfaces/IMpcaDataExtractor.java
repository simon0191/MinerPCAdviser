/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import controllers.exceptions.PreexistingEntityException;
import exceptions.MpcaCommentsExtractorNotFoundException;
import java.io.IOException;

/**
 *
 * @author Antonio
 */
public interface IMpcaDataExtractor {
    void updateFromFiles(String files) 
            throws IOException, MpcaCommentsExtractorNotFoundException, PreexistingEntityException, Exception;
    void updateFromDataBase();
}
