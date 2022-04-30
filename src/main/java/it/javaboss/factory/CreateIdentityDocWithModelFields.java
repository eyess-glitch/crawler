package it.javaboss.factory;

import it.javaboss.constants.StaticData;

public class CreateIdentityDocWithModelFields extends CreateIdentityDocWithFields {

    public CreateIdentityDocWithModelFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentModelFields();
    }

}
