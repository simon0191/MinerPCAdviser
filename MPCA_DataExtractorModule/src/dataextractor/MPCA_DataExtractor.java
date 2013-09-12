/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dataextractor;

import controllers.AdditionJpaController;
import controllers.AuthorJpaController;
import controllers.BrandJpaController;
import controllers.CommentAdditionJpaController;
import controllers.MpcaCommentJpaController;
import controllers.ProductAdditionJpaController;
import controllers.ProductJpaController;
import controllers.ProductWebPageJpaController;
import controllers.WebPageJpaController;
import controllers.exceptions.PreexistingEntityException;
import entities.Addition;
import entities.Author;
import entities.Brand;
import entities.CommentAddition;
import entities.MpcaComment;
import entities.MpcaCommentPK;
import entities.Product;
import entities.ProductAddition;
import entities.ProductWebPage;
import entities.WebPage;
import exceptions.CommentsExtractorNotFoundException;
import exceptions.UnrecognizedExtensionException;
import interfaces.ICommentsExtractor;
import interfaces.IDataExtractor;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import model.MPCA_Comment;
import model.Polarity;
import org.jsoup.nodes.Element;

/**
 *
 * @author Antonio
 */
public class MPCA_DataExtractor implements IDataExtractor {
    
    // Singleton
    private static IDataExtractor dataExtractor = null;
    // Lista de URLs por página
    private Map<String, List<MPCA_Page>> computersURLs;
    // Grupo de descriptores por Página
    private Map<String, Map<String, List<MPCA_Selector>>> descriptors;
    
    private WebPageJpaController wpc = new WebPageJpaController();
    private MpcaCommentJpaController cc = new MpcaCommentJpaController();
    private ProductJpaController pc = new ProductJpaController();
    private BrandJpaController bc = new BrandJpaController();
    private ProductAdditionJpaController pac = new ProductAdditionJpaController();
    private AdditionJpaController ac = new AdditionJpaController();
    private ProductWebPageJpaController pwpc = new ProductWebPageJpaController();
    private AuthorJpaController authorC = new AuthorJpaController();
    private CommentAdditionJpaController cac = new CommentAdditionJpaController();
    
    public static IDataExtractor getInstance() {
        if(dataExtractor == null) {
            dataExtractor = new MPCA_DataExtractor();
        }
        return dataExtractor;
    }
    
    private MPCA_DataExtractor() {
        computersURLs = new TreeMap<>();
        descriptors = new TreeMap<>();
    }

    @Override
    public void updateFromFiles(String files) 
            throws IOException, PreexistingEntityException, CommentsExtractorNotFoundException, Exception {
        StringBuffer fileData = MPCA_PageExtractor.readFile(new File(files));
        String []lines = fileData.toString().split("\n");
        
        // Se leen los archivos que contienen los descriptores y URLs para guardarlos en memoria
        extractDescriptorsAndURLs(lines);
        
        /* Se consulta la cantidad de comentarios existentes en la base de datos para poder asignar el Id a los nuevos
           comentarios */
        long lastComment = cc.getMpcaCommentCount();
        
        BigDecimal biggest = BigDecimal.ZERO;
        Set<String> keySet = descriptors.keySet();
        for (String pageName : keySet) {
            pageName = pageName.trim();
            // Crea o busca el nombre de la página
            WebPage wp = createOrGetWebPageByName(biggest, pageName);
            System.out.println("WebPage created or updated = " + pageName);
            
            List<MPCA_Page> urls = computersURLs.get(pageName);
            Map<String, List<MPCA_Selector>> des = descriptors.get(pageName);
            // Se trasladan los descriptores de los comentarios del antiguo mapa para poder tratar página por página
            Map<String, List<MPCA_Selector>> comments = new TreeMap<>();
            comments.put(MPCA_DataExtractor.COMMENTS_TAG, des.remove(MPCA_DataExtractor.COMMENTS_TAG));
            if(des.containsKey(MPCA_DataExtractor.NEXT_PAGE_TAG)) {
                comments.put(MPCA_DataExtractor.NEXT_PAGE_TAG, des.remove(MPCA_DataExtractor.NEXT_PAGE_TAG));
            }
            if(des.containsKey(MPCA_DataExtractor.TOTAL_PAGES_TAG)) {
                comments.put(MPCA_DataExtractor.TOTAL_PAGES_TAG, des.remove(MPCA_DataExtractor.TOTAL_PAGES_TAG));
            }
            for (MPCA_Page p : urls) {
                System.out.println("Url = " + p.getCommentsPageURL());
                Map<String, Element> data = MPCA_PageExtractor.get(p.getMainPageURL().toString(), des);
                List<Element> allComments = extractCommentsFromURL(p.getCommentsPageURL(), comments, pageName);
                
                String brand = data.get(MPCA_DataExtractor.BRAND_TAG).text().trim().toUpperCase();
                String model = data.get(MPCA_DataExtractor.MODEL_TAG).text().trim().toUpperCase();
                
                // Crea o actualiza un producto
                Product product = createOrUpdateProduct(model, brand, wp, p, data);
                
                // Extrae los comentarios de los HTMLs y los estructura
                List<MPCA_Comment> commentsExtracted = extractComments(pageName, allComments);
                
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
                if(line.endsWith(MPCA_DataExtractor.URL_EXTENSION)) {
                    StringBuffer urlData = MPCA_PageExtractor.readFile(new File(line.trim()));
                    String []urls = urlData.toString().split("\n+");
                    List<MPCA_Page> urlPages = new ArrayList<>();
                    for (String urlLine : urls) {
                        if(urlLine.trim().charAt(0) != '#') {
                            String []pages = urlLine.split(" +");
                            MPCA_Page p;
                            // Una sola URL indica que los comentarios están en esa misma página, de lo contrario,
                            //los comentarios se encuentran en la segunda página
                            if(pages.length == 1) {
                                p = new MPCA_Page(pages[0].trim());
                            } else {
                                p = new MPCA_Page(pages[0].trim(), pages[1].trim());
                            }
                            urlPages.add(p);
                        }
                    }
                    computersURLs.put(page, urlPages);

                    // Extractor de selectors
                } else if(line.endsWith(MPCA_DataExtractor.DESCRIPTOR_EXTENSION)) {
                    Map<String, List<MPCA_Selector>> parseDescriptor = MPCA_PageExtractor.parseDescriptor(new File(line));
                    descriptors.put(page, parseDescriptor);
                } else {
                    throw new UnrecognizedExtensionException(line.substring(line.indexOf(".")));
                }
            } // fi line.charAt()
        }
    }
    
