/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import exceptions.MpcaFeatureNotFound;
import exceptions.MpcaPageNameNotFound;
import controllers.JpaController;
import controllers.MpcaAdditionCategoryJpaController;
import controllers.MpcaAdditionTypeJpaController;
import controllers.MpcaCommentAdditionJpaController;
import controllers.MpcaCommentJpaController;
import controllers.MpcaProductAdditionJpaController;
import controllers.MpcaProductJpaController;
import controllers.MpcaProductWebPageJpaController;
import controllers.MpcaWebPageJpaController;
import controllers.exceptions.PreexistingEntityException;
import entities.MpcaAdditionCategory;
import entities.MpcaAdditionType;
import entities.MpcaComment;
import entities.MpcaCommentAddition;
import entities.MpcaProduct;
import entities.MpcaProductAddition;
import entities.MpcaProductWebPage;
import entities.MpcaWebPage;
import exceptions.MpcaCommentsExtractorNotFoundException;
import exceptions.MpcaUnrecognizedExtensionException;
import interfaces.IMpcaCommentsExtractor;
import interfaces.IMpcaDataExtractor;
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
import model.MpcaCommentModel;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public class MpcaDataExtractor implements IMpcaDataExtractor {
    
    // Singleton
    private static IMpcaDataExtractor dataExtractor = null;
    // Lista de URLs por página
    private Map<String, List<MpcaPage>> computersURLs;
    // Grupo de descriptores por Página
    private Map<String, Map<String, List<MpcaSelector>>> descriptors;
    
    private MpcaWebPageJpaController wpc = new MpcaWebPageJpaController();
    private MpcaCommentJpaController cc = new MpcaCommentJpaController();
    private MpcaProductJpaController pc = new MpcaProductJpaController();
    private MpcaProductAdditionJpaController pac = new MpcaProductAdditionJpaController();
    private MpcaAdditionTypeJpaController ac = new MpcaAdditionTypeJpaController();
    private MpcaProductWebPageJpaController pwpc = new MpcaProductWebPageJpaController();
    private MpcaCommentAdditionJpaController cac = new MpcaCommentAdditionJpaController();
    private MpcaAdditionCategoryJpaController macc = new MpcaAdditionCategoryJpaController();
    
    public static IMpcaDataExtractor getInstance() {
        if(dataExtractor == null) {
            dataExtractor = new MpcaDataExtractor();
        }
        return dataExtractor;
    }
    
    private MpcaDataExtractor() {
        computersURLs = new TreeMap<>();
        descriptors = new TreeMap<>();
    }

    @Override
    public void updateFromFiles(String files) 
            throws IOException, PreexistingEntityException, MpcaCommentsExtractorNotFoundException, Exception {
        StringBuffer fileData = MpcaPageExtractor.readFile(new File(files));
        String []lines = fileData.toString().split("\n");
        
        // Se leen los archivos que contienen los descriptores y URLs para guardarlos en memoria
        extractDescriptorsAndURLs(lines);
        
        /* Se consulta la cantidad de comentarios existentes en la base de datos para poder asignar el Id a los nuevos
           comentarios */
        long lastComment = cc.getMpcaCommentCount();
        
        Set<String> keySet = descriptors.keySet();
        for (String pageName : keySet) {
            pageName = pageName.trim();
            // Crea o busca el nombre de la página
            MpcaWebPage wp = createOrGetWebPageByName(pageName);
            System.out.println("WebPage created or updated = " + pageName);
            
            List<MpcaPage> urls = computersURLs.get(pageName);
            Map<String, List<MpcaSelector>> des = descriptors.get(pageName);
            // Se trasladan los descriptores de los comentarios del antiguo mapa para poder tratar página por página
            Map<String, List<MpcaSelector>> comments = new TreeMap<>();
            comments.put(JpaController.COMMENTS_TAG, des.remove(JpaController.COMMENTS_TAG));
            if(des.containsKey(JpaController.NEXT_PAGE_TAG)) {
                comments.put(JpaController.NEXT_PAGE_TAG, des.remove(JpaController.NEXT_PAGE_TAG));
            }
            if(des.containsKey(JpaController.TOTAL_PAGES_TAG)) {
                comments.put(JpaController.TOTAL_PAGES_TAG, des.remove(JpaController.TOTAL_PAGES_TAG));
            }
            for (MpcaPage p : urls) {
                System.out.println("Url = " + p.getCommentsPageURL());
                Map<String, Element> data = MpcaPageExtractor.get(p.getMainPageURL().toString(), des);
                List<Element> allComments = extractCommentsFromURL(p.getCommentsPageURL(), comments, pageName);
                
                //String brand = data.get(JpaController.BRAND_TAG).text().trim().toUpperCase();
                String brand = extractFeature(data.get(JpaController.BRAND_TAG), pageName, JpaController.BRAND_TAG);
                //String model = data.get(JpaController.MODEL_TAG).text().trim().toUpperCase();
                String model = extractFeature(data.get(JpaController.MODEL_TAG), pageName, JpaController.MODEL_TAG);
                
                // Crea o actualiza un producto
                MpcaProduct product = createOrUpdateProduct(model, brand, wp, p, data);
                
                // Extrae los comentarios de los HTMLs y los estructura
                List<MpcaCommentModel> commentsExtracted = extractComments(pageName, allComments);
                
                // Crea o actualiza la base de datos de comentarios
                lastComment = createOrUpdateComments(product, wp, commentsExtracted, lastComment, p);
                
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
                if(line.endsWith(JpaController.URL_EXTENSION)) {
                    StringBuffer urlData = MpcaPageExtractor.readFile(new File(line.trim()));
                    String []urls = urlData.toString().split("\n+");
                    List<MpcaPage> urlPages = new ArrayList<>();
                    for (String urlLine : urls) {
                        if(urlLine.trim().charAt(0) != '#') {
                            String []pages = urlLine.split(" +");
                            MpcaPage p;
                            // Una sola URL indica que los comentarios están en esa misma página, de lo contrario,
                            //los comentarios se encuentran en la segunda página
                            if(pages.length == 1) {
                                p = new MpcaPage(pages[0].trim());
                            } else {
                                p = new MpcaPage(pages[0].trim(), pages[1].trim());
                            }
                            urlPages.add(p);
                        }
                    }
                    computersURLs.put(page, urlPages);

                    // Extractor de selectors
                } else if(line.endsWith(JpaController.DESCRIPTOR_EXTENSION)) {
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
        
        if(pageName.equals(JpaController.NEWEGG_NAME)) {
            Element ele = commentsData.get(JpaController.TOTAL_PAGES_TAG);
            String totalPagesS = "0";
            if(ele != null) {
                totalPagesS = ele.text().replaceAll("[^0-9]", "");
            }
            int totalPages = Integer.parseInt(totalPagesS);
            System.out.println(totalPages);
            for (int i = 1; i <= totalPages; ++i) {
                String newURL = commentsURL.toString().replaceFirst("Page=" + "[0-9]+", "Page=" + i);
                Map<String, Element> comms = MpcaPageExtractor.get(newURL, comments);
                allComments.add(comms.get(JpaController.COMMENTS_TAG));
                System.out.println("Page = " + i);
            }
            return allComments;
        }
        
        allComments.add(commentsData.get(JpaController.COMMENTS_TAG));
        Element ele = commentsData.get(JpaController.NEXT_PAGE_TAG);
        
        String nextPage = ((ele!=null)?ele.attr("href").trim():"");
        
        String prefix = "";
        if(pageName.equals(JpaController.TIGERDIRECT_NAME)) {
            prefix = "http://www.tigerdirect.com";
        }
        
        int a = 1;
        while(!nextPage.equals("")) {
            Map<String, Element> comms = MpcaPageExtractor.get(prefix + nextPage, comments);
            allComments.add(comms.get(JpaController.COMMENTS_TAG));
            ele = comms.get(JpaController.NEXT_PAGE_TAG);
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
        long biggest = 0;
        for (MpcaWebPage wp : webPages) {
            /*if(biggest < wp.getPageId()) {
                biggest = wp.getPageId();
            }*/
            if(wp.getPageName().equalsIgnoreCase(pageName)) {
                webPage = wp;
                break;
            }
            biggest++;
        }
        if(webPage == null) {
            webPage = new MpcaWebPage(pageName, "http://www." + pageName + ".com");
            wpc.create(webPage);
            biggest++;
            webPage.setPageId(biggest);
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
    private MpcaProduct createOrUpdateProduct(String model, String brand, MpcaWebPage wp, MpcaPage p, Map<String, Element> data) throws Exception {
        MpcaProduct product = pc.findProductByModel(model);
        if(product == null) {
            product = new MpcaProduct();
            product.setModel(model);
            pc.create(product);
            product = pc.findProductByModel(model);
            
            MpcaAdditionType at = createOrGetAddition(JpaController.BRAND_TAG, JpaController.PRODUCT_TAG);
            
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
        data.remove(JpaController.BRAND_TAG);
        data.remove(JpaController.MODEL_TAG);
        
        // Se actualizan las adiciones que el producto ya tenga
        if(product.getMpcaProductAdditionList() != null) {
            for (MpcaProductAddition pa : product.getMpcaProductAdditionList()) {
                MpcaAdditionType add = pa.getMpcaAdditionType();
                if(data.containsKey(add.getAddType())) {
                    pa.setValue(data.get(add.getAddType()).text().trim());
                    data.remove(add.getAddType());
                    pac.edit(pa);
                }
            }
        }
        // Adiciones que faltan en la tabla principal
        for (Map.Entry<String, Element> es : data.entrySet()) {
            MpcaAdditionType theAdd = createOrGetAddition(es.getKey(), JpaController.PRODUCT_TAG);
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
        IMpcaCommentsExtractor extractor = null;
        switch (pageName) {
            case JpaController.AMAZON_NAME:
                extractor = MpcaAmazonCommentsExtractor.getExtractor();
                break;
            case JpaController.TIGERDIRECT_NAME:
                extractor = MpcaTigerDirectCommentsExtractor.getExtractor();
                break;
            case JpaController.NEWEGG_NAME:
                extractor = MpcaNeweggCommentsExtractor.getExtractor();
                break;
        }
        if(extractor == null) {
            throw new MpcaCommentsExtractorNotFoundException(pageName);
        }
        List<MpcaCommentModel> commentsExtracted = new ArrayList<>();
        for (Element coms : allComments) {
            commentsExtracted.addAll(extractor.commentExtractor(coms));
        }
        return commentsExtracted;
    }
    
    private MpcaAdditionType createOrGetAddition(String type, String category) 
            throws PreexistingEntityException, Exception {
        MpcaAdditionType theAdd = ac.findAdditionByType(type);
        // Se crea la adición en caso de que no exista
        if(theAdd == null) {
            theAdd = new MpcaAdditionType();
            theAdd.setAddType(type);
            MpcaAdditionCategory mac = createOrGetCategory(category);
            theAdd.setCategoryId(mac);
            ac.create(theAdd);
            theAdd = ac.findAdditionByType(theAdd.getAddType());
            System.out.println("Addition Created");
        }
        return theAdd;
    }
    
    private void createProductWebPage(MpcaProduct product, MpcaWebPage wp, MpcaPage p) throws Exception {
        MpcaProductWebPage pwb = new MpcaProductWebPage();
        pwb.setMpcaProduct(product);
        pwb.setMpcaWebPage(wp);
        pwb.setProductUrl(p.getMainPageURL().toString());
        pwb.setCommentUrl(p.getCommentsPageURL().toString());
        pwb.setLastUpdate(new Date(1945, 5, 6));
        pwpc.create(pwb);
        System.out.println("ProductWebPage created");
    }

    private long createOrUpdateComments(MpcaProduct product, MpcaWebPage wp, List<MpcaCommentModel> commentsExtracted, long lastComment, MpcaPage p) throws Exception {
        Date lastUpdate = null;
        for (MpcaProductWebPage productWebPage : product.getMpcaProductWebPageList()) {
            if(productWebPage.getMpcaWebPage().getPageId().compareTo(wp.getPageId()) == 0) {
                lastUpdate = productWebPage.getLastUpdate();
                GregorianCalendar gc = new GregorianCalendar();
                productWebPage.setLastUpdate(
                        new Date(gc.get(Calendar.YEAR), gc.get(Calendar.MONTH), gc.get(Calendar.DAY_OF_MONTH)));
                pwpc.edit(productWebPage);
                break;
            }
        }
        if(lastUpdate == null) {
            lastUpdate = new Date(1945, 5, 6);
        }
        GregorianCalendar gc = new GregorianCalendar(
                lastUpdate.getYear(), lastUpdate.getMonth(), lastUpdate.getDay());
        
        for (MpcaCommentModel c : commentsExtracted) {
            /* Si la fecha de publicación del comentario es más reciente que la de la última actualización
               entonces se tiene en cuenta */
            if(c.getDate().after(gc)) {
                GregorianCalendar gc2 = c.getDate();
                String commentPosted = gc2.get(Calendar.DAY_OF_MONTH) 
                        + "/" + gc2.get(Calendar.MONTH) + "/" + gc2.get(Calendar.YEAR);
                String lastUpdateS = gc.get(Calendar.DAY_OF_MONTH) 
                        + "/" + gc.get(Calendar.MONTH) + "/" + gc.get(Calendar.YEAR);
                
                System.out.println("Comment posted: " + commentPosted + ", LastUpdate: " + lastUpdateS);
                
                String authorUsername = c.getAuthor();
                if(authorUsername != null) {
                    authorUsername = authorUsername.trim();
                }
                if(authorUsername == null || authorUsername.equalsIgnoreCase("na") || authorUsername.equalsIgnoreCase("n/a") || 
                        authorUsername.equalsIgnoreCase("") || authorUsername.equalsIgnoreCase(",")) {
                    authorUsername = JpaController.NA_AUTHOR;
                }
                
                MpcaComment persisComment = new MpcaComment();
                //persisComment.setCommentId(lastComment);
                persisComment.setPageId(wp);
                persisComment.setProductId(product);
                persisComment.setCommentText(c.getComment());
                persisComment.setPublicationDate(c.getDate().getTime());
                
                cc.create(persisComment);
                System.out.println(lastComment + " Comment Created");
                
                // Crea al Autor
                createCommentAddition(JpaController.AUTHOR_TAG, 
                        JpaController.COMMENTS_TAG, persisComment, authorUsername);
                
                if(c.getTitle() == null || c.getTitle().trim().equals("")) {
                    c.setTitle("-");
                }
                
                // Crea el Titulo
                createCommentAddition(JpaController.TAG_TITLE, 
                        JpaController.COMMENTS_TAG, persisComment, c.getTitle());
                
                // Crea el Rank
                createCommentAddition(JpaController.ADDITION_RANK, 
                        JpaController.COMMENTS_TAG, persisComment, c.getStars() + "");
                
                // Crea la Polaridad
                createCommentAddition(JpaController.ADDITION_POLARITY, 
                        JpaController.COMMENTS_TAG, persisComment, c.getPolarity() + "");
            }
            
        }
        return lastComment;
    }

    private MpcaAdditionCategory createOrGetCategory(String category) throws PreexistingEntityException, Exception {
        MpcaAdditionCategory mac = macc.findMpcaAdditionCategoryByName(category);
        if(mac == null) {
            mac = new MpcaAdditionCategory();
            mac.setName(category);
            macc.create(mac);
            mac = macc.findMpcaAdditionCategoryByName(category);
        }
        return mac;
    }

    private void createCommentAddition(String addType, String categoryTag, MpcaComment persisComment, String authorUsername) throws Exception {
        MpcaAdditionType at = createOrGetAddition(addType, categoryTag);
        MpcaCommentAddition ca = new MpcaCommentAddition(persisComment.getCommentId(), at.getAddId());
        ca.setValue(authorUsername);
        cac.create(ca);
    }

    private String extractFeature(Element featureBlock, String pageName, String feature) 
            throws MpcaPageNameNotFound, MpcaFeatureNotFound {
        IMpcaCommentsExtractor extractor;
        switch(pageName) {
            case JpaController.TIGERDIRECT_NAME:
                extractor = MpcaTigerDirectCommentsExtractor.getExtractor();
                break;
            case JpaController.AMAZON_NAME:
                extractor = MpcaAmazonCommentsExtractor.getExtractor();
                break;
            case JpaController.NEWEGG_NAME:
                extractor = MpcaNeweggCommentsExtractor.getExtractor();
                break;
            default:
                throw new MpcaPageNameNotFound(pageName);
        }
        
        switch(feature) {
            case JpaController.BRAND_TAG:
                return extractor.brandExtractor(featureBlock);
            case JpaController.MODEL_TAG:
                return extractor.modelExtractor(featureBlock);
            default:
                throw new MpcaFeatureNotFound(feature);
        }
    }
    
    
}
