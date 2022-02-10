package it.javaboss.handler;

import it.javaboss.factory.DocumentPartFactory;

public class SendDocumentValidityHandler extends PostRequestHandler {

    public SendDocumentValidityHandler(DocumentPartFactory documentPartFactory) {
        super(documentPartFactory);
        this.partType = "DOCUMENT_VALIDITY";
        this.controllerMappingUrl = "/validita-documenti/add"; // da cambiare
    }

}
