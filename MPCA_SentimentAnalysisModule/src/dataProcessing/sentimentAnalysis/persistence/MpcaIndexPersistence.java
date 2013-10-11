/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.persistence;

import dataProcessing.sentimentAnalysis.MpcaClassification;
import dataProcessing.sentimentAnalysis.MpcaIClassifier;
import dataProcessing.sentimentAnalysis.MpcaLingPipeClassifier;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.controllers.MpcaCommentIndexJpaController;
import model.controllers.MpcaCommentJpaController;
import model.controllers.MpcaIndexTypeJpaController;
import model.controllers.MpcaLabelTypeJpaController;
import model.controllers.exceptions.PreexistingEntityException;
import model.entities.MpcaComment;
import model.entities.MpcaCommentIndex;
import model.entities.MpcaIndexType;
import model.entities.MpcaLabelType;

/**
 *
 * @author Antonio
 */
public class MpcaIndexPersistence {
    
    /*TODO: Persistencia de indices
     *  - Ponerle el nombre al Clasificador y ponerlo en la BD
     */
    
    public static void persistIndex(MpcaIClassifier classifier, String classifierName, boolean override) throws Exception {
        MpcaCommentJpaController cc = new MpcaCommentJpaController();
        MpcaCommentIndexJpaController cic = new MpcaCommentIndexJpaController();
        MpcaIndexType indexType = createOrGetIndexType(classifierName);
        if(override) {
            cic.deleteByIndexType(indexType);    
        }       

        Map<String, MpcaLabelType> labels = new HashMap<String, MpcaLabelType>();
        System.out.println("Retrieving all comments");
        List<MpcaComment> comments = cc.findMpcaCommentEntities();
        System.out.println("Classifying comments");
        for (MpcaComment comment : comments) {
            System.out.println(comment.getCommentId());
            //String polarity = classifier.bestMatch(comment.getCommentText());
            MpcaClassification classifications = classifier.classify(comment.getCommentText());
            for (Map.Entry<String, Double> entry : classifications.entrySet()) {
                MpcaLabelType label;
                if(!labels.containsKey(entry.getKey())) {
                    label = createOrGetLabelType(entry.getKey());
                    labels.put(entry.getKey(), label);
                } else {
                    label = labels.get(entry.getKey());
                }
                MpcaCommentIndex commentIndex = new MpcaCommentIndex();
                commentIndex.setMpcaComment(comment);
                commentIndex.setMpcaIndexType(indexType);
                commentIndex.setLabelId(label);
                commentIndex.setIndexValue(new BigDecimal(entry.getValue()));
                cic.create(commentIndex);
            }
        }
    }

    private static MpcaIndexType createOrGetIndexType(String indexName) throws PreexistingEntityException, Exception {
        MpcaIndexTypeJpaController itc = new MpcaIndexTypeJpaController();
        MpcaIndexType indexType = itc.findMpcaIndexTypeByName(indexName);
        if(indexType == null) {
            indexType = new MpcaIndexType();
            indexType.setIndexName(indexName);
            itc.create(indexType);
        }
        return indexType;
    }

    private static MpcaLabelType createOrGetLabelType(String labelName) throws PreexistingEntityException, Exception {
        MpcaLabelTypeJpaController ltc = new MpcaLabelTypeJpaController();
        MpcaLabelType labelType = ltc.findMpcaLabelTypeByName(labelName);
        if(labelType == null) {
            labelType = new MpcaLabelType();
            labelType.setLabelName(labelName);
            ltc.create(labelType);
        }
        return labelType;
    }
}
