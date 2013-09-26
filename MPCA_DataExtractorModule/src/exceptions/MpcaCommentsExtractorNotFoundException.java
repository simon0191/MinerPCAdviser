/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Antonio
 */
public class MpcaCommentsExtractorNotFoundException extends Exception {

    public MpcaCommentsExtractorNotFoundException() {
        super("Extractor not found.");
    }
    
    public MpcaCommentsExtractorNotFoundException(String msg) {
        super("Extractor not found for: " + msg + ".");
    }
    
}
