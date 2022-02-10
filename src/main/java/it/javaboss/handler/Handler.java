package it.javaboss.handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public interface Handler {

    void sendPostRequest(JSONObject document, long id) throws IOException, InterruptedException, JSONException;

}
