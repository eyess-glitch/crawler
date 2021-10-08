package it.javaboss;

import com.google.common.collect.PeekingIterator;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

public interface ExtractDocumentDataStrategy {

    void executeStrategy(PeekingIterator<Element> tags, JSONObject document);
}
