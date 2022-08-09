package it.webcrawler.factory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

public class CreateIdentityDocWithFields implements CreationStrategy {

    protected Set<String> documentFieldNames;

    public Object createDocumentPart(JSONObject document) throws JSONException {
        JSONObject documentPart = new JSONObject();
        for (String fieldName : documentFieldNames) {
            try {
                String fieldValue = (String) document.get(fieldName);
                String filteredFieldName = fieldName.replace(":","");
                documentPart.put(filteredFieldName, fieldValue);
            } catch(JSONException e) {
                System.out.println("Field " + fieldName + " not present");
            }
        }
        return documentPart;
    }
}

