package com.example.Automation;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleSheetService {

    private static final String APPLICATION_NAME = "Automation";
    // Use Google's JacksonFactory
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS_READONLY);
    private static final String SPREADSHEET_ID = "1YfNURZSdy05CdWIEjGBSBsXQCl_wPtpzM7Rr9V98hyc";
//    private static final String RANGE = "'Form_Responses1'!A1";
    private static final String RANGE = "Sheet1!A2:E"; // Remove single quotes



    public Sheets getSheetsService() throws IOException, GeneralSecurityException {
        InputStream in = new FileInputStream("C:\\Users\\nagas\\Downloads\\Automation\\Automation\\src\\main\\resources\\credentials.json");
        GoogleCredential credential = GoogleCredential.fromStream(in).createScoped(SCOPES);
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public List<List<Object>> fetchFeedbackData() throws IOException, GeneralSecurityException {
        Sheets sheetsService = getSheetsService();
        ValueRange response = sheetsService.spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute();
        return response.getValues();
    }
}
