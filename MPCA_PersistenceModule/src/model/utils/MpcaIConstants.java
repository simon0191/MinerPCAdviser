/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.utils;

import java.util.GregorianCalendar;

/**
 *
 * @author Antonio
 */
public interface MpcaIConstants {
    public static final String URL_EXTENSION = ".urls";
    public static final String DESCRIPTOR_EXTENSION = ".descriptor";
    public static final String BRAND_TAG = "brand";
    public static final String MODEL_TAG = "model";
    public static final String HARD_DRIVE_TAG = "HD";
    public static final String RAM_TAG = "ram";
    public static final String TOTAL_PAGES_TAG = "totalPages";
    public static final String NEXT_PAGE_TAG = "nextPage";
    public static final String COMMENTS_TAG = "comments";
    public static final String PRODUCT_TAG = "product";
    public static final String AUTHOR_TAG = "author";
    public static final String TIGERDIRECT_NAME = "tigerdirect";
    public static final String AMAZON_NAME = "amazon";
    public static final String NEWEGG_NAME = "newegg";
    public static final String NA_AUTHOR = "N/A";
    public static final String NO_TITLE = "N/T";
    public static final String TAG_TITLE = "title";
    public static final String ADDITION_RANK = "rank";
    public static final String ADDITION_POLARITY = "polarity";
    public static final MpcaPolarity ADDITION_POSITIVE = MpcaPolarity.POSITIVE;
    public static final MpcaPolarity ADDITION_NEGATIVE = MpcaPolarity.NEGATIVE;
    public static final MpcaPolarity ADDITION_NEUTRAL = MpcaPolarity.NEUTRAL;
    public static final GregorianCalendar GREGORIAN_BASE = new GregorianCalendar(1945, 5, 6);
    public static final String SEPARATOR = "------------------------------------------------------------------------------------------";
    public static final String CLASSIFIERS_DESCRIPTOR_PATH = "data/classifiers";
    public static final String FILTERS_DESCRIPTOR_PATH = "data/filters";
    public static final String RECOMMENDER_DESCRIPTOR_PATH = "data/recommender";
    public static final String MAX_RESULTS_TAG = "maxResults";
    public static final String OFFSET_TAG = "offset";
    public static final String LING_PIPE = "LING_PIPE";
    public static final String FILTER_DESCRIPTOR_FILE = "filters_descriptor.xml";
    public static final String RECOMMENDER_DESCRIPTOR_FILE = "recommender_descriptor.xml";
}
