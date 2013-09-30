/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.exceptions;

import java.io.IOException;

/**
 *
 * @author Antonio
 */
public class MpcaUnrecognizedExtensionException extends IOException {

    public MpcaUnrecognizedExtensionException() {
        super("Invalid extension used.");
    }

    public MpcaUnrecognizedExtensionException(String string) {
        super("Invalid extension \"" + string + "\" used.");
    }
    
}
