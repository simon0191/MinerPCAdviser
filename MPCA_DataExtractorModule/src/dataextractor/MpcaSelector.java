/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

/**
 *
 * @author simon
 */
public class MpcaSelector {
    private String selector;
    private int childNumber;

    public MpcaSelector(String selector) {
        this.selector = selector;
        this.childNumber = 0;
    }
    
    public MpcaSelector(String selector, int childNumber) {
        this.selector = selector;
        this.childNumber = childNumber;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }

    public int getChildNumber() {
        return childNumber;
    }

    public void setChildNumber(int childNumber) {
        this.childNumber = childNumber;
    }
    
    
}
