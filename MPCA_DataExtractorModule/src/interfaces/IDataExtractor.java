/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import controllers.exceptions.PreexistingEntityException;
import exceptions.CommentsExtractorNotFoundException;
import java.io.IOException;

/**
 *
 * @author Antonio
 */
public interface IDataExtractor {
    void updateFromFiles(String files) 
            throws IOException, CommentsExtractorNotFoundException, PreexistingEntityException, Exception;
    void updateFromDataBase();
}
