package coe.ahr;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: jrubio
 */
@WebServlet(urlPatterns = {"/login"})
public class SLogin extends HttpServlet {

//    private final String MSG_BAD_LOGIN = "Login failed (accounts will be temporally disabled after " + GestorIntentosLogin.NUM_MAX_INTENTOS + " consecutive failed attempts)";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("user");
        String pwd = req.getParameter("pwd");

        if (userName == null || userName.trim().length() == 0) {
            resp.getWriter().println("Username is required");
            return;
        }
        if (pwd == null || pwd.trim().length() == 0) {
            resp.getWriter().println("Password is required");
            return;
        }

        if (User.isValid(userName, pwd)) {
            req.getSession().setAttribute("userName", userName);
            resp.getWriter().println("");
        }
        else
            resp.getWriter().println("Username and/or password incorrect");
    }
}

//@SuppressWarnings("ConstantConditions")
//public class GestorCookieVulnerable {
//    private static final String NOMBRE = "ALT";
//
//    private GestorCookieVulnerable() {
//    }
//
//    public static User quienEs(WebRequest webRequest) {
//        Cookie cookie = webRequest.getCookie(NOMBRE);
//        if (cookie == null) return null;
//
//        String valor = cookie.getValue();
//        if (valor == null) return null;
//
//        for (User user : User.values())
//            if (valor.equals(user.getPwdEnMD5()))
//                return user;
//
//        return null;
//    }
//
//    public static void setCookie (WebResponse response, User user) {
//        response.addCookie(new Cookie(NOMBRE, user.getPwdEnMD5()));
//    }
//}

//public class GestorIntentosLogin {
//    private static GestorIntentosLogin gestor = new GestorIntentosLogin();
//
//    public static final int NUM_MAX_INTENTOS = 5;
//    public static final double TIEMPO_ESPERA_EN_MINUTOS = 5;
//
//    private Map<String, TreeSet<Date>> intentos = new TreeMap<>();
//
//    private GestorIntentosLogin() {
//    }
//
//    public static GestorIntentosLogin get() {
//        return gestor;
//    }
//
//    public boolean esLegitimo(String user) {
//        if (user == null || user.trim().length() == 0) return false;
//
//        user = user.trim();
//        Date ahora = new Date();
//
//        if (intentos.containsKey(user)) {
//
//            TreeSet<Date> fechas = intentos.get(user);
//
//            if (fechas.size() == NUM_MAX_INTENTOS) {
//                long tiempoEsperaEnMiliSecs = (long) (TIEMPO_ESPERA_EN_MINUTOS * 60 * 1000);
//
//                if (ahora.getTime() - fechas.last().getTime() > tiempoEsperaEnMiliSecs) {
//                    fechas.clear();
//                    fechas.add(ahora);
//                    return true;
//                } else {
//                    return false;
//                }
//
//            } else {
//
//                fechas.add(new Date());
//                return true;
//
//            }
//        } else {
//
//            TreeSet<Date> fechas = new TreeSet<>();
//            fechas.add(new Date());
//            intentos.put(user, fechas);
//            return true;
//        }
//    }
//
//}


