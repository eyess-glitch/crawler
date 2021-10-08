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

public class PostRequestHandler {

    private PostRequestHandler next;

    protected final static String SERVER_URL = "http://localhost:8090";
    protected String partType, controllerMappingUrl;

    protected DocumentPartFactory documentPartFactory;

    public PostRequestHandler(DocumentPartFactory documentPartFactory) {
        this.documentPartFactory = documentPartFactory;
    }

    public void sendPostRequest(JSONObject document, long id) throws IOException, InterruptedException, JSONException {
        //System.out.println(document.toString(4));
        JSONObject partOfADocument = (JSONObject) documentPartFactory.create(partType, document);
        partOfADocument.put("documentId", id);
        HttpPost request = new HttpPost(SERVER_URL + controllerMappingUrl);
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        StringEntity params = new StringEntity(partOfADocument.toString());
        request.addHeader("content-type", "application/json");
        request.setEntity(params);
        CloseableHttpResponse response = httpClient.execute(request);
        long documentId = Long.parseLong(EntityUtils.toString(response.getEntity(), "UTF-8"));
        if (document.length() > 0 && next != null) next.sendPostRequest(document, documentId);
    }


    public PostRequestHandler linkWith(PostRequestHandler next) {
        this.next = next;
        return next;
    }

}
