package coe.ahr;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by jesus on 6/21/16.
 */
public class DataBase {
    private static final String mysqlURL = "jdbc:mysql://localhost/ahr?" + "user=root&password=ccdcoe" ;

    static boolean existsMail(String mail) throws Exception {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(mysqlURL);
            Statement stmt = connection.createStatement();

            String sql = "SELECT * from users where email='" + mail + "'; ";
            System.out.println("sql = " + sql);

            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();

        } catch (Exception e) {
            throw new Exception("Connection error.");
        }
    }

    static void populate () throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection(mysqlURL);

        connection.createStatement().execute("create table users (id int(11) not null auto_increment, name varchar(255), pwd varchar(255), primary key (id)) ") ;


        for (User user : User.values()) {
            connection.createStatement().execute("insert into users (name, pwd) values ('" + user.getName() + "','" + user.getPwd() + "');");
        }
    }

    public static void main(String[] args) throws Exception{
        populate() ;
    }

}
