/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet.Servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.GestionBD;
import projet.ProfilModel;

/**
 * Servlet utilisée pour afficher profil.jsp
 *
 * @author francis
 */
public class Profil extends HttpServlet {

    /**
     * doGet qui est à peu près le même que MonProfil.doGet()
     *
     * @see ProfilModel#ProfilModel(projet.GestionBD, java.lang.String) 
     * @see MonProfil#doGet(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse)
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

        ProfilModel profil = new ProfilModel(gestionBD, psd);
        
        request.setAttribute("pseudo", psd);
        request.setAttribute("lAmis", profil.lAmis);
        request.setAttribute("lDocument", profil.lDoc);
        request.getRequestDispatcher("/WEB-INF/profil.jsp").forward(request, response);
    }
}
