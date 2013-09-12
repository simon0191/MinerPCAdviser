/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import java.util.List;
import model.MPCA_Comment;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public abstract class MPCA_CommentsExtractor {
    public abstract List<MPCA_Comment> commentExtractor(Element comments);
}
