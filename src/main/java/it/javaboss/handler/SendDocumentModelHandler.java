package it.javaboss.handler;

import it.javaboss.factory.DocumentPartFactory;

public class SendDocumentModelHandler extends PostRequestHandler {

    public SendDocumentModelHandler(DocumentPartFactory documentPartFactory) {
        super(documentPartFactory);
        this.partType = "DOCUMENT_MODEL";
        this.controllerMappingUrl = "/modelli-documento/add";
    }
}
