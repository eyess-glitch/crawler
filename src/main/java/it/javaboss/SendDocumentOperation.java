package it.javaboss;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SendDocumentOperation implements Operation {

    private JSONObject document;

    public SendDocumentOperation(JSONObject document) {
        this.document = document;
    }

    // eventualmente da aggiustare
    public void performTask() {
        ConcreteFactory factory = new ConcreteFactory();
        SendDocumentModelHandler sendDocumentModelHandler = new SendDocumentModelHandler(factory);
        SendDocumentValidityHandler sendValidityHandler = new SendDocumentValidityHandler(factory);
        sendDocumentModelHandler.linkWith(sendValidityHandler);
        //SendDocumentImageHandler sendImageHandler = new SendDocumentImageHandler(factory);
        //sendDocumentModelHandler.linkWith(sendValidityHandler).linkWith(sendImageHandler);
        try {
            sendDocumentModelHandler.sendPostRequest(document, 0); // l'id nell'invio della prima richiesta e' uno stub
        } catch (InterruptedException | IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}
