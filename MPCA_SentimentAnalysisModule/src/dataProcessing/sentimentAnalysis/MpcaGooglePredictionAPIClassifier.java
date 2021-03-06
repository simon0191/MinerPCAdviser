package dataProcessing.sentimentAnalysis;

import dataProcessing.sentimentAnalysis.test.MpcaClassifierTest;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.prediction.Prediction;
import com.google.api.services.prediction.PredictionScopes;
import com.google.api.services.prediction.model.Input;
import com.google.api.services.prediction.model.Input.InputInput;
import com.google.api.services.prediction.model.Output;
import dataProcessing.sentimentAnalysis.utils.MpcaDataSet;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author simon
 */
public class MpcaGooglePredictionAPIClassifier implements MpcaIClassifier {

    /**
     * Be sure to specify the name of your application. If the application name
     * is {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "MinerPCAdviser";
    /**
     * Directory to store user credentials.
     */
    private static final java.io.File STORE_DIR =
            new java.io.File("store");
    /**
     * Directory to locate credentials.
     */
    private static final java.io.File DATA_DIR =
            new java.io.File("data");
    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */
    private static FileDataStoreFactory dataDir;
    private static FileDataStoreFactory storeDir;
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;
    private static Prediction client;

    public MpcaGooglePredictionAPIClassifier() throws GeneralSecurityException, IOException, Exception {

        // initialize the transport
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // initialize the data store factory
        dataDir = new FileDataStoreFactory(DATA_DIR);
        storeDir = new FileDataStoreFactory(STORE_DIR);


        // authorization
        Credential credential = authorize();
        // set up global Prediction instance
        client = new Prediction.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

    }

    @Override
    public String bestMatch(String text) throws IOException {



        Output output =
                getGoogleOutput(text);
        //client.trainedmodels().predict("109973074802", "sentiment", input).execute();
        //System.out.println(output.toPrettyString());
        //output.
        return output.getOutputLabel().toUpperCase();
    }

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private Credential authorize() throws Exception {
        // load client secrets

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(new FileInputStream(new File(dataDir.getDataDirectory(), "/client_secrets.json"))));

        // Set up authorization code flow.
        // Ask for only the permissions you need. Asking for more permissions will
        // reduce the number of users who finish the process for giving you access
        // to their accounts. It will also increase the amount of effort you will
        // have to spend explaining to users what you are doing with their data.
        // Here we are listing all of the available scopes. You should remove scopes
        // that you are not actually using.
        Set<String> scopes = new HashSet<String>();
        scopes.add(PredictionScopes.DEVSTORAGE_FULL_CONTROL);
        scopes.add(PredictionScopes.DEVSTORAGE_READ_ONLY);
        scopes.add(PredictionScopes.DEVSTORAGE_READ_WRITE);
        scopes.add(PredictionScopes.PREDICTION);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets, scopes)
                .setDataStoreFactory(storeDir)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static void main(String[] args) {
        try {
            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // initialize the data store factory
            //dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // authorization
            MpcaGooglePredictionAPIClassifier bla = new MpcaGooglePredictionAPIClassifier();
            Credential credential = bla.authorize();
            // set up global Prediction instance
            client = new Prediction.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            Output output = bla.getGoogleOutput("I'm very happy");
            //client.trainedmodels().predict("109973074802", "sentiment", input).execute();
            System.out.println(output.toPrettyString());
            List<Output.OutputMulti> multi = output.getOutputMulti();
            System.out.println("--------------------------");
            for (Output.OutputMulti oo : multi) {
                //System.out.println(oo.toPrettyString());
                System.out.println(oo.getScore());
            }


        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public MpcaClassifierTest createTest(MpcaDataSet testData) {
        return new MpcaClassifierTest(testData, this);
    }

    @Override
    public MpcaClassification classify(String text) throws Exception {

        Output output = getGoogleOutput(text);
        List<Output.OutputMulti> multi = output.getOutputMulti();
        MpcaClassification mclass = new MpcaClassification();
        for (Output.OutputMulti oo : multi) {
            mclass.put(oo.getLabel(), Double.parseDouble(oo.getScore()));
        }
        return mclass;
    }
    
    //TODO GoogleClassifier.getCategories()
    @Override
    public String[] getCategories() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Output getGoogleOutput(String text) throws IOException {
        Input input = new Input();
        InputInput inputInput = new InputInput();
        List<Object> params = Arrays.asList(new Object[]{text});
        inputInput.setCsvInstance(params);
        input.setInput(inputInput);
        Output output =
                client.hostedmodels().predict("414649711441", "sample.sentiment", input).execute();
        //client.trainedmodels().predict("109973074802", "sentiment", input).execute();
        return output;
    }
}
