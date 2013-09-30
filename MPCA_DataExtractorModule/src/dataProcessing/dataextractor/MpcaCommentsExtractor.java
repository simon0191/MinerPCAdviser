/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.dataextractor;

import java.util.List;
import dataProcessing.model.MpcaCommentModel;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public abstract class MpcaCommentsExtractor {
    public abstract List<MpcaCommentModel> commentExtractor(Element comments);
}
