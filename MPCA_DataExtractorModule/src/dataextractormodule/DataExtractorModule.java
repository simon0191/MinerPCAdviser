package dataextractormodule;

import dataextractor.MpcaAmazonCommentsExtractor;
import dataextractor.MpcaDataExtractor;
import dataextractor.MpcaNeweggCommentsExtractor;
import dataextractor.MpcaTigerDirectCommentsExtractor;
import interfaces.IMpcaCommentsExtractor;
import interfaces.IMpcaDataExtractor;
import java.io.IOException;
import java.net.URL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Antonio
 */
public class DataExtractorModule {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        IMpcaDataExtractor de = MpcaDataExtractor.getInstance();
        de.updateFromFiles("files.descriptor");
        //testExtractors();
    }
    
    private static void tigerDirectBrand(Document doc) {
        IMpcaCommentsExtractor extractor = MpcaTigerDirectCommentsExtractor.getExtractor();
        String res = extractor.brandExtractor(doc.select("div.productFootnote.itemModuleBox>ul.pInfo>li>strong").first());
        System.out.println(res);
    }
    
    private static void neweggBrand(Document doc) {
        IMpcaCommentsExtractor extractor = MpcaNeweggCommentsExtractor.getExtractor();
        String res = extractor.brandExtractor(doc.select("fieldset").first());
        System.out.println(res);
    }
    
    private static void amazonBrand(Document doc) {
        IMpcaCommentsExtractor extractor = MpcaAmazonCommentsExtractor.getExtractor();
        String res = extractor.brandExtractor(doc.select("div.attrG").get(1).select("tbody").first());
        System.out.println(res);
    }

    private static void tigerDirectModel(Document doc) {
        IMpcaCommentsExtractor extractor = MpcaTigerDirectCommentsExtractor.getExtractor();
        String res = extractor.modelExtractor(doc.select("div.rightCol>div.prodName span.sku").first());
        System.out.println(res);
    }
    
    private static void neweggModel(Document doc) {
        IMpcaCommentsExtractor extractor = MpcaNeweggCommentsExtractor.getExtractor();
        String res = extractor.modelExtractor(doc.select("fieldset").first());
        System.out.println(res);
    }
    
    private static void amazonModel(Document doc) {
        IMpcaCommentsExtractor extractor = MpcaAmazonCommentsExtractor.getExtractor();
        String res = extractor.modelExtractor(doc.select("div.attrG").get(1).select("tbody").first());
        System.out.println(res);
    }

    private static void testExtractors() throws IOException {
        //String url = "http://www.tigerdirect.com/applications/SearchTools/item-details.asp?EdpNo=4917324&CatId=7330";
        //String url = "http://www.newegg.com/Product/Product.aspx?Item=34-131-403&SortField=0&SummaryType=0&Pagesize=10&PurchaseMark=&SelectedRating=-1&VideoOnlyMark=False&VendorMark=&IsFeedbackTab=true&Keywords=%28keywords%29&Page=1#scrollFullInfo";
        String url = "http://www.amazon.com/Samsung-XE303C12-A01US-Chromebook-Wi-Fi-11-6-Inch/dp/B009LL9VDG/";
        
        Document doc = Jsoup.parse(new URL(url), 3000);
        //tigerDirectBrand(doc);
        //neweggBrand(doc);
        //amazonBrand(doc);
        //tigerDirectModel(doc);
        //neweggModel(doc);
        amazonModel(doc);
    }
}
