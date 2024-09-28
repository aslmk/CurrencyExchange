package aslmk;

import aslmk.Utils.Exceptions.DatabaseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Database {
    private static List<Connection> connectionPool;
    private static List<Connection> usedConnections = new ArrayList<>();
    private static final byte INITIAL_POOL_SIZE = 10;

    public static void isConnectionPoolCreated() throws SQLException, DatabaseException  {
        if (connectionPool == null) createConnectionPool();
    }

    private static void createConnectionPool() throws SQLException, DatabaseException {
        try {
            String dbPath;

            URL resource = Database.class.getClassLoader().getResource("CurrencyExchange.db");
            if (resource == null) {
                throw new FileNotFoundException("Database file does not found in resources folder.");
            }

            dbPath = new File(resource.toURI()).getAbsolutePath();
            List<Connection> pool = new ArrayList<>(INITIAL_POOL_SIZE);

            for (int i = 0; i < INITIAL_POOL_SIZE; i++) {
                pool.add(createConnection(dbPath));
            }
            connectionPool = pool;
        } catch (FileNotFoundException e) {
            throw new DatabaseException(e.getMessage());
        } catch (URISyntaxException e) {
            throw new DatabaseException("this URL is not formatted strictly according to RFC2396 and cannot be converted to a URI.");
        }

    }
    private static Connection createConnection(String dbPath) throws SQLException {
        Connection connection;

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new DatabaseException("Something wrong with database.");
        }

        connection = DriverManager.getConnection(String.format("jdbc:sqlite:%s", dbPath));

        return connection;
    }
    public static Connection getConnection() {
        Connection connection;

        if (usedConnections.size() < INITIAL_POOL_SIZE) {
            connection = connectionPool.remove(connectionPool.size()-1);
            usedConnections.add(connection);
        } else {
            throw new DatabaseException("Database is not available.");
        }
        return connection;
    }
    public static void releaseConnection(Connection connection) {
        usedConnections.remove(connection);
        connectionPool.add(connection);
    }
}
