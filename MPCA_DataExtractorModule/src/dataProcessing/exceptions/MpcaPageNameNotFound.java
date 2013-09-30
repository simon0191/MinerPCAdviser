package dataProcessing.exceptions;

/**
 *
 * @author Antonio
 */
public class MpcaPageNameNotFound extends Exception {

    public MpcaPageNameNotFound() {
        super("Page Name Not Found");
    }
    
    public MpcaPageNameNotFound(String pageName) {
        super("Page '" + pageName + "' Not Found");
    }
    
}
