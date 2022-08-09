package it.webcrawler.handler;

import it.webcrawler.factory.DocumentPartFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public class AddDocumentFieldHandler extends AddDocumentHandler {

    public AddDocumentFieldHandler(Connection connection, DocumentPartFactory documentPartFactory, String partType) {
        super(connection, documentPartFactory);
        this.partType = partType;
    }

    public long insertRowsIntoDatabase(JSONObject document) throws JSONException, SQLException {
        long documentId = ((Number) document.remove("documentId")).longValue(), documentFieldId = -1L;
        String query1 = "INSERT INTO document_field(field_type_id, field_value) VALUES(?,?)";
        String query2 = "INSERT INTO document_model_document_fields(document_models_id, document_fields_id) VALUES(?,?)";
        JSONArray keys = document.names();

        for (int i = 0; i < keys.length(); i++) {
            String fieldName = keys.getString(i);
            String fieldValue = (String) document.get(fieldName);

            long fieldTypeId = insertRowIfNotExist(fieldName, "field_type", "field_description");

            PreparedStatement preparedStatement = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, fieldTypeId);
            preparedStatement.setString(2, fieldValue);
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next())  documentFieldId = rs.getLong(1);

            preparedStatement = connection.prepareStatement(query2, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, documentId);
            preparedStatement.setLong(2, documentFieldId);
            preparedStatement.executeUpdate();

        }

        return -1L;
    }
}
