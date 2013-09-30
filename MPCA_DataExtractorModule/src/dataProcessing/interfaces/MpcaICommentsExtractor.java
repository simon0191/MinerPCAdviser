/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.interfaces;

import java.util.List;
import dataProcessing.model.MpcaCommentModel;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public interface MpcaICommentsExtractor {
    public List<MpcaCommentModel> commentExtractor(Element comments);
    public String brandExtractor(Element brandEle);
    public String modelExtractor(Element modelEle);
}
