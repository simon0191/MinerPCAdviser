/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package exceptions;

import java.io.IOException;

/**
 *
 * @author Antonio
 */
public class UnrecognizedExtensionException extends IOException {

    public UnrecognizedExtensionException() {
        super("Invalid extension used.");
    }

    public UnrecognizedExtensionException(String string) {
        super("Invalid extension \"" + string + "\" used.");
    }
    
}
