package it.javaboss.handler;

import it.javaboss.factory.DocumentPartFactory;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class PostRequestHandler implements Handler {

    private PostRequestHandler next;

    protected final static String SERVER_URL = "http://localhost:8090";
    protected String partType, controllerMappingUrl;

    protected DocumentPartFactory documentPartFactory;

    public PostRequestHandler(DocumentPartFactory documentPartFactory) {
        this.documentPartFactory = documentPartFactory;
    }

    public void sendPostRequest(JSONObject document, long id) throws IOException, InterruptedException, JSONException {
        JSONObject partOfADocument = (JSONObject) documentPartFactory.createPart(partType, document);
        partOfADocument.put("documentId", id);
        CloseableHttpResponse response = buildAndSendHttpRequest(partOfADocument.toString(), controllerMappingUrl);
        long documentId = Long.parseLong(EntityUtils.toString(response.getEntity(), "UTF-8"));
        if (document.length() > 0 && next != null) next.sendPostRequest(document, documentId);
    }

    public CloseableHttpResponse buildAndSendHttpRequest(String requestBody, String controllerMappingUrl) throws IOException {
        HttpPost request = new HttpPost(SERVER_URL + controllerMappingUrl);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity params = new StringEntity(requestBody);
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        return httpClient.execute(request);
    }



    public PostRequestHandler linkWith(PostRequestHandler next) {
        this.next = next;
        return next;
    }

}
