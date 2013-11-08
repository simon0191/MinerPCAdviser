/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recommender;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import model.utils.MpcaPolarity;
import model.utils.Range;

/**
 *
 * @author Antonio
 */
public class MpcaRecommendation implements Comparable<MpcaRecommendation> {
    private String decision;
    private Map<MpcaPolarity, Range> ranges;
    private Integer priority;

    public MpcaRecommendation(String decision, int priority) {
        this.decision = decision;
        this.ranges = new HashMap<MpcaPolarity, Range>();
        this.priority = priority;
    }
    
    public void addPolarity(MpcaPolarity polarity, Range range) {
        ranges.put(polarity, range);
    }

    public String getDecision() {
        return decision;
    }
    
    public Set<MpcaPolarity> getPolarities() {
        return ranges.keySet();
    }
    
    public Range getRange(MpcaPolarity polarity) {
        return ranges.get(polarity);
    }

    public Integer getPriority() {
        return priority;
    }

    @Override
    public int compareTo(MpcaRecommendation t) {
        return priority.compareTo(t.getPriority());
    }
    
}
