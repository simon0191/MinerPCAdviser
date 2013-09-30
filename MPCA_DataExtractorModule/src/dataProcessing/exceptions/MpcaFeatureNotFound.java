/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.exceptions;

/**
 *
 * @author Antonio
 */
public class MpcaFeatureNotFound extends Exception {

    public MpcaFeatureNotFound() {
        super("Feature Not Found");
    }
    
    public MpcaFeatureNotFound(String feature) {
        super("Feature " + feature + " Not Found");
    }
    
}
