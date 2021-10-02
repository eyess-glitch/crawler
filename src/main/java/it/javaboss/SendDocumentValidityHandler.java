package it.javaboss;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SendDocumentValidityHandler extends PostRequestHandler {

    public SendDocumentValidityHandler(DocumentPartFactory documentPartFactory) {
        super(documentPartFactory);
        this.partType = "DOCUMENT_VALIDITY";
        this.controllerMappingUrl = "/validita-documenti/add"; // da cambiare
    }

}
