package it.javaboss;

import com.google.common.collect.PeekingIterator;
import com.sleepycat.je.utilint.Stat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Element;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ExtractDocumentImagesStrategy implements ExtractDocumentDataStrategy {

    public void executeStrategy(PeekingIterator<Element> tags, JSONObject document) {
        JSONArray listOfSideInfos = new JSONArray();
        try {
            while (tags.hasNext()) {
                Element tag = tags.next();
                String text = tag.ownText();
                boolean textContainedInDocumentSidesName = StaticData.INSTANCE.getDocumentSideNames().contains(text);

                if (textContainedInDocumentSidesName) {
                    // Prendo l'immagine del documento con relativo nome
                    JSONObject documentSideInfo = new JSONObject();
                    String documentSideName = text;

                    while (tags.hasNext()) {
                        tag = tags.next();
                        if (tag.tagName().equals("img")) break;
                    }

                    //System.out.println("Nome lato : " + text + ", urlImmagine : " + tag.absUrl("src"));
                    documentSideInfo.put("sideName", documentSideName);
                    documentSideInfo.put("imageURL", tag.absUrl("src"));

                    //aggiungere nome e URL;
                    // ora mi accingo a, per questo lato del documento, estrarre tutte le feature di sicurezza, url delle immagini con relativi nomi
                    JSONArray listOfDocumentSecurity = new JSONArray();

                    while (tags.hasNext()) {
                        tag = tags.next();
                        text = tag.ownText();
                        if (tag.tagName().equals("img")) {
                            String altAttribute = tag.attr("alt");
                            if (altAttribute.startsWith("Image")) {
                                int ofPosition = altAttribute.indexOf("of");
                                String securityFeatureName = altAttribute.substring(ofPosition + 3, altAttribute.length());
                                String url = tag.absUrl("src");

                                JSONObject securityInfo = new JSONObject();
                                securityInfo.put("securityFeatureName", securityFeatureName);
                                securityInfo.put("securityFeatureImage", url);
                                listOfDocumentSecurity.put(securityInfo);

                                //System.out.println("nome security feature : " + securityFeatureName + ", urlImmagine : " + url);
                            }
                        }
                        if (tags.hasNext()) {
                            Element nextTag = tags.peek();
                            String nextText = nextTag.ownText();
                            textContainedInDocumentSidesName = StaticData.INSTANCE.getDocumentSideNames().contains(nextText);
                            if (textContainedInDocumentSidesName) break;
                        }
                    }

                    documentSideInfo.put("listOfDocumentSecurity", listOfDocumentSecurity);
                    listOfSideInfos.put(documentSideInfo);
                }
            }

            document.put("images", listOfSideInfos);
        } catch(JSONException e) { e.printStackTrace(); }

    }
}




