package it.javaboss;

import com.google.common.collect.PeekingIterator;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ExtractDocumentFieldsStrategy implements ExtractDocumentDataStrategy {

    private Map<String, String> nameAliases;

    // eventualmente da cambiare
    public ExtractDocumentFieldsStrategy() {
        nameAliases = Map.of("Document Category:", "tipoDocumento",
                            "Issuing Country:", "nazionalita",
                            "Maximum validity:", "validita",
                            "Extension is possible:", "estensione possibile"); // da cambiare questo
    }

    public void executeStrategy(PeekingIterator<Element> tags, JSONObject document) {
        String text = tags.next().ownText();
        while (tags.hasNext()) {
            boolean textIsContained = StaticData.INSTANCE.getDocumentFieldNames().contains(text);
            if (textIsContained) {
                String fieldName = nameAliases.get(text);
                //String fieldName = text;
                text = tags.next().ownText();
                while (tags.hasNext() && text.isEmpty()) text = tags.next().ownText();
                try {
                    document.put(fieldName, text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            text = tags.peek().ownText();
            textIsContained = StaticData.INSTANCE.getDocumentSideNames().contains(text);
            if (textIsContained) break;
            tags.next();
        }
    }

}
