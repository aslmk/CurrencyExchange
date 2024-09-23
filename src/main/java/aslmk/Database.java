package aslmk;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URL;

public class Database {
    private Connection connection = null;
    private static Database instance = null;
    private Database() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            URL resource = Database.class.getClassLoader().getResource("CurrencyExchange.db");
            if (resource == null) {
                throw new FileNotFoundException("Database file does not found in resources folder");
            }
            String path = new File(resource.toURI()).getAbsolutePath();
            connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", path));
        } catch (SQLException | FileNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
