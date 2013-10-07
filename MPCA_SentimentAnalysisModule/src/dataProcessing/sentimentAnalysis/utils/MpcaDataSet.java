/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.controllers.JpaController;
import model.entities.MpcaComment;

/**
 *
 * @author Antonio
 */
public class MpcaDataSet extends HashMap<String, List<String>> {

    private MpcaDataSet() {
        super();
    }
    
    public static MpcaDataSet createDataSet(File file) throws FileNotFoundException, IOException {
        MpcaDataSet dataSet;
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            int numberOfCategories = Integer.parseInt(bf.readLine());
            JpaController jpaController = new JpaController();
            EntityManager em = jpaController.getEntityManager();
            dataSet = new MpcaDataSet();
            for (int i = 0; i < numberOfCategories; ++i) {
                String[] line = bf.readLine().split(" +");
                String label = line[0];
                int numberOfQueries = Integer.parseInt(line[1]);
                if (!dataSet.containsKey(label)) {
                    dataSet.put(label, new ArrayList<String>());
                }

                List<MpcaComment> comments = new ArrayList<MpcaComment>();
                for (int j = 0; j < numberOfQueries; ++j) {
                    int maxResults = Integer.parseInt(bf.readLine());
                    int firstResult = Integer.parseInt(bf.readLine());

                    String query = bf.readLine();
                    Query q = em.createQuery(query);
                    if (maxResults > 0) {
                        q.setMaxResults(maxResults);
                    }
                    if (firstResult > 0) {
                        q.setFirstResult(firstResult);
                    }
                    comments = q.getResultList();
                    for (MpcaComment mc : comments) {
                        dataSet.get(label).add(mc.getCommentText());
                    }
                }

            }
        }
        return dataSet;
    }
}
