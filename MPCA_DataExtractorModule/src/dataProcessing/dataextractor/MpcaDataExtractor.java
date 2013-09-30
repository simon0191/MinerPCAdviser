/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataProcessing.dataextractor;

import dataProcessing.model.MpcaPageModel;
import dataProcessing.exceptions.MpcaFeatureNotFound;
import dataProcessing.exceptions.MpcaPageNameNotFound;
import model.controllers.JpaController;
import model.controllers.MpcaAdditionCategoryJpaController;
import model.controllers.MpcaAdditionTypeJpaController;
import model.controllers.MpcaCommentAdditionJpaController;
import model.controllers.MpcaCommentJpaController;
import model.controllers.MpcaProductAdditionJpaController;
import model.controllers.MpcaProductJpaController;
import model.controllers.MpcaProductWebPageJpaController;
import model.controllers.MpcaWebPageJpaController;
import model.controllers.exceptions.PreexistingEntityException;
import model.entities.MpcaAdditionCategory;
import model.entities.MpcaAdditionType;
import model.entities.MpcaComment;
import model.entities.MpcaCommentAddition;
import model.entities.MpcaProduct;
import model.entities.MpcaProductAddition;
import model.entities.MpcaProductWebPage;
import model.entities.MpcaWebPage;
import dataProcessing.exceptions.MpcaCommentsExtractorNotFoundException;
import dataProcessing.exceptions.MpcaUnrecognizedExtensionException;
import dataProcessing.interfaces.MpcaICommentsExtractor;
import dataProcessing.interfaces.MpcaIDataExtractor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import dataProcessing.model.MpcaCommentModel;
import dataProcessing.utils.Utils;
import model.utils.MpcaIConstants;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public class MpcaDataExtractor implements MpcaIDataExtractor {
    
    // Singleton
    private static MpcaIDataExtractor dataExtractor = null;
    // Lista de URLs por página
    private Map<String, List<MpcaPageModel>> computersURLs;
    // Grupo de descriptores por Página
    private Map<String, Map<String, List<MpcaSelector>>> descriptors;
    
    private MpcaWebPageJpaController wpc;
    private MpcaCommentJpaController cc;
    private MpcaProductJpaController pc;
    private MpcaProductAdditionJpaController pac;
    private MpcaAdditionTypeJpaController ac;
    private MpcaProductWebPageJpaController pwpc;
    private MpcaCommentAdditionJpaController cac;
    private MpcaAdditionCategoryJpaController macc;
    
    public static MpcaIDataExtractor getInstance() {
        if(dataExtractor == null) {
            dataExtractor = new MpcaDataExtractor();
        }
        return dataExtractor;
    }
    
    private MpcaDataExtractor() {
        computersURLs = new TreeMap<>();
        descriptors = new TreeMap<>();
        wpc = new MpcaWebPageJpaController();
        cc = new MpcaCommentJpaController();
        pc = new MpcaProductJpaController();
        pac = new MpcaProductAdditionJpaController();
        ac = new MpcaAdditionTypeJpaController();
        pwpc = new MpcaProductWebPageJpaController();
        cac = new MpcaCommentAdditionJpaController();
        macc = new MpcaAdditionCategoryJpaController();
    }

    @Override
    public void updateFromFiles(String files) 
            throws IOException, PreexistingEntityException, MpcaCommentsExtractorNotFoundException, Exception {
        StringBuffer fileData = MpcaPageExtractor.readFile(new File(files));
        String []lines = fileData.toString().split("\n");
        
        // Se leen los archivos que contienen los descriptores y URLs para guardarlos en memoria
        extractDescriptorsAndURLs(lines);
        
        Set<String> keySet = descriptors.keySet();
        for (String pageName : keySet) {
            pageName = pageName.trim();
            // Crea o busca el nombre de la página
            MpcaWebPage wp = createOrGetWebPageByName(pageName);
            System.out.println("WebPage created or updated = " + pageName);
            
            List<MpcaPageModel> urls = computersURLs.get(pageName);
            Map<String, List<MpcaSelector>> des = descriptors.get(pageName);
            // Se trasladan los descriptores de los comentarios del antiguo mapa para poder tratar página por página
            Map<String, List<MpcaSelector>> comments = new TreeMap<>();
            comments.put(MpcaIConstants.COMMENTS_TAG, des.remove(MpcaIConstants.COMMENTS_TAG));
            if(des.containsKey(MpcaIConstants.NEXT_PAGE_TAG)) {
                comments.put(MpcaIConstants.NEXT_PAGE_TAG, des.remove(MpcaIConstants.NEXT_PAGE_TAG));
            }
            if(des.containsKey(MpcaIConstants.TOTAL_PAGES_TAG)) {
                comments.put(MpcaIConstants.TOTAL_PAGES_TAG, des.remove(MpcaIConstants.TOTAL_PAGES_TAG));
            }
            for (MpcaPageModel p : urls) {
                System.out.println("Url = " + p.getCommentsPageURL());
                Map<String, Element> data = MpcaPageExtractor.get(p.getMainPageURL().toString(), des);
                List<Element> allComments = extractCommentsFromURL(p.getCommentsPageURL(), comments, pageName);
                
                //String brand = data.get(JpaController.BRAND_TAG).text().trim().toUpperCase();
                String brand = extractFeature(data.get(MpcaIConstants.BRAND_TAG), pageName, MpcaIConstants.BRAND_TAG);
                //String model = data.get(JpaController.MODEL_TAG).text().trim().toUpperCase();
                String model = extractFeature(data.get(MpcaIConstants.MODEL_TAG), pageName, MpcaIConstants.MODEL_TAG);
                
                // Crea o actualiza un producto
                MpcaProduct product = createOrUpdateProduct(model, brand, wp, p, data);
                
                // Extrae los comentarios de los HTMLs y los estructura
                List<MpcaCommentModel> commentsExtracted = extractComments(pageName, allComments);
                
                // Crea o actualiza la base de datos de comentarios
                createOrUpdateComments(product, wp, commentsExtracted);
                
                System.out.println("==================");
            }
        }
    }

    @Override
    public void updateFromDataBase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /**
     * Método para extraer las URLs y los Descriptores de cada página
     * 
     * @param lines
     * @throws IOException
     */
    private void extractDescriptorsAndURLs(String[] lines) throws IOException {
        for (String line : lines) {
            line = line.trim();
            // Nombre de la página
            String page = line.substring(0,line.indexOf("."));
            // Omite comentarios
            if(line.charAt(0) != '#') {
                // Extractor de URLs
                if(line.endsWith(MpcaIConstants.URL_EXTENSION)) {
                    StringBuffer urlData = MpcaPageExtractor.readFile(new File(line.trim()));
                    String []urls = urlData.toString().split("\n+");
                    List<MpcaPageModel> urlPages = new ArrayList<>();
                    for (String urlLine : urls) {
                        if(urlLine.trim().charAt(0) != '#') {
                            String []pages = urlLine.split(" +");
                            MpcaPageModel p;
                            // Una sola URL indica que los comentarios están en esa misma página, de lo contrario,
                            //los comentarios se encuentran en la segunda página
                            if(pages.length == 1) {
                                p = new MpcaPageModel(pages[0].trim());
                            } else {
                                p = new MpcaPageModel(pages[0].trim(), pages[1].trim());
                            }
                            urlPages.add(p);
                        }
                    }
                    computersURLs.put(page, urlPages);

                    // Extractor de selectors
                } else if(line.endsWith(MpcaIConstants.DESCRIPTOR_EXTENSION)) {
                    Map<String, List<MpcaSelector>> parseDescriptor = MpcaPageExtractor.parseDescriptor(new File(line));
                    descriptors.put(page, parseDescriptor);
                } else {
                    throw new MpcaUnrecognizedExtensionException(line.substring(line.indexOf(".")));
                }
            } // fi line.charAt()
        }
    }
    
    private List<Element> extractCommentsFromURL(URL commentsURL, Map<String, 
            List<MpcaSelector>> comments, String pageName) throws IOException, NumberFormatException {
        
        Map<String, Element> commentsData = MpcaPageExtractor.get(commentsURL.toString().trim(), comments);
        
        List<Element> allComments = new ArrayList<>();
        
        if(pageName.equals(MpcaIConstants.NEWEGG_NAME)) {
            Element ele = commentsData.get(MpcaIConstants.TOTAL_PAGES_TAG);
            String totalPagesS = "0";
            if(ele != null) {
                totalPagesS = ele.text().replaceAll("[^0-9]", "");
            }
            int totalPages = Integer.parseInt(totalPagesS);
            System.out.println(totalPages);
            for (int i = 1; i <= totalPages; ++i) {
                String newURL = commentsURL.toString().replaceFirst("Page=" + "[0-9]+", "Page=" + i);
                Map<String, Element> comms = MpcaPageExtractor.get(newURL, comments);
                allComments.add(comms.get(MpcaIConstants.COMMENTS_TAG));
                System.out.println("Page = " + i);
            }
            return allComments;
        }
        
        allComments.add(commentsData.get(MpcaIConstants.COMMENTS_TAG));
        Element ele = commentsData.get(MpcaIConstants.NEXT_PAGE_TAG);
        
        String nextPage = ((ele!=null)?ele.attr("href").trim():"");
        
        String prefix = "";
        if(pageName.equals(MpcaIConstants.TIGERDIRECT_NAME)) {
            prefix = "http://www.tigerdirect.com";
        }
        
        int a = 1;
        while(!nextPage.equals("")) {
            Map<String, Element> comms = MpcaPageExtractor.get(prefix + nextPage, comments);
            allComments.add(comms.get(MpcaIConstants.COMMENTS_TAG));
            ele = comms.get(MpcaIConstants.NEXT_PAGE_TAG);
            if(ele != null && ele.text().toLowerCase().contains("next")) {
                nextPage = ele.attr("href").trim();
            }
            else {
                nextPage = "";
            }
            
            System.out.println("Page = " + (a++));
            //System.out.println("URL = " + nextPage);
        }
        
        return allComments;
    }
    
    /**
     * Método que busca o crea una página web. Si la encuentra, la retorna. Si no la encuentra, la crea y la retorna
     * @param wpc
     * @param biggest
     * @param pageName
     * @return
     * @throws Exception 
     */
    private MpcaWebPage createOrGetWebPageByName(String pageName) throws Exception {
        List<MpcaWebPage> webPages = wpc.findMpcaWebPageEntities();
        MpcaWebPage webPage = null;
        //long biggest = 0;
        for (MpcaWebPage wp : webPages) {
            /*if(biggest < wp.getPageId()) {
                biggest = wp.getPageId();
            }*/
            if(wp.getPageName().equalsIgnoreCase(pageName)) {
                webPage = wp;
                break;
            }
            //biggest++;
        }
        if(webPage == null) {
            webPage = new MpcaWebPage(pageName, "http://www." + pageName + ".com");
            wpc.create(webPage);
            //biggest++;
            //webPage.setPageId(biggest);
        }
        return webPage;
    }
    
    /**
     * Este método crea o actualiza un producto de la base de datos.
     * 
     * Al actualizar el producto, él revisa las adiciones que tiene y las sobrescribe o sino existe la adición, 
     * entonces la crea.
     * 
     * 
     * @param model El modelo del producto
     * @param brand El productor del producto
     * @param wp La página del review del producto
     * @param p La URL del review del producto
     * @param data La información extraida del review
     * @return Retorna el producto nuevo o actualizado
     * @throws Exception 
     */
    private MpcaProduct createOrUpdateProduct(String model, String brand, MpcaWebPage wp, MpcaPageModel p, Map<String, Element> data) throws Exception {
        MpcaProduct product = pc.findProductByModel(model);
        if(product == null) {
            product = new MpcaProduct();
            product.setModel(model);
            pc.create(product);
            //product = pc.findProductByModel(model);
            
            MpcaAdditionType at = createOrGetAddition(MpcaIConstants.BRAND_TAG, MpcaIConstants.PRODUCT_TAG);
            
            MpcaProductAddition pa = new MpcaProductAddition(product.getProductId(),at.getAddId());
            pa.setValue(brand);
            
            System.out.println("Product created");
        }
        boolean find = false;
        for (MpcaProductWebPage productWebPage : product.getMpcaProductWebPageList()) {
            if(productWebPage.getMpcaWebPage().getPageId() == wp.getPageId()) {
                find = true;
            }
        }
        if(!find) {
            createProductWebPage(product, wp, p);
        }
        
        // Se eliminan el modelo y el productor para que no sean contadas como adiciones
        data.remove(MpcaIConstants.BRAND_TAG);
        data.remove(MpcaIConstants.MODEL_TAG);
        
        // Se actualizan las adiciones que el producto ya tenga
        if(product.getMpcaProductAdditionList() != null) {
            for (MpcaProductAddition pa : product.getMpcaProductAdditionList()) {
                MpcaAdditionType add = pa.getMpcaAdditionType();
                if(data.containsKey(add.getAddType()) && data.get(add.getAddType()) != null) {
                    pa.setValue(data.get(add.getAddType()).text().trim());
                    data.remove(add.getAddType());
                    pac.edit(pa);
                }
            }
        }
        // Adiciones que faltan en la tabla principal
        for (Map.Entry<String, Element> es : data.entrySet()) {
            MpcaAdditionType theAdd = createOrGetAddition(es.getKey(), MpcaIConstants.PRODUCT_TAG);
            System.out.println(theAdd);
            // Se agrega la nueva adición al producto con su respectivo valor
            MpcaProductAddition pa = new MpcaProductAddition();
            pa.setMpcaProduct(product);
            pa.setMpcaAdditionType(theAdd);
            if(es.getValue() != null) {
                pa.setValue(es.getValue().text().trim());
                pac.create(pa);
                System.out.println("Product Addition created");
            }
        }
        return pc.findMpcaProduct(product.getProductId());
    }
    
    /**
     * Método que extrae los comentarios de un bloque de HTML para estructurarlo.
     * @param pageName Nombre de la página que va a ser utilizada para encontrar su Extractor respectivo
     * @param allComments Lista de comentarios semiestructurados en HTML
     * @return Retorna una lista de comentarios estructurados en la clase MpcaCommentModel
     * @throws MpcaCommentsExtractorNotFoundException 
     */
    private List<MpcaCommentModel> extractComments(String pageName, List<Element> allComments) throws MpcaCommentsExtractorNotFoundException {
        /* Extracción de comentarios */
        MpcaICommentsExtractor extractor = null;
        switch (pageName) {
            case MpcaIConstants.AMAZON_NAME:
                extractor = MpcaAmazonCommentsExtractor.getExtractor();
                break;
            case MpcaIConstants.TIGERDIRECT_NAME:
                extractor = MpcaTigerDirectCommentsExtractor.getExtractor();
                break;
            case MpcaIConstants.NEWEGG_NAME:
                extractor = MpcaNeweggCommentsExtractor.getExtractor();
                break;
            default:
                throw new MpcaCommentsExtractorNotFoundException(pageName);
        }
        
        List<MpcaCommentModel> commentsExtracted = new ArrayList<>();
        for (Element coms : allComments) {
            commentsExtracted.addAll(extractor.commentExtractor(coms));
        }
        System.out.println("a = " + commentsExtracted.size());
        return commentsExtracted;
    }
    
    private MpcaAdditionType createOrGetAddition(String type, final String category) 
            throws PreexistingEntityException, Exception {
        MpcaAdditionType theAdd = ac.findAdditionByType(type);
        // Se crea la adición en caso de que no exista
        if(theAdd == null) {
            theAdd = new MpcaAdditionType();
            theAdd.setAddType(type);
            MpcaAdditionCategory mac = createOrGetCategory(category);
            theAdd.setCategoryId(mac);
            ac.create(theAdd);
            //theAdd = ac.findAdditionByType(theAdd.getAddType());
            System.out.println("Addition Created");
        }
        return theAdd;
    }
    
    private void createProductWebPage(MpcaProduct product, MpcaWebPage wp, MpcaPageModel p) throws Exception {
        MpcaProductWebPage pwb = new MpcaProductWebPage();
        pwb.setMpcaProduct(product);
        pwb.setMpcaWebPage(wp);
        pwb.setProductUrl(p.getMainPageURL().toString());
        pwb.setCommentUrl(p.getCommentsPageURL().toString());
        pwb.setLastUpdate(new Date(MpcaIConstants.GREGORIAN_BASE.getTimeInMillis()));
        pwpc.create(pwb);
        System.out.println("ProductWebPage created");
    }

    private void createOrUpdateComments(MpcaProduct product, MpcaWebPage wp, List<MpcaCommentModel> commentsExtracted) throws Exception {
        GregorianCalendar lastUpdate = null;
        // TODO: Cambiar a consulta
        for (MpcaProductWebPage productWebPage : product.getMpcaProductWebPageList()) {
            if(productWebPage.getMpcaWebPage().getPageId() == wp.getPageId()) {
                lastUpdate = new GregorianCalendar();
                lastUpdate.setTime(productWebPage.getLastUpdate());
                productWebPage.setLastUpdate(new Date());
                pwpc.edit(productWebPage);
                break;
            }
        }
        boolean firstTime = false;
        if(lastUpdate == null) {
            lastUpdate = MpcaIConstants.GREGORIAN_BASE;
            firstTime = true;
        }
        
        for (MpcaCommentModel c : commentsExtracted) {
            /* Si la fecha de publicación del comentario es más reciente que la de la última actualización
               entonces se tiene en cuenta */
            if(firstTime || c.getDate().after(lastUpdate)) {
                GregorianCalendar commentDate = c.getDate();
                String commentPosted = Utils.gregorianCalendarToString(commentDate);
                String lastUpdateS = Utils.gregorianCalendarToString(lastUpdate);
                
                System.out.println("Comment posted: " + commentPosted + ", LastUpdate: " + lastUpdateS);
                
                MpcaComment persisComment = new MpcaComment();
                //persisComment.setCommentId(lastComment);
                persisComment.setPageId(wp);
                persisComment.setProductId(product);
                persisComment.setCommentText(c.getComment());
                persisComment.setPublicationDate(c.getDate().getTime());
                
                cc.create(persisComment);
                
                System.out.println(persisComment.getCommentId() + " Comment Created");
                
                // Crea al Autor
                createCommentAddition(MpcaIConstants.AUTHOR_TAG, MpcaIConstants.COMMENTS_TAG, persisComment, c.getAuthor());
                
                // Crea el Titulo
                createCommentAddition(MpcaIConstants.TAG_TITLE, MpcaIConstants.COMMENTS_TAG, persisComment, c.getTitle());
                
                // Crea el Rank
                createCommentAddition(MpcaIConstants.ADDITION_RANK, MpcaIConstants.COMMENTS_TAG, persisComment, c.getStars() + "");
                
                // Crea la Polaridad
                createCommentAddition(MpcaIConstants.ADDITION_POLARITY, MpcaIConstants.COMMENTS_TAG, persisComment, c.getPolarity() + "");
            }
            
        }
        
    }

    private MpcaAdditionCategory createOrGetCategory(String category) throws PreexistingEntityException, Exception {
        MpcaAdditionCategory mac = macc.findMpcaAdditionCategoryByName(category);
        if(mac == null) {
            mac = new MpcaAdditionCategory();
            mac.setName(category);
            macc.create(mac);
            //mac = macc.findMpcaAdditionCategoryByName(category);
            //macc.getMpcaAdditionCategoryCount();
        }
        return mac;
    }

    private void createCommentAddition(String addType, String categoryTag, MpcaComment persisComment, String authorUsername) throws Exception {
        MpcaAdditionType at = createOrGetAddition(addType, categoryTag);
        MpcaCommentAddition ca = new MpcaCommentAddition();
        ca.setMpcaComment(persisComment);
        ca.setMpcaAdditionType(at);
        ca.setValue(authorUsername);
        cac.create(ca);
    }

    private String extractFeature(Element featureBlock, String pageName, String feature) 
            throws MpcaPageNameNotFound, MpcaFeatureNotFound {
        MpcaICommentsExtractor extractor;
        switch(pageName) {
            case MpcaIConstants.TIGERDIRECT_NAME:
                extractor = MpcaTigerDirectCommentsExtractor.getExtractor();
                break;
            case MpcaIConstants.AMAZON_NAME:
                extractor = MpcaAmazonCommentsExtractor.getExtractor();
                break;
            case MpcaIConstants.NEWEGG_NAME:
                extractor = MpcaNeweggCommentsExtractor.getExtractor();
                break;
            default:
                throw new MpcaPageNameNotFound(pageName);
        }
        
        switch(feature) {
            case MpcaIConstants.BRAND_TAG:
                return extractor.brandExtractor(featureBlock);
            case MpcaIConstants.MODEL_TAG:
                return extractor.modelExtractor(featureBlock);
            default:
                throw new MpcaFeatureNotFound(feature);
        }
    }
    
    
}
