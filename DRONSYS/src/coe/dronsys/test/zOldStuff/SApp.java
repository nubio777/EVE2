package coe.dronsys.test.zOldStuff;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by jesus on 1/18/16.
 */
public class SApp extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer requestURL = req.getRequestURL();

        if (requestURL.toString().endsWith("/help")) {
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/classes/coe/dronsis/footer/help.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