    private List<Element> extractCommentsFromURL(URL commentsURL, Map<String, 
            List<MPCA_Selector>> comments, String pageName) throws IOException, NumberFormatException {
        
        Map<String, Element> commentsData = MPCA_PageExtractor.get(commentsURL.toString().trim(), comments);
        
        List<Element> allComments = new ArrayList<>();
        
        if(pageName.equals(MPCA_DataExtractor.NEWEGG_NAME)) {
            Element ele = commentsData.get(MPCA_DataExtractor.TOTAL_PAGES_TAG);
            String totalPagesS = "0";
            if(ele != null) {
                totalPagesS = ele.text().replaceAll("[^0-9]", "");
            }
            int totalPages = Integer.parseInt(totalPagesS);
            System.out.println(totalPages);
            for (int i = 1; i <= totalPages; ++i) {
                String newURL = commentsURL.toString().replaceFirst("Page=" + "[0-9]+", "Page=" + i);
                Map<String, Element> comms = MPCA_PageExtractor.get(newURL, comments);
                allComments.add(comms.get(MPCA_DataExtractor.COMMENTS_TAG));
                System.out.println("Page = " + i);
            }
            return allComments;
        }
        
        allComments.add(commentsData.get(MPCA_DataExtractor.COMMENTS_TAG));
        Element ele = commentsData.get(MPCA_DataExtractor.NEXT_PAGE_TAG);
        
        String nextPage = ((ele!=null)?ele.attr("href").trim():"");
        
        String prefix = "";
        if(pageName.equals(MPCA_DataExtractor.TIGERDIRECT_NAME)) {
            prefix = "http://www.tigerdirect.com";
        }
        
        int a = 1;
        while(!nextPage.equals("")) {
            Map<String, Element> comms = MPCA_PageExtractor.get(prefix + nextPage, comments);
            allComments.add(comms.get(MPCA_DataExtractor.COMMENTS_TAG));
            ele = comms.get(MPCA_DataExtractor.NEXT_PAGE_TAG);
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
    private WebPage createOrGetWebPageByName(BigDecimal biggest, String pageName) throws Exception {
        List<WebPage> webPages = wpc.findWebPageEntities();
        WebPage webPage = null;
        for (WebPage wp : webPages) {
            if(biggest.compareTo(wp.getPageId()) < 0) {
                biggest = wp.getPageId();
            }
            if(wp.getPageName().equalsIgnoreCase(pageName)) {
                webPage = wp;
                break;
            }
        }
        if(webPage == null) {
            webPage = new WebPage(pageName, "http://www." + pageName + ".com");
            wpc.create(webPage);
            biggest = biggest.add(BigDecimal.ONE);
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
    private Product createOrUpdateProduct(String model, String brand, WebPage wp, MPCA_Page p, Map<String, Element> data) throws Exception {
        Product product = pc.findProductByModel(model);
        if(product == null) {
            Brand b = bc.findBrandByName(brand);
            if(b == null) {
                b = new Brand();
                b.setBrandName(brand);
                bc.create(b);
                b = bc.findBrandByName(brand);
                System.out.println("Brand created");
            }
            product = new Product(null, model);
            product.setBrand(b);
            pc.create(product);
            product = pc.findProductByModel(model);
            System.out.println("Product created");
        }
        boolean find = false;
        for (ProductWebPage productWebPage : product.getProductWebPageList()) {
            if(productWebPage.getWebPage().getPageId().compareTo(wp.getPageId()) == 0) {
                find = true;
            }
        }
        if(!find) {
            createProductWebPage(product, wp, p);
        }
        
        // Se eliminan el modelo y el productor para que no sean contadas como adiciones
        data.remove(MPCA_DataExtractor.BRAND_TAG);
        data.remove(MPCA_DataExtractor.MODEL_TAG);
        
        // Se actualizan las adiciones que el producto ya tenga
        if(product.getProductAdditionList() != null) {
            for (ProductAddition pa : product.getProductAdditionList()) {
                Addition add = pa.getAddition();
                if(data.containsKey(add.getAddType())) {
                    pa.setValue(data.get(add.getAddType()).text().trim());
                    data.remove(add.getAddType());
                    pac.edit(pa);
                }
            }
        }
        // Adiciones que faltan en la tabla principal
        for (Map.Entry<String, Element> es : data.entrySet()) {
            Addition theAdd = createOrGetAddition(es.getKey());
            System.out.println(theAdd);
            // Se agrega la nueva adición al producto con su respectivo valor
            ProductAddition pa = new ProductAddition();
            pa.setProduct(product);
            pa.setAddition(theAdd);
            if(es.getValue() != null) {
                pa.setValue(es.getValue().text().trim());
                pac.create(pa);
                System.out.println("Product Addition created");
            }
        }
        return pc.findProduct(product.getProductPK());
    }
    
    /**
     * Método que extrae los comentarios de un bloque de HTML para estructurarlo.
     * @param pageName Nombre de la página que va a ser utilizada para encontrar su Extractor respectivo
     * @param allComments Lista de comentarios semiestructurados en HTML
     * @return Retorna una lista de comentarios estructurados en la clase MPCA_Comment
     * @throws CommentsExtractorNotFoundException 
     */
    private List<MPCA_Comment> extractComments(String pageName, List<Element> allComments) throws CommentsExtractorNotFoundException {
        /* Extracción de comentarios */
        ICommentsExtractor extractor = null;
        if(pageName.equals(MPCA_DataExtractor.AMAZON_NAME)) {
            extractor = AmazonCommentsExtractor.getExtractor();
        } else if(pageName.equals(MPCA_DataExtractor.TIGERDIRECT_NAME)) {
            extractor = TigerDirectCommentsExtractor.getExtractor();
        } else if(pageName.equals(MPCA_DataExtractor.NEWEGG_NAME)) {
            extractor = NeweggCommentsExtractor.getExtractor();
        }
        if(extractor == null) {
            throw new CommentsExtractorNotFoundException(pageName);
        }
        List<MPCA_Comment> commentsExtracted = new ArrayList<>();
        for (Element coms : allComments) {
            commentsExtracted.addAll(extractor.commentExtractor(coms));
        }
        return commentsExtracted;
    }
    
    private Addition createOrGetAddition(String type) throws Exception {
        Addition theAdd = ac.findAdditionByType(type);
        // Se crea la adición en caso de que no exista
        if(theAdd == null) {
            theAdd = new Addition();
            theAdd.setAddType(type);
            ac.create(theAdd);
            theAdd = ac.findAdditionByType(theAdd.getAddType());
            System.out.println("Addition Created");
        }
        return theAdd;
    }
    
    private void createProductWebPage(Product product, WebPage wp, MPCA_Page p) throws Exception {
        ProductWebPage pwb = new ProductWebPage();
        pwb.setProduct(product);
        pwb.setWebPage(wp);
        pwb.setProductUrl(p.getMainPageURL().toString());
        pwb.setCommentUrl(p.getCommentsPageURL().toString());
        pwb.setLastUpdate(new Date(1945, 5, 6));
        pwpc.create(pwb);
        System.out.println("ProductWebPage created");
    }

    private long createOrUpdateComments(Product product, WebPage wp, List<MPCA_Comment> commentsExtracted, long lastComment, MPCA_Page p) throws Exception {
        Date lastUpdate = null;
        for (ProductWebPage productWebPage : product.getProductWebPageList()) {
            if(productWebPage.getWebPage().getPageId().compareTo(wp.getPageId()) == 0) {
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
        
        for (MPCA_Comment c : commentsExtracted) {
            /* Si la fecha de publicación del comentario es más reciente que la de la última actualización
               entonces se tiene en cuenta */
            if(c.getDate().after(gc)) {
                GregorianCalendar gc2 = c.getDate();
                String commentPosted = gc2.get(Calendar.DAY_OF_MONTH) 
                        + "/" + gc2.get(Calendar.MONTH) + "/" + gc2.get(Calendar.YEAR);
                String lastUpdateS = gc.get(Calendar.DAY_OF_MONTH) 
                        + "/" + gc.get(Calendar.MONTH) + "/" + gc.get(Calendar.YEAR);
                System.out.println("Comment posted: " + commentPosted + ", LastUpdate: " + lastUpdateS);
                String a = c.getAuthor();
                if(a != null) {
                    a = a.trim();
                }
                if(a == null || a.equalsIgnoreCase("na") || a.equalsIgnoreCase("n/a") || 
                        a.equalsIgnoreCase("") || a.equalsIgnoreCase(",")) {
                    a = NA_AUTHOR;
                }
                
                // Crea o busca al Autor
                Author author = authorC.findAuthorByName(a);
                if(author == null) {
                    author = new Author();
                    author.setAuthorName(a);
                    authorC.create(author);
                    author = authorC.findAuthorByName(a);
                }
                
                MpcaComment persisComment = new MpcaComment();
                persisComment.setMpcaCommentPK(new MpcaCommentPK(
                        new BigInteger((++lastComment) + ""), wp.getPageId().toBigInteger(), 
                        product.getProductPK().getBrandId(), author.getAuthorId().toBigInteger(), 
                        product.getProductPK().getBrandId()));
                persisComment.setWebPage(wp);
                persisComment.setProduct(product);
                persisComment.setAuthor(author);
                persisComment.setCommentText(c.getComment());
                persisComment.setPublicationDate(c.getDate().getTime());
                persisComment.setCommentUrl(p.getCommentsPageURL().toString());
                
                cc.create(persisComment);
                System.out.println(lastComment + " Comment Created");
                
                
                if(c.getTitle() == null || c.getTitle().trim().equals("")) {
                    c.setTitle("-");
                }
                /*System.out.println("title = " + c.getTitle());
                System.out.println("rank = " + c.getStars());
                System.out.println("Polarity = " + c.getPolarity());*/
                
                Addition add = createOrGetAddition(ADDITION_TITLE);
                CommentAddition ca = new CommentAddition();
                ca.setAddition(add);
                ca.setMpcaComment(persisComment);
                ca.setValue(c.getTitle());
                cac.create(ca);
                add = createOrGetAddition(ADDITION_RANK);
                ca.setAddition(add);
                ca.setValue(c.getStars() + "");
                cac.create(ca);
                add = createOrGetAddition(ADDITION_POLARITY);
                ca.setAddition(add);
                ca.setValue(c.getPolarity() + "");
                cac.create(ca);
            }
            
        }
        return lastComment;
    }
    
    private static String URL_EXTENSION = ".urls";
    private static String DESCRIPTOR_EXTENSION = ".descriptor";
    private static String BRAND_TAG = "brand";
    private static String MODEL_TAG = "model";
    private static String HARD_DRIVE_TAG = "HD";
    private static String RAM_TAG = "ram";
    private static String TOTAL_PAGES_TAG = "totalPages";
    private static String NEXT_PAGE_TAG = "nextPage";
    private static String COMMENTS_TAG = "comments";
    private static String TIGERDIRECT_NAME = "tigerdirect";
    private static String AMAZON_NAME = "amazon";
    private static String NEWEGG_NAME = "newegg";
    private static String NA_AUTHOR = "N/A";
    private static String ADDITION_TITLE = "title";
    private static String ADDITION_RANK = "rank";
    private static String ADDITION_POLARITY = "polarity";
    private static Polarity ADDITION_POSITIVE = Polarity.POSITIVE;
    private static Polarity ADDITION_NEGATIVE = Polarity.NEGATIVE;
    private static Polarity ADDITION_NEUTRAL = Polarity.NEUTRAL;
    
}
