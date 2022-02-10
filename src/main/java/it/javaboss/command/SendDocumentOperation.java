package it.javaboss.command;

import it.javaboss.factory.ConcreteFactory;
import it.javaboss.handler.SendDocumentImageHandler;
import it.javaboss.handler.SendDocumentModelHandler;
import it.javaboss.handler.SendDocumentValidityHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SendDocumentOperation implements Operation {

    private JSONObject identityDocument;

    public SendDocumentOperation(JSONObject identityDocument) {
        this.identityDocument = identityDocument;
    }

    public void performTask() {
        ConcreteFactory factory = new ConcreteFactory();
        SendDocumentModelHandler sendDocumentModelHandler = new SendDocumentModelHandler(factory);
        SendDocumentValidityHandler sendValidityHandler = new SendDocumentValidityHandler(factory);
        SendDocumentImageHandler sendImageHandler = new SendDocumentImageHandler(factory);
        sendDocumentModelHandler.linkWith(sendValidityHandler).linkWith(sendImageHandler);
        try {
            sendDocumentModelHandler.sendPostRequest(identityDocument, 0); // l'id nell'invio della prima richiesta e' uno stub
        } catch (InterruptedException | IOException | JSONException e) {
            e.printStackTrace();
        }
    }


}
