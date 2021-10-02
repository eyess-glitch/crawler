package it.javaboss;

import com.google.common.collect.PeekingIterator;
import org.apache.wink.json4j.OrderedJSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.Iterator;

public interface ExtractDocumentDataStrategy {

    void executeStrategy(PeekingIterator<Element> tags, JSONObject document);
}
