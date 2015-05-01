package projet.Servlet;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.DocumentModel;

import projet.GestionBD;

/**
 * Servlet gérant la lecture et l'écriture de Document
 *
 * @author francis
 */
public class Document extends HttpServlet {

    /**
     * doGet est utilisée pour la lecture de Document
     *
     * @see DocumentModel#DocumentModel(projet.GestionBD, java.lang.String, java.lang.String) 
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

        String pseudo = (String) session.getAttribute("pseudo");

        String id = (String) request.getParameter("id"); //on récupère l'id grâce à l'url

        DocumentModel doc = new DocumentModel(gestionBD, id, pseudo);
        
        request.setAttribute("titre", doc.titre);
        request.setAttribute("fic", doc.fic);
        request.setAttribute("id", id);

        request.getRequestDispatcher("/WEB-INF/document.jsp").forward(request, response);
    }

    /**
     * doPost est utilisée pour l'écriture de Document
     *
     * @see DocumentModel#DocumentModel(projet.GestionBD, java.lang.String, java.lang.String) 7
     * @see DocumentModel#setFic(projet.GestionBD, java.lang.String, java.lang.String, java.lang.String) 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        GestionBD gestionBD = (GestionBD) session.getAttribute("gestionBD");

        String pseudo = (String) session.getAttribute("pseudo");

        String id = (String) request.getParameter("id"); //on récupère l'id grâce au champ hidden
        
        String fic = (String) request.getParameter("textarea");
        
        DocumentModel doc = new DocumentModel(gestionBD, id, pseudo);
        
        String redirect = doc.setFic(gestionBD, id, pseudo, fic);
        
        response.sendRedirect(redirect);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
