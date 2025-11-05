package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class Database {

    private static Connection conn = null;

    public static Connection getConnection() {
        if (conn != null) return conn;

        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config/db.properties"));

            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            conn = DriverManager.getConnection(url, user, password);
            System.out.println("✅ Ligação à BD estabelecida!");
        } catch (Exception e) {
            System.out.println("❌ Erro na ligação à BD: " + e.getMessage());
        }

        return conn;
    }
}
