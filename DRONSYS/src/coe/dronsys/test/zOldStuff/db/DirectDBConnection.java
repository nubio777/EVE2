package coe.dronsys.test.zOldStuff.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by jesus on 1/26/16.
 */
public class DirectDBConnection {
    private static DirectDBConnection directDBConnection;

    private Connection conexion;

    private DirectDBConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/dronsis?" + "user=root&password=ccdcoe");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static DirectDBConnection get() {
        if (directDBConnection == null) directDBConnection = new DirectDBConnection();
        return directDBConnection;
    }

    public boolean executeQuery(String query) throws Exception {
        Statement stmt = conexion.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }
}
