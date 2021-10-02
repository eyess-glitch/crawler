package it.javaboss;

import org.json.JSONException;
import org.json.JSONObject;

public class CreateDocWithImage implements CreationStrategy {

    public Object createDocumentPart(JSONObject document) throws JSONException {
        JSONObject documentWithImage = new JSONObject();
        documentWithImage.put("sideName", document.remove("sideName"));
        documentWithImage.put("imageURL", document.remove("imageURL"));
        return documentWithImage;
    }
}
