package it.webcrawler.factory;

import org.json.JSONException;
import org.json.JSONObject;

public interface DocumentPartFactory {

    Object createPart(String partType, JSONObject document) throws JSONException;
}


