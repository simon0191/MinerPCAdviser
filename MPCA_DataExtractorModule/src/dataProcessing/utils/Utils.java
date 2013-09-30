/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.utils;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Antonio
 */
public class Utils {
    public static String gregorianCalendarToString(GregorianCalendar date) {
        String commentPosted = date.get(Calendar.DAY_OF_MONTH) 
                + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.YEAR);
        return commentPosted;
    }
}
