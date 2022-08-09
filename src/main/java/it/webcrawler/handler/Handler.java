package it.webcrawler.handler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLException;

public interface Handler {

    void handleRequest(JSONObject document, long id) throws IOException, InterruptedException, JSONException, SQLException;

}
