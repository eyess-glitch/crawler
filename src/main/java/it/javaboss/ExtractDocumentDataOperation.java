package it.javaboss;

import edu.uci.ics.crawler4j.parser.HtmlParseData;


public class ExtractDocumentDataOperation implements Operation {

    private Extractor extractor;

    private String data;


    public ExtractDocumentDataOperation(Extractor extractor, String data) {
        this.extractor = extractor;
        this.data = data;
    }

    public void performTask() {
        extractor.extract(data);
    }


}
