package coe.dronsys.test.zOldStuff.webservices;

import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * Created by root on 2/5/16.
 */

@WebService()
public interface HelpWebServiceInterface {
    @WebMethod(operationName = "getHelpText")
    public String getHelpText(String input);
}
