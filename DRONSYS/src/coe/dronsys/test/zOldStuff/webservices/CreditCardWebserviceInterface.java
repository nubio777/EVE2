package coe.dronsys.test.zOldStuff.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

/**
 * Created by root on 1/20/16.
 */


@WebService(
       // targetNamespace = "http://localhost:8080/wsdl"
)
public interface CreditCardWebserviceInterface {

   @WebMethod(operationName="getCreditCards")
   public List<CreditCardEntity> getCreditCards(String id);


    @WebMethod(operationName="createCreditCards")
    public Boolean createCreditCard(String name, String id);

}
