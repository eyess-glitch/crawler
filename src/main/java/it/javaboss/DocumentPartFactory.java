package it.javaboss;

import org.json.JSONException;
import org.json.JSONObject;

public interface DocumentPartFactory {

    Object create(String partType, JSONObject document) throws JSONException;
}
