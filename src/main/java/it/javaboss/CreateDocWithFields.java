package it.javaboss;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class CreateDocWithFields implements CreationStrategy {

    protected Set<String> documentFieldNames;

    public Object createDocumentPart(JSONObject document) throws JSONException {
        JSONObject documentPart = new JSONObject();
        for (String fieldName : documentFieldNames) {
            String fieldValue = (String) document.remove(fieldName);
            if (!(fieldValue == null)) {
                try {
                    documentPart.put(fieldName, fieldValue);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return documentPart;
    }
}

