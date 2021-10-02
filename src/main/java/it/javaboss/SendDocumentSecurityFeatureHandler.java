package it.javaboss;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/*
public class SendDocumentSecurityFeatureHandler extends SendDocumentHandler {

    private final static int

    public int sendRequest(JSONObject document, int id)  {
        int documentId = 0;
        try {
            HttpClient client = HttpClient.newHttpClient();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(SERVER_URL + DOCUMENT_CONTROLLER_MAPPING))
                    .POST(HttpRequest.BodyPublishers.ofString(document.toString()))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            documentId = Integer.parseInt(response.body());
        } catch(InterruptedException | IOException e) { e.printStackTrace(); }

        return documentId;
    }
}
*/