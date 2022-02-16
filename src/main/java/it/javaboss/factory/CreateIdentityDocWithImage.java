package it.javaboss.factory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateIdentityDocWithImage implements CreationStrategy {

    public Object createDocumentPart(JSONObject document) throws JSONException {
        JSONObject documentSideImage = new JSONObject();
        documentSideImage.put("images", document.remove("images"));
        return documentSideImage;
    }
}
