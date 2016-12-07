package coe.dronsys.test;

import coe.dronsys.main.app.User;
import coe.wuti.jpa.DAO;
import coe.wuti.jpa.Insert;

/**
 * Created by jesus on 1/25/16.
 */
public class Test {

    /**
     * Examples about using JPA (via DAO help class)
     */
    public static void main(String[] args) throws Exception {

        // Get DAO help class
        DAO dao = DAO.get("DRONSYS-DB");

        // Insert things...
        dao.execute(new Insert(new User("somebody", "smb@xx.org", "1234", "1111222233334444")));

        // Read list of objects...
        for (User user: dao.getList(User.class)) {
            System.out.println("user = " + user);
        }
    }
}
