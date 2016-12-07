package coe.dronsys.test.zOldStuff.webservices;

import javax.ejb.Stateless;
import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/19/16.
 */
@Stateless
@WebService( portName = "CreditCardWebserviceInterfacePort",
        serviceName = "CreditCardWebserviceInterfaceService",
        //targetNamespace = "http://localhost:8080/dronsis/wsdl",
        endpointInterface= "coe.dronsis.model.webservices.CreditCardWebserviceInterface")
public class CreditCardWebService implements CreditCardWebserviceInterface {

    private Connection connection;
  //  private EntityManager entityManh ap
    public static String persistenceUnitName = "DRONSIS-DB";
    public CreditCardWebService() {

  //      entityManager = Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();

    }


    private boolean openConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost/pru?" + "user=admin&password=torro!234");
            return true;
        } catch (Exception e) {
            closeConnection();
            return false;
        }
    }


    private boolean closeConnection() {
        try {
            if (connection != null)
                connection.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }


/*    public List<CreditCardEntity> getCreditCard(String id) {
        ResultSet results = getResults(id);
        List
        if ((results != null)) {
            try {
                results.last();
                String[] users = new String[results.getRow()];
                if (users.length > 4) {
                    Boolean completed = true;
                }
                results.beforeFirst();
                while (results.next() == true) {
                    int i = results.getRow();
                    users[i - 1] = results.getString(ccNumber);
                }
                return users;
            } catch (SQLException sqle) {
            }
        }
        return null;
    }*/

    public  List<CreditCardEntity> getCreditCards(String id) {

        List<CreditCardEntity> resultsList  = new ArrayList<CreditCardEntity>();
        try {
/*
            String query = "SELECT * FROM Signup,CreditCardEntity WHERE Signup.id = " + id;
            try {
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);
                int coulumns = results.getMetaData().getColumnCount();
                System.out.println("coulumns :"+coulumns );
                while (results.next()){

                    CreditCardEntity creditCardEntity = new CreditCardEntity();
                    creditCardEntity.setCreditCardNumber(results.getBigDecimal("CreditCardnumber"));
                    creditCardEntity.setCvs(results.getInt("cvs"));
                    creditCardEntity.setCreditCardType(results.getString("CreditCardType"));
                    resultsList.add(creditCardEntity);

                }

                return resultsList;
            } catch (SQLException sqle) {

            }*/


       /*     try {


                Runtime rt = Runtime.getRuntime();
                String[] cmd = {"/bin/sh", "-c", "grep 'Report Process started' server.log|wc -l"};
                Process proc = rt.exec(cmd);
                printStream(proc.getInputStream());
                System.out.println("Error : ");
                printStream(proc.getErrorStream());


            } catch (Exception ex) {
                ex.printStackTrace();
            }*/

///etc/passwd

            CreditCardEntity creditCardEntity = new CreditCardEntity();
            StringBuffer output = new StringBuffer();
            String line= null ;


            //String[] cmd = {"/bin/sh","-c"," grep -v bash "+id+"| grep -v nologin "};
            //ps -ef
            String[] cmd = {"/bin/sh","-c","ps -ef"};
            Process proc = Runtime.getRuntime().exec(cmd);


            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(proc.getInputStream()));


            while ((line = reader.readLine())!= null) {
                output.append(line + "\n");
            }
            creditCardEntity.setCreditCardType(output.toString().substring(0,24));
            creditCardEntity.setCreditCardNumber(new BigDecimal(100000));
            creditCardEntity.setCvs(100);
            resultsList.add(creditCardEntity);

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return null;
        }

        return resultsList;
    }





/*

    public ResultSet getResultsWithEntityManager(String id) {
        try {

            EntityTransaction tx = entityManager.getTransaction();
            entityManager.find(CreditCardEntity.class, new CreditCardEntinty(id));


            String query = "SELECT * FROM user_data WHERE userid = " + id;
            try {
                tx.begin();
                Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                ResultSet results = statement.executeQuery(query);
                return results;
            } catch (SQLException sqle) {
            }
        } catch (Exception e) {
        }
        return null;
    }
*/



    public Boolean createCreditCard(String name,String id){


        return true;
    };

}
