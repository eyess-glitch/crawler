package it.javaboss;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateDocWithImages implements CreationStrategy {

    public Object createDocumentPart(JSONObject document) throws JSONException {
        return document.get("images");
    }
}
