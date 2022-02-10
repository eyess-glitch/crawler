package it.javaboss.factory;

import it.javaboss.constants.StaticData;

public class CreateIdentityDocWithValidityFields extends CreateIdentityDocWithFields {

    public CreateIdentityDocWithValidityFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentValidity();
    }
}
