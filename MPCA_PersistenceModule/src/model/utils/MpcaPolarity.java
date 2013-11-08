/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Antonio
 */
public enum MpcaPolarity {
    
    POSITIVE,
    NEGATIVE,
    NEUTRAL;
    private final static Map<String, MpcaPolarity> polarities = new HashMap<String, MpcaPolarity>();
    static {
        polarities.put("POSITIVE", POSITIVE);
        polarities.put("NEGATIVE", NEGATIVE);
        polarities.put("NEUTRAL", NEUTRAL);
    }
    public static MpcaPolarity getPolarity(String polarity) {
        return polarities.get(polarity);
    }
}
