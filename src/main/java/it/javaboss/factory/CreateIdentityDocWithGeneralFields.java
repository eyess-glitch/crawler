package it.javaboss.factory;

import it.javaboss.constants.StaticData;

public class CreateIdentityDocWithGeneralFields extends CreateIdentityDocWithFields {

    public CreateIdentityDocWithGeneralFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentFieldNames();
    }

}
