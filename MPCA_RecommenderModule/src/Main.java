
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import model.controllers.MpcaProductJpaController;
import model.entities.MpcaProduct;
import model.utils.MpcaPolarity;
import model.utils.Range;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import recommender.MpcaRecommendation;
import recommender.MpcaRecommender;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author SimonXPS
 */
public class Main {

    public static void main(String[] args) throws Exception {
        JSONObject jo = getJsonFromWS("http://mpca-api.herokuapp.com/products");
        System.out.println(jo);
        /*List<MpcaProduct> products = new MpcaProductJpaController().findMpcaProductEntities();
         MpcaRecommender recommender = MpcaRecommender.getInstance();
         System.out.println("============================================");
         for (MpcaProduct p : products) {
         MpcaRecommendation recommendation = recommender.doRecommendation(p, 4l);
         System.out.println("Id: " + p.getProductId());
         System.out.println("Model: " + p.getModel());
         System.out.println("- Recommendation:");
         String prefix = "\t";
         System.out.println(prefix + recommendation.getDecision());
         prefix += "\t";
         for (MpcaPolarity polarity : recommendation.getPolarities()) {
         System.out.println("\t" + "Polarity: " + polarity);
         Range range = recommendation.getRange(polarity);
         System.out.println(prefix + "Min: " + range.getMin() + ", Max: " + range.getMax());
         }
         System.out.println("========================================");
         }*/
    }

    private static JSONObject getJsonFromWS(String url) {
        DefaultHttpClient httpClient = new DefaultHttpClient(new BasicHttpParams());
        HttpGet httpGet = new HttpGet(url);

        httpGet.setHeader("Content-type", "application/json");

        JSONObject jObject = null;

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            //response.getEntity().

            InputStream inputStream = entity.getContent();


            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = bf.readLine()) != null) {
                sb.append(line + "\n");
            }
            jObject = new JSONObject(sb.toString());
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jObject;
    }
}
