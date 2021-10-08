package it.javaboss;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum StaticData {

    INSTANCE;

    private Set<String> documentValidity = new HashSet<>();
    private Set<String> documentFieldNames = new HashSet<>();
    private Set<String> documentSideNames = new HashSet<>();

    // eventualmente da mettere in un file di testo
    private StaticData() {
        Collections.addAll(documentFieldNames, "Issuing Country:", "Document Category:");
        Collections.addAll(documentSideNames, "Outside back cover", "Outside front cover",
                                                        "Inside front cover", "Inside back cover",
                                                        "Biodata page", "Inner page(s)", "Cover");
        Collections.addAll(documentValidity, "Maximum validity:", "Extension is possible:");
    }

    public Set<String> getDocumentValidity() { return Collections.unmodifiableSet(documentValidity); }

    public Set<String> getDocumentFieldNames() {
        return Collections.unmodifiableSet(documentFieldNames);
    }

    public Set<String> getDocumentSideNames() {
        return Collections.unmodifiableSet(documentSideNames);
    }
}


