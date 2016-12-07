package coe.ahr;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * User: jrubio
 */
@WebServlet(urlPatterns = {"/recover"})
public class SRecover extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");

        if (email == null || email.trim().length() == 0)
            resp.getWriter().println("Email address required");

        else
            resp.getWriter().println(handleEmail(email));
    }

    private String handleEmail(String email) {

        try {
            return DataBase.existsMail(email) ? "A message has been sent to the supplied email" : "Email not found";
        } catch (Exception e) {
            return e.getMessage();
        }

    }


}


