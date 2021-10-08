package it.javaboss;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SendDocumentImageHandler extends PostRequestHandler {

    public SendDocumentImageHandler(DocumentPartFactory documentPartFactory) {
        super(documentPartFactory);
        this.partType = "DOCUMENT_IMAGES";
        this.controllerMappingUrl = "/immagini/add";
    }

    // fare un refactoring
    @Override
    public void sendPostRequest(JSONObject document, long id) throws IOException, InterruptedException, JSONException {
        JSONArray documentImages = (JSONArray) documentPartFactory.create(partType, document); // da cambiare

        for (int i = 0; i < documentImages.length(); i++) {
            JSONObject element = documentImages.getJSONObject(i);
            JSONObject documentImage = (JSONObject) documentPartFactory.create("DOCUMENT_IMAGE", element); // da cambiare
            documentImage.put("documentId", id);

            HttpPost request = new HttpPost(SERVER_URL + controllerMappingUrl);
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            StringEntity params = new StringEntity(documentImage.toString());
            request.addHeader("content-type", "application/json");
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            long imageId = Long.parseLong(EntityUtils.toString(response.getEntity(), "UTF-8")); // id dell'immagine associata

            JSONArray securityFeatures = (JSONArray) element.get("listOfDocumentSecurity");

            for (int j = 0; j < securityFeatures.length(); j++) {
                JSONObject securityFeature = securityFeatures.getJSONObject(j);
                securityFeature.put("imageId", imageId);
                securityFeature.put("documentId", id);

                request = new HttpPost(SERVER_URL + "/sicurezze/add");
                httpClient = HttpClientBuilder.create().build();
                params = new StringEntity(securityFeature.toString());
                request.addHeader("content-type", "application/json");
                request.setEntity(params);
                response = httpClient.execute(request);
            }
        }
    }

}
