package it.webcrawler.constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum StaticData {

    INSTANCE;

    private final Set<String> documentValidity = new HashSet<>();
    private final Set<String> documentFieldNames = new HashSet<>();
    private final Set<String> documentModelFields = new HashSet<>();
    private final Set<String> documentSideNames = new HashSet<>();

    StaticData() {
        Collections.addAll(documentModelFields, "Issuing Country:", "Document Category:", "Document:");
        Collections.addAll(documentFieldNames, "Overall construction:", "Valid:", "Not valid after:",
                                                         "First issued on:", "Format width (mm):", "Format height (mm):",
                                                         "Number of pages:");
        Collections.addAll(documentSideNames, "Outside back cover", "Outside front cover",
                                                        "Inside front cover", "Inside back cover",
                                                        "Biodata page", "Inner page(s)", "Cover",
                                                        "Document element (if no other qualifier is applicable)",
                                                        "Integrated biodata card - recto (identity)",
                                                        "Integrated card - verso",
                                                        "Recto - identity", "Verso",
                                                        "Document (valid for all physical document elements, including cover)",
                                                         "Document except cover");
        Collections.addAll(documentValidity, "Maximum validity:", "Extension is possible:", "Minimum age for validity:",
                                                       "Maximum age for validity:");
    }

    public Set<String> getDocumentValidity() { return Collections.unmodifiableSet(documentValidity); }

    public Set<String> getDocumentFieldNames() {
        return Collections.unmodifiableSet(documentFieldNames);
    }

    public Set<String> getDocumentSideNames() {
        return Collections.unmodifiableSet(documentSideNames);
    }

    public Set<String> getDocumentModelFields() {
        return Collections.unmodifiableSet(documentModelFields);
    }
}


