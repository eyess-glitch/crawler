package it.webcrawler.handler;

import it.webcrawler.factory.DocumentPartFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.*;

public abstract class AddDocumentHandler implements Handler {

    protected AddDocumentHandler next;
    protected DocumentPartFactory documentPartFactory;
    protected Connection connection;

    protected String partType;

    public AddDocumentHandler(Connection connection, DocumentPartFactory documentPartFactory) {
        this.connection = connection;
        this.documentPartFactory = documentPartFactory;
    }

    public void handleRequest(JSONObject document, long id) throws JSONException, SQLException {
        JSONObject partOfADocument = (JSONObject) documentPartFactory.createPart(partType, document);
        partOfADocument.put("documentId", id);
        long documentId = insertRowsIntoDatabase(partOfADocument);
        if (document.length() > 0 && next != null) next.handleRequest(document, documentId);
        if (next == null) connection.close();
    }

    public AddDocumentHandler linkWith(AddDocumentHandler next) {
        this.next = next;
        return next;
    }

    public long insertRowIfNotExist(String documentField, String tableName, String columnName) throws JSONException, SQLException {
        String query = prepareQuery(tableName, columnName);
        PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, documentField);
        preparedStatement.setString(2, documentField);
        ResultSet queryResult = preparedStatement.executeQuery();
        long rowId = -1L;
        if (queryResult.next()) rowId = queryResult.getLong(1);
        return rowId;
    }

    public String prepareQuery(String tableName, String columnName) {
        String query = "WITH result AS(\n" +
                "           INSERT INTO table_name(column_name)\n" +
                "           VALUES (?)\n" +
                "           ON CONFLICT(column_name) DO NOTHING\n" +
                "           RETURNING id\n" +
                "          )\n" +
                "SELECT * FROM result\n" +
                "UNION ALL\n" +
                "SELECT id FROM table_name WHERE column_name = ?";
        query = query.replaceAll("column_name", columnName).replaceAll("table_name", tableName);
        return query;
    }

    public abstract long insertRowsIntoDatabase(JSONObject document) throws JSONException, SQLException;

}
