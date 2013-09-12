/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

/**
 *
 * @author Antonio
 */
public class CommentsExtractorNotFoundException extends Exception {

    public CommentsExtractorNotFoundException() {
        super("Extractor not found.");
    }
    
    public CommentsExtractorNotFoundException(String msg) {
        super("Extractor not found for: " + msg + ".");
    }
    
}
