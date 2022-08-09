package it.webcrawler.factory;

import it.webcrawler.constants.StaticData;

public class CreateIdentityDocWithGeneralFields extends CreateIdentityDocWithFields {

    public CreateIdentityDocWithGeneralFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentFieldNames();
    }
}


