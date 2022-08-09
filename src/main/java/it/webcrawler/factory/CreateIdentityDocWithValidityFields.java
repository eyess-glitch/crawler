package it.webcrawler.factory;

import it.webcrawler.constants.StaticData;

public class CreateIdentityDocWithValidityFields extends CreateIdentityDocWithFields {

    public CreateIdentityDocWithValidityFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentValidity();
    }
}
