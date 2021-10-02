package it.javaboss;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class DocumentDataExtractor implements Extractor<String> {

    private Map<String, String> nameAliases;

    public DocumentDataExtractor() {
        // da cambiare
        nameAliases = Map.of("Document Category:", "tipoDocumento",
                "Issuing Country:", "nazionalita",
                "Maximum validity:", "validita",
                "Extension is possible:", "estensione possibile");
    }


    public void extract(String data)  {
        Document htmlPage = Jsoup.parse(data);
        htmlPage.setBaseUri("https://www.consilium.europa.eu/prado/prado-documents/ARM/index.html"); // da cambiare

        JSONObject document = new JSONObject();

        Elements elements = htmlPage.body().select("*"); // prendo tutti gli elementi della pagina html
        PeekingIterator<Element> tags = Iterators.peekingIterator(elements.iterator());

        advanceToFieldsOfInterest(tags);

        extractFields(tags, document);
        extractImages(tags, document);

        try {
            System.out.println(document.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new SimpleExecutor().storeOperation(new SendDocumentOperation(document));
    }



    private void extractFields(PeekingIterator<Element> tags, JSONObject document) {
        String text = tags.next().ownText();
        while (tags.hasNext()) {
            boolean textIsContained = StaticData.INSTANCE.getDocumentFieldNames().contains(text)
                                      || StaticData.INSTANCE.getDocumentValidity().contains(text);
            if (textIsContained) {
                //String fieldName = nameAliases.get(text);
                String fieldName = text;
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

    private void extractImages(PeekingIterator<Element> tags, JSONObject document) {
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


    private void advanceToFieldsOfInterest(PeekingIterator<Element> tags) {
        while (tags.hasNext()) {
            String text = tags.peek().ownText();
            if (text.equals("Title:")) break;
            tags.next();
        }
    }



}


