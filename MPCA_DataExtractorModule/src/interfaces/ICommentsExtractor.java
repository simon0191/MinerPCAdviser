/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import model.MPCA_Comment;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public interface ICommentsExtractor {
    List<MPCA_Comment> commentExtractor(Element comments);
}
