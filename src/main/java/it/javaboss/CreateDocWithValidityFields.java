package it.javaboss;

public class CreateDocWithValidityFields extends CreateDocWithFields {

    public CreateDocWithValidityFields() {
        documentFieldNames = StaticData.INSTANCE.getDocumentValidity();
    }
}
