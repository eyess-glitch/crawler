package it.javaboss.extractor;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import it.javaboss.command.FIFOExecutor;
import it.javaboss.command.addDocumentOperation;
import it.javaboss.constants.StaticData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DocumentDataExtractor implements Extractor<String> {

    public void extract(String data)  {
        Document htmlPage = Jsoup.parse(data);
        htmlPage.setBaseUri("https://www.consilium.europa.eu/prado/prado-documents/ARM/index.html"); // da cambiare

        JSONObject identityDocument = new JSONObject();

        Elements elements = htmlPage.body().select("*"); // prendo tutti gli elementi della pagina html
        PeekingIterator<Element> it = Iterators.peekingIterator(elements.iterator());

        String documentName = advanceUntilNotFound(it, "Document:").split(":")[1].trim();
        advanceUntilNotFound(it, "Title:");

        extractFields(it, identityDocument);
        extractImagesURL(it, identityDocument);
        
        try {
            identityDocument.put("Document:", documentName);
            System.out.println(identityDocument.toString(4));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        FIFOExecutor executor = new FIFOExecutor();
        executor.storeOperation(new addDocumentOperation(identityDocument));
        executor.execute();
    }


    private void extractFields(PeekingIterator<Element> it, JSONObject identityDocument) {
        String text = it.next().ownText();
        while (it.hasNext()) {
            boolean fieldOfInterestContained = StaticData.INSTANCE.getDocumentFieldNames().contains(text) || StaticData.INSTANCE.getDocumentValidity().contains(text);

            if (fieldOfInterestContained) {
                String fieldName = text;
                text = it.next().ownText();
                while (it.hasNext() && text.isEmpty()) text = it.next().ownText();
                try {
                    identityDocument.put(fieldName, text); // text sarebbe in tal caso il valore del campo
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            text = it.peek().ownText();
            fieldOfInterestContained = StaticData.INSTANCE.getDocumentSideNames().contains(text);
            if (fieldOfInterestContained) break;

            it.next();
        }
    }

    //TODO VEDERE COME GESTIRE NEL CASO SI PRESENTINO PIU' IMMAGINI PER UN LATO O FORSE NON GESTIRLO PROPRIO
    private void extractImagesURL(PeekingIterator<Element> it, JSONObject identityDocument) {
        JSONArray listOfSidesInfo = new JSONArray();

        try {
            while (it.hasNext()) {
                Element htmlTag = it.next();
                String textBetweenTags = htmlTag.ownText();
                boolean isAdocumentSideName = StaticData.INSTANCE.getDocumentSideNames().contains(textBetweenTags);

                if (isAdocumentSideName) {
                    JSONObject documentSideInfo = new JSONObject();
                    String documentSideName = textBetweenTags;

                    while (it.hasNext()) {
                        htmlTag = it.next();
                        if (htmlTag.tagName().equals("img")) break;
                    }

                    documentSideInfo.put("sideName", documentSideName);
                    documentSideInfo.put("imageURL", htmlTag.absUrl("src"));

                    JSONArray listOfDocumentSecurity = extractSideSecurityFeatures(it);

                    documentSideInfo.put("listOfDocumentSecurity", listOfDocumentSecurity);
                    listOfSidesInfo.put(documentSideInfo);
                }
            }

            identityDocument.put("images", listOfSidesInfo);
        } catch(JSONException e) { e.printStackTrace(); }

    }

    //TODO EVENTUALMENTE SISTEMARE IL FATTO CHE NON PRENDE PIU' IMMAGINI
    private JSONArray extractSideSecurityFeatures(PeekingIterator<Element> it) {
        JSONArray listOfDocumentSecurity = new JSONArray();
        boolean isSecurityFeature = false, securityFeaturePassed = false;

        while (it.hasNext()) {
            Element htmlTag = it.next();
            String text = htmlTag.ownText();
            isSecurityFeature = text.equals("Security features:");

            if (isSecurityFeature) securityFeaturePassed = true;

            if (htmlTag.tagName().equals("img") && securityFeaturePassed) {
                String altAttribute = htmlTag.attr("alt");
                if (altAttribute.startsWith("Image")) {
                    addSecurityInfoToList(listOfDocumentSecurity, altAttribute, htmlTag);
                    securityFeaturePassed = false;
                }
            }

            if (it.hasNext()) {
                Element nextTag = it.peek();
                String nextText = nextTag.ownText();
                boolean isAdocumentSideName = StaticData.INSTANCE.getDocumentSideNames().contains(nextText);
                if (isAdocumentSideName) break;
            }

        }

        return listOfDocumentSecurity;
    }

    private void addSecurityInfoToList(JSONArray listOfDocumentSecurity, String altAttribute, Element htmlTag) {
        int ofPosition = altAttribute.indexOf("of");
        String securityFeatureName = altAttribute.substring(ofPosition + 3);
        String url = htmlTag.absUrl("src");

        JSONObject securityInfo = new JSONObject();

        try {
            securityInfo.put("securityFeatureName", securityFeatureName);
            securityInfo.put("securityFeatureImage", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        listOfDocumentSecurity.put(securityInfo);
    }

    private String advanceUntilNotFound(PeekingIterator<Element> it, String fieldtoFind) {
        String  fieldOfInterest = "";
        while (it.hasNext()) {
            String text = it.peek().ownText();
            if (text.startsWith(fieldtoFind)) {
                fieldOfInterest = text;
                break;
            }
            it.next();
        }
        return fieldOfInterest;
    }



}


