package it.javaboss.handler;

import it.javaboss.factory.DocumentPartFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddDocumentImageHandler extends AddDocumentHandler {

    public AddDocumentImageHandler(Connection connection, DocumentPartFactory documentPartFactory, String partType) {
        super(connection, documentPartFactory);
        this.partType = partType;
    }

    public long insertRowsIntoDatabase(JSONObject document) throws JSONException, SQLException {
        JSONArray images = document.getJSONArray("images");
        long documentId = ((Number) document.remove("documentId")).longValue(), documentImageId = -1L;

        for (int i = 0; i < images.length(); i++) {
            JSONObject documentImage = images.getJSONObject(i);
            String sideName = (String) documentImage.get("sideName");
            String imageURL = (String) documentImage.get("imageURL");

            long imageTypeId = insertRowIfNotExist(sideName, "image_type", "image_type_description");

            String query = "INSERT INTO image(image_path, document_model_id, image_type_id) VALUES(?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, imageURL);
            preparedStatement.setLong(2, documentId);
            preparedStatement.setLong(3, imageTypeId);
            preparedStatement.executeUpdate();

            ResultSet rs = preparedStatement.getGeneratedKeys();
            if (rs.next())  documentImageId = rs.getLong(1);

            JSONArray documentSecurityFeatures = (JSONArray) documentImage.get("listOfDocumentSecurity");

            if (documentSecurityFeatures.length() > 0)
                addSecurityFeatures(documentSecurityFeatures, documentId, documentImageId);
        }

        return documentId;
    }

    private void addSecurityFeatures(JSONArray documentSecurityFeatures, long documentId, long documentImageId) throws JSONException, SQLException {
        for (int i = 0; i < documentSecurityFeatures.length(); i++) {
            JSONObject securityFeature = documentSecurityFeatures.getJSONObject(i);
            String securityFeatureName = (String) securityFeature.get("securityFeatureName");
            String securityFeatureImageURL = (String) securityFeature.get("securityFeatureImage");

            long securityTypeId = insertRowIfNotExist(securityFeatureName, "security_type", "security_type_description");
            String query = "INSERT INTO security_feature(image_path, document_model_id, related_image_id, security_type_id) VALUES(?,?,?,?)";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, securityFeatureImageURL);
            preparedStatement.setLong(2, documentId);
            preparedStatement.setLong(3, documentImageId);
            preparedStatement.setLong(4, securityTypeId);

            preparedStatement.executeUpdate();

        }
    }
}
