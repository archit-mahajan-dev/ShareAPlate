package in.shareaplate.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public final class Database {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = Database.class.getResourceAsStream("/db.properties")) {
            if (input == null) {
                throw new IllegalStateException(
                    "Missing db.properties. Copy src/main/resources/db.properties.example to db.properties and configure it."
                );
            }
            PROPERTIES.load(input);
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database() {}

    public static Connection getConnection() throws SQLException {
        String url = PROPERTIES.getProperty("db.url");
        String user = PROPERTIES.getProperty("db.username");
        String password = PROPERTIES.getProperty("db.password");
        return DriverManager.getConnection(url, user, password);
    }

    public static void testConnection() throws SQLException {
        try (Connection ignored = getConnection()) {
            // Successful connection is enough for startup validation.
        }
    }
}
