package it.webcrawler.command;

import it.webcrawler.factory.ConcreteFactory;
import it.webcrawler.handler.AddDocumentFieldHandler;
import it.webcrawler.handler.AddDocumentImageHandler;
import it.webcrawler.handler.AddDocumentModelHandler;
import it.webcrawler.handler.AddDocumentValidityHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class addDocumentOperation implements Operation {

    private final JSONObject identityDocument;

    public addDocumentOperation(JSONObject identityDocument) {
        this.identityDocument = identityDocument;
    }

    public void performTask()  {
        ConcreteFactory factory = new ConcreteFactory();
        Connection connection = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream("src/main/resources/config.properties"));

            connection = DriverManager.getConnection(properties.getProperty("URL"), properties.getProperty("USER"), properties.getProperty("PASSWORD"));

            AddDocumentModelHandler addDocumentModelHandler = new AddDocumentModelHandler(connection, factory, "DOCUMENT_MODEL");
            AddDocumentValidityHandler addDocumentValidityHandler = new AddDocumentValidityHandler(connection, factory, "DOCUMENT_VALIDITY");
            AddDocumentImageHandler addDocumentImageHandler = new AddDocumentImageHandler(connection, factory, "DOCUMENT_IMAGE");
            AddDocumentFieldHandler addDocumentFieldHandler = new AddDocumentFieldHandler(connection, factory, "DOCUMENT_FIELD");

            addDocumentModelHandler.linkWith(addDocumentValidityHandler).linkWith(addDocumentImageHandler).linkWith(addDocumentFieldHandler);

            addDocumentModelHandler.handleRequest(identityDocument, 0); // l'id nell'invio della prima richiesta e' uno stub
        } catch (JSONException | SQLException | IOException e) {
            e.printStackTrace();
        }
    }

}
