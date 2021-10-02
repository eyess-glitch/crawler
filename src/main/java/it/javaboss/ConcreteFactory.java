package it.javaboss;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConcreteFactory implements DocumentPartFactory {

    private Map<String,CreationStrategy> creationStrategiesPerType;

    public ConcreteFactory() {
        this.creationStrategiesPerType = new HashMap<>(){{
            put("DOCUMENT_MODEL", new CreateDocWithGeneralFields());
            put("DOCUMENT_VALIDITY", new CreateDocWithValidityFields());
            put("DOCUMENT_IMAGES", new CreateDocWithImages());
            put("DOCUMENT_IMAGE", new CreateDocWithImage());
        }};

    }

    public Object create(String partType, JSONObject document) throws JSONException {
        return creationStrategiesPerType.get(partType).createDocumentPart(document);
    }

}



