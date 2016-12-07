package coe.dronsys.test.zOldStuff.webservices;

import javax.ejb.Stateless;
import javax.jws.WebService;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by root on 2/5/16.
 */

@Stateless
@WebService(portName = "CreditCardWebserviceInterfacePort", serviceName = "CreditCardWebserviceInterfaceService", endpointInterface = "coe.dronsis.webservices.HelpWebServiceInterface")
public class HelpWebService implements HelpWebServiceInterface {

    private static final String HELP_FILE = "/ShopSite_Frequently_Asked_Questions.html";


    public String getHelpText(String input) {

        try {
            URL url = HelpWebService.class.getResource(HELP_FILE);
            String[] cmd = {"sh", "-c", "grep --color -i -r " + url.getPath() + " -e " + sanitizeHelpText(input)};
            Process proc = Runtime.getRuntime().exec(cmd);

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            StringBuilder results = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null)
                results.append(line).append("\n");

            return results.toString();


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }


    private String sanitizeHelpText(String s) {
        return (s == null || s.trim().length() == 0) ?
                s : s.replace("'", "").
                replace("\\", "").
                replace("{", "").
                replace("&", "").
                replace("&&", "").
                replace("../", "").
                replace(";", "").
                replace("}", "");
    }
}
