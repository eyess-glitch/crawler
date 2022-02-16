package it.javaboss.command;

import it.javaboss.extractor.Extractor;


public class ExtractDocumentDataOperation implements Operation {

    private final Extractor<String> extractor;

    private final String htmlPage; // pagina html dal quale estrarre le informazioni di un determinato documento

    public ExtractDocumentDataOperation(Extractor<String> extractor, String htmlPage) {
        this.extractor = extractor;
        this.htmlPage = htmlPage;
    }

    public void performTask() {
        extractor.extract(htmlPage);
    }


}
