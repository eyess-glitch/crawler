package it.javaboss.handler;

import it.javaboss.factory.DocumentPartFactory;
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

public class SendDocumentImageHandler extends PostRequestHandler {

    public SendDocumentImageHandler(DocumentPartFactory documentPartFactory) {
        super(documentPartFactory);
        this.partType = "DOCUMENT_IMAGE";
        this.controllerMappingUrl = "/immagini/add";
    }


    @Override
    public void sendPostRequest(JSONObject document, long id) throws IOException, InterruptedException, JSONException {
        JSONArray documentImages = (JSONArray) document.get("images");
        for (int i = 0; i < documentImages.length(); i++) {
            JSONObject element = documentImages.getJSONObject(i);
            JSONObject documentImage = (JSONObject) documentPartFactory.createPart(partType, element); // da cambiare
            documentImage.put("documentId", id);
            CloseableHttpResponse response = buildAndSendHttpRequest(documentImage.toString(), controllerMappingUrl);
            long imageId = Long.parseLong(EntityUtils.toString(response.getEntity(), "UTF-8")); // id dell'immagine associata
            JSONArray securityFeatures = (JSONArray) element.get("listOfDocumentSecurity");
            for (int j = 0; j < securityFeatures.length(); j++) {
                JSONObject securityFeature = securityFeatures.getJSONObject(j);
                securityFeature.put("imageId", imageId);
                securityFeature.put("documentId", id);
                buildAndSendHttpRequest(securityFeature.toString(), "/sicurezze/add");
            }
        }
    }

}
