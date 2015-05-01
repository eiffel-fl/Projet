/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.GestionBD;

/**
 * Servlet de Déconnexion
 *
 * @author francis
 */
public class Deconnexion extends HttpServlet {

    /**
     * Lors d'un GET cette méthode sera appelée pour fermer les connexions avec
     * la base de données et la connexion à mira
     *
     * @see GestionBD#Close()
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        HttpSession session = request.getSession();

        //si les cookies existent alors on les supprime
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("cookiePWD".equals(cookie.getName()) || "cookiePSD".equals(cookie.getName())) {
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }

        GestionBD gestionBD = (GestionBD) session.getAttribute("gestionBD");

        //GestionBD.nbConnexion --;
        //if(GestionBD.nbConnexion == 0){
        gestionBD.Close();

        session.invalidate();
        //}
        response.sendRedirect("accueil");
    }
}
