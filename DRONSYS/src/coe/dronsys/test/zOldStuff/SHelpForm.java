package coe.dronsys.test.zOldStuff;

import coe.dronsys.main.pages.contact.Contact;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * User: jrubio
 */
public class SHelpForm extends HttpServlet {
    private Contact contact = new Contact();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Back to helpdesk page
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/classes/coe/dronsis/footer/help.jsp");
        dispatcher.forward(req, resp);
    }
}
