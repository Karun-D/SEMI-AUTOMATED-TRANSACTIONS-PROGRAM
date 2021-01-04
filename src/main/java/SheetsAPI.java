import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class SheetsAPI {
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = SheetsAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Read cells from Personal Expense Tracker spreadsheet:
     * https://docs.google.com/spreadsheets/d/16oCSqjHPE5TWFGwG95wmkB4j96U9mdJbwtg2_OwlqYU/edit#gid=0
     */
    public static String readCell(String range) {
        String return_value = "";
        // String range = "Sheet1!" + cell_range;
        
        try {
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final String spreadsheetId = "16oCSqjHPE5TWFGwG95wmkB4j96U9mdJbwtg2_OwlqYU";

            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        
            // Locate cell using range parameter
            ValueRange result = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();

            List<List<Object>> values = result.getValues();

            for (List row : values){
                return_value = String.valueOf(row.get(0));
                return_value = return_value.substring(1, return_value.length()).replace(",", "");
            }

        } catch (Exception e) {
            System.out.println("Error, exception has occured in insertCell method: " + e.getMessage());
        }

        return return_value;
    }

    /**
     * Insert cells into Personal Expense Tracker spreadsheet:
     * https://docs.google.com/spreadsheets/d/16oCSqjHPE5TWFGwG95wmkB4j96U9mdJbwtg2_OwlqYU/edit#gid=0
     */
    public static void insertCell(String range, String value) {
        try {
            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            final String spreadsheetId = "16oCSqjHPE5TWFGwG95wmkB4j96U9mdJbwtg2_OwlqYU";

            Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();

            // Initialize cell body using value parameter.
            ValueRange body = new ValueRange().setValues(
                Arrays.asList(
                    Arrays.asList(value)
                )
            );
        
            // Edit cell using range parameter and cell body.
            UpdateValuesResponse result = service.spreadsheets().values()
                .update (spreadsheetId, range, body)
                .setValueInputOption("USER_ENTERED")
                .execute();

        } catch (Exception e) {
            System.out.println("Error, exception has occured in insertCell method: " + e.getMessage());
        }
    }
}  