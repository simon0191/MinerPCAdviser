/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.util.List;
import model.MpcaCommentModel;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public interface IMpcaCommentsExtractor {
    public List<MpcaCommentModel> commentExtractor(Element comments);
    public String brandExtractor(Element brandEle);
    public String modelExtractor(Element modelEle);
}
