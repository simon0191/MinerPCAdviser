/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.sentimentAnalysis.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Antonio
 */
public class MpcaDataSetDirectory {
    private Map<String, MpcaDataSet> directory;

    public MpcaDataSetDirectory() {
        this.directory = new HashMap<String, MpcaDataSet>();
    }
    
    public MpcaDataSet getOrCreate(String file) throws FileNotFoundException, IOException {
        MpcaDataSet dataSet;
        if(directory.containsKey(file)) {
            dataSet = directory.get(file);
        } else {
            dataSet = MpcaDataSet.createDataSet(new File(file));
            directory.put(file, dataSet);
        }
        return dataSet;
    }
}
