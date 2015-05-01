package projet.Servlet;

import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import projet.GestionBD;

/**
 * Servlet gérant la préconnexion sur méthode GET ou la connexion simple sur
 * méthode POST
 *
 * @author francis
 */
public class Connexion extends HttpServlet {

    /**
     * Méthode "appelée" par Accueil en cas de la présence de Cookie permettant
     * la "préconnexion"
     *
     * @see GestionBD#Connexion(java.lang.String, java.lang.String)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GestionBD gestionBD = new GestionBD();

        String psd = (String) request.getAttribute("pseudo");
        String pwd = (String) request.getAttribute("password");

        boolean connexion = gestionBD.Connexion(psd, pwd);

        if (connexion) {
            HttpSession session = request.getSession(); //si la connexion est reussie on met dans la session le pseudo du connecté
            session.setAttribute("pseudo", psd);

            session.setAttribute("gestionBD", gestionBD);

            response.sendRedirect("monProfil");
        } else {
            gestionBD.Close();
            response.sendRedirect("accueil");
        }
    }

    /**
     * Méthode "appelée" par la soumission du formulaire de connexion
     * d'accueil.jsp
     *
     * @see GestionBD#Connexion(java.lang.String, java.lang.String)
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //GestionBD gestionBD = GestionBD.getGestionBD();
        GestionBD gestionBD = new GestionBD();

        String psd = request.getParameter("pseudo");
        String pwd = request.getParameter("password");

        boolean connexion = gestionBD.Connexion(psd, pwd);

        System.out.println(psd + " et " + pwd);
        if (connexion) {
            HttpSession session = request.getSession();
            session.setAttribute("pseudo", psd);

            session.setAttribute("gestionBD", gestionBD);

            String conserver = (String) request.getParameter("conserver");

            if (conserver != null) {
                //Si l'utilisateur a choisi qu'on se souvienne de lui on créé 2 cookies
                Cookie cookiePSD = new Cookie("cookiePSD", psd); //l'un avec le pseudo
                Cookie cookiePWD = new Cookie("cookiePWD", pwd); //l'autre avec son mot de passe (en clair...)

                //on met l'âge max de ces cookies à une semaine
                cookiePSD.setMaxAge(60 * 60 * 24 * 7);
                cookiePWD.setMaxAge(60 * 60 * 24 * 7);

                response.addCookie(cookiePSD);
                response.addCookie(cookiePWD);
            }

            response.sendRedirect("monProfil");
        } else {
            System.out.println("connexion echouee");
            gestionBD.Close();
            response.sendRedirect("accueil");
        }
    }
}
