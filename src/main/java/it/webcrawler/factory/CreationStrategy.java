package it.webcrawler.factory;

import org.json.JSONException;
import org.json.JSONObject;

public interface CreationStrategy {

    Object createDocumentPart(JSONObject document) throws JSONException;
}

