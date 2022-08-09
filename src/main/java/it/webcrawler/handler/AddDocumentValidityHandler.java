package it.webcrawler.handler;

import it.webcrawler.factory.DocumentPartFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddDocumentValidityHandler extends AddDocumentHandler {

    public AddDocumentValidityHandler(Connection connection, DocumentPartFactory documentPartFactory, String partType) {
        super(connection, documentPartFactory);
        this.partType = partType;
    }

    //AGGIUSTARE LA QUESTIONE DI DOCUMENTID
    public long insertRowsIntoDatabase(JSONObject document) throws JSONException, SQLException {
        String query = "INSERT INTO document_validity(field_value, document_model, validity_type) VALUES  (?,?,?)";
        long documentId = ((Number) document.remove("documentId")).longValue();
        JSONArray keys = document.names();

        if (keys != null) { // nessun campo di validita' presente
            for (int i = 0; i < keys.length(); i++) {
                String fieldName = keys.getString(i);
                String fieldValue = document.getString(fieldName);
                long documentValidityTypeId = -1L;

                documentValidityTypeId = insertRowIfNotExist(fieldName, "validity_type", "field_description");

                PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, fieldValue);
                preparedStatement.setLong(2, documentId);
                preparedStatement.setLong(3, documentValidityTypeId);

                int affectedRows = preparedStatement.executeUpdate();
            }
        }

        return documentId;
    }
}

