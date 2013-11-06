package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import model.MpcaFilterProperties;
import interfaces.MpcaIFilterProperties;
import model.MpcaProperty;
import model.controllers.MpcaProductJpaController;
import model.entities.MpcaProduct;
import model.entities.MpcaProductAddition;
import model.utils.MpcaIConstants;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author MinerPCAdviser
 */
public class MpcaFilterFeature {

    public static MpcaIFilterProperties getFilterProperties(boolean technicalFilter) throws IOException {
        MpcaFilterProperties filterProperties;
        if(technicalFilter) {
            filterProperties = getPhysicalFilterProperties(null);
        } else { // Subjective Filter
            filterProperties = null;
        }
        return filterProperties;
    }

    public static MpcaIFilterProperties getFilterProperties(MpcaIFilterProperties filtersSelected, boolean technicalFilter) throws IOException {
        MpcaIFilterProperties filterProperties;
        if(technicalFilter) {
            filterProperties = getPhysicalFilterProperties(filtersSelected);
        } else {
            filterProperties = null;
        }
        return filterProperties;
    }
    
    private static MpcaFilterProperties getPhysicalFilterProperties(MpcaIFilterProperties filtersSelected) throws IOException {
        MpcaFilterProperties properties = new MpcaFilterProperties();
        Set<MpcaProduct> products = new TreeSet<MpcaProduct>(applyFilter(filtersSelected));
        properties.setProducts(products);
        
        // Extrae los demás filtros establecidos en el XML
        // y crea los filtros que faltaban a partir de la lista de productos
        // filtrados
        List<MpcaProperty> keysNames = extractPhysicalFilters();
        for (MpcaProperty keyName : keysNames) {
            String property = keyName.getPropertyKey();
            String name = keyName.getPropertyName();
            if(filtersSelected == null || !filtersSelected.containsProperty(property)) {
                Set<String> values = new TreeSet<String>();
                for (MpcaProduct p : products) {
                    for (MpcaProductAddition pa : p.getMpcaProductAdditionList()) {
                        if(pa.getMpcaAdditionType().getAddType().equals(property)) {
                            values.add(pa.getValue());
                        }
                    }
                }
                properties.addProperty(property, name, values);
            } else {
                properties.addProperty(property, name, filtersSelected.getProperty(property).getPropertyValues());
            }
        }
        return properties;
    }
    
    private static List<MpcaProduct> applyFilter(MpcaIFilterProperties filtersSelected) {
        MpcaProductJpaController pc = new MpcaProductJpaController();
        List<MpcaProduct> products = pc.findMpcaProductEntities();
        
        if(filtersSelected != null) {
            Set<String> propertiesFiltered = filtersSelected.getProperties();
            for (String property : propertiesFiltered) {
                Set<String> values = filtersSelected.getProperty(property).getPropertyValues();
                for (String value : values) {
                    products = applyFilter(property, value, products);
                }
            }
        }
        return products;
    }
    
    private static List<MpcaProduct> applyFilter(String property, String value, List<MpcaProduct> products) {
        List<MpcaProduct> productsFiltered = new ArrayList<MpcaProduct>();
        for (MpcaProduct p : products) {
            for (MpcaProductAddition pa : p.getMpcaProductAdditionList()) {
                if(pa.getMpcaAdditionType().getAddType().equals(property) && pa.getValue().equals(value)) {
                    productsFiltered.add(p);
                    break;
                }
            }
        }
        return productsFiltered;
    }

    private static List<MpcaProperty> extractPhysicalFilters() throws IOException {
        Document doc = Jsoup.parse(new File(MpcaIConstants.FILTERS_DESCRIPTOR_PATH, MpcaIConstants.FILTER_DESCRIPTOR_FILE), "UTF-8");
        Element filters = doc.select("technicalFilters").first();
        List<MpcaProperty> keysNames = new ArrayList<MpcaProperty>();
        for (Element filter : filters.select("filter")) {
            String key = filter.id();
            String name = filter.attr("name");
            keysNames.add(new MpcaProperty(key, name, null));
        }
        return keysNames;
    }
    
}
