package projet.Servlet; //package spécifique au servlet

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet gérant la JSP accueil.jsp
 *
 * @author francis
 */
public class Accueil extends HttpServlet {

    /**
     * Cette méthode testera l'existence de cookie, si oui elle redirigera sur
     * la servlet Connexion pour permettre une préconnexion. Si non elle
     * redirigera sur la JSP accueil.jsp
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        String psd = null;
        String pwd = null;

        //On test l'existence des cookies
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cookiePSD".equals(cookie.getName())) {
                    psd = cookie.getValue();
                }
                if ("cookiePWD".equals(cookie.getName())) {
                    pwd = cookie.getValue();
                }
            }
        }

        //Si ils ne sont pas null on redirige sur la servlet Connexion (donc on fait un GET sur cette servlet)
        if (psd != null && pwd != null) {
            request.setAttribute("pseudo", psd);
            request.setAttribute("password", pwd);
            System.out.println("cookie : " + psd + " et " + pwd);

            request.getRequestDispatcher("Connexion").forward(request, response);
        } else {
            //Sinon on redigire sur accueil.jsp
            request.getRequestDispatcher("WEB-INF/accueil.jsp").forward(request, response);
        }
    }
}
