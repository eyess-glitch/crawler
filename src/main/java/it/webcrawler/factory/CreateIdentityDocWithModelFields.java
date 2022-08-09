package it.webcrawler.factory;

import it.webcrawler.constants.StaticData;

public class CreateIdentityDocWithModelFields extends CreateIdentityDocWithFields {

    public CreateIdentityDocWithModelFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentModelFields();
    }

}
