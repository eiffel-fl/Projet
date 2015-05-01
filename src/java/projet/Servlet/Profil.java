/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.GestionBD;

/**
 * Servlet utilisée pour afficher profil.jsp
 *
 * @author francis
 */
public class Profil extends HttpServlet {

    /**
     * doGet qui est à peu près le même que MonProfil.doGet()
     *
     * @see MonProfil#doGet(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
     * @see GestionBD#getAmis(java.lang.String)
     * @see GestionBD#getTravailleSur(java.lang.String)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        GestionBD gestionBD = (GestionBD) session.getAttribute("gestionBD");

        String psd = request.getParameter("profil"); //on récupère le pseudo du profil consulté dans l'url

        String amis = gestionBD.getAmis(psd);

        String[] parties = amis.split(",");
        String[] parties2;

        List<String> lAmis = new ArrayList<>();

        for (String party : parties) {
            parties2 = party.split("-");
            lAmis.add(parties2[0]);
        }

        String travailleSur = gestionBD.getTravailleSur(psd);
        parties = travailleSur.split(",");

        List<String[]> lDoc = new ArrayList<>();
        for (String party : parties) {
            parties2 = party.split("-");
            System.out.println(parties2[0]);
            System.out.println(parties2[1]);
            lDoc.add(parties2);
        }

        request.setAttribute("pseudo", psd);
        request.setAttribute("lAmis", lAmis);
        request.setAttribute("lDocument", lDoc);
        request.getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
    }
}
