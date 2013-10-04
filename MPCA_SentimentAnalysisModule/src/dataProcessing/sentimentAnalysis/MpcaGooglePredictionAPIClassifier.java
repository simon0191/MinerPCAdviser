package dataProcessing.sentimentAnalysis;

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

    private String[] categories;
    /**
     * Be sure to specify the name of your application. If the application name
     * is {@code null} or blank, the application will log a warning. Suggested
     * format is "MyCompany-ProductName/1.0".
     */
    private static final String APPLICATION_NAME = "MinerPCAdviser";
    /**
     * Directory to store user credentials.
     */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File("store/prediction");
    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to
     * make it a single globally shared instance across your application.
     */
    private static FileDataStoreFactory dataStoreFactory;
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport httpTransport;
    private static Prediction client;

    public MpcaGooglePredictionAPIClassifier(String[] categories) throws GeneralSecurityException, IOException, Exception {
        this.categories = Arrays.copyOf(categories, categories.length);



        // initialize the transport
        httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // initialize the data store factory
        dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

        // authorization
        Credential credential = authorize();
        // set up global Prediction instance
        client = new Prediction.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();

    }

    @Override
    public String classify(String text) throws IOException {
        Input input = new Input();


        InputInput inputInput = new InputInput();
        List<Object> params = Arrays.asList(new Object[]{"I'm Very happy"});

        inputInput.setCsvInstance(params);
        input.setInput(inputInput);



        Output output =
                //client.hostedmodels().predict("414649711441", "sample.sentiment", input).execute();
                client.trainedmodels().predict("109973074802", "sentiment", input).execute();
        //System.out.println(output.toPrettyString());
        //output.
        return output.getOutputLabel().toUpperCase();
    }

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {
        // load client secrets

        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(new FileInputStream(new File(dataStoreFactory.getDataDirectory(), "/client_secrets.json"))));

        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            System.out.println(
                    "Overwrite the src/main/resources/client_secrets.json file with the client secrets file "
                    + "you downloaded from the Quickstart tool or manually enter your Client ID and Secret "
                    + "from https://code.google.com/apis/console/?api=prediction#project:109973074802 "
                    + "into src/main/resources/client_secrets.json");
            System.exit(1);
        }

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
                .setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    @Override
    public String[] getCategories() {
        return this.categories;
    }

    public static void main(String[] args) {
        try {
            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();

            // initialize the data store factory
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);

            // authorization
            Credential credential = authorize();
            // set up global Prediction instance
            client = new Prediction.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();

            System.out.println("Success! Now add code here.");

            Input input = new Input();

            InputInput inputInput = new InputInput();
            List<Object> params = Arrays.asList(new Object[]{"I'm Very happy"});

            inputInput.setCsvInstance(params);
            input.setInput(inputInput);

            Output output =
                    //client.hostedmodels().predict("414649711441", "sample.sentiment", input).execute();
                    client.trainedmodels().predict("109973074802", "sentiment", input).execute();
            System.out.println(output.toPrettyString());

            List<Output.OutputMulti> outs = output.getOutputMulti();
            for (Output.OutputMulti o : outs) {
                //o.
            }


        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        System.exit(1);
    }
}
