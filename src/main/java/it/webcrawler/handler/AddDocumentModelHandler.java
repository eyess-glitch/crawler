package it.webcrawler.handler;

import it.webcrawler.factory.DocumentPartFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public class AddDocumentModelHandler extends AddDocumentHandler {

    public AddDocumentModelHandler(Connection connection, DocumentPartFactory documentPartFactory, String partType) {
        super(connection, documentPartFactory);
        this.partType = partType;
    }

    public long insertRowsIntoDatabase(JSONObject document) throws JSONException, SQLException {
        String issuingCountry = (String) document.get("Issuing Country");
        String documentCategory = ((String) document.get("Document Category")).split("-")[1].trim();

        long documentTypeId = insertRowIfNotExist(documentCategory, "document_type", "document_model_description");
        long issuingCountryId = insertRowIfNotExist(issuingCountry, "nationality", "country_name");

        String query = "INSERT INTO document_model(document_name, document_type_id, nationality_id) VALUES(?,?,?)";
        String documentName = (String) document.get("Document");

        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, documentName);
        preparedStatement.setLong(2, documentTypeId);
        preparedStatement.setLong(3, issuingCountryId);
        int affectedRows = preparedStatement.executeUpdate();

        long documentModelId = -1L;

        ResultSet rs = preparedStatement.getGeneratedKeys();
        if (rs.next())  documentModelId = rs.getLong(1);
        return documentModelId;
    }
}
