package it.javaboss.factory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ConcreteFactory implements DocumentPartFactory {

    private Map<String,CreationStrategy> creationStrategiesPerType;

    public ConcreteFactory() {
        this.creationStrategiesPerType = new HashMap<>(){{
            put("DOCUMENT_MODEL", new CreateIdentityDocWithModelFields());
            put("DOCUMENT_VALIDITY", new CreateIdentityDocWithValidityFields());
            put("DOCUMENT_FIELD", new CreateIdentityDocWithGeneralFields());
            put("DOCUMENT_IMAGE", new CreateIdentityDocWithImage());
        }};

    }

    public Object createPart(String partType, JSONObject document) throws JSONException {
        return creationStrategiesPerType.get(partType).createDocumentPart(document);
    }

}



