package projet.Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.GestionBD;

/**
 * Classe qui permet à l'utilisateur de faire une demande d'amis
 * 
 * @author francis
 */
public class DemandeAmis extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        out.println("<html><body><img src='https://pbs.twimg.com/profile_images/378800000822867536/3f5a00acf72df93528b6bb7cd0a4fd0c.jpeg'></img><h1>Doge est notre ami</h1></body></html>");
    }

    /**
     * doPost utilisé pour creer une demande d'ami
     *
     * @see GestionBD#sontAmis(java.lang.String, java.lang.String) 
     * @see GestionBD#setDemandeAmis(java.lang.String, java.lang.String) 
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @author Francis
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        GestionBD gestionBD = (GestionBD) session.getAttribute("gestionBD");
        
        String pseudo = (String) session.getAttribute("pseudo");
        String pseudoAmi = request.getParameter("amis");
        
        if(!gestionBD.sontAmis(pseudo, pseudoAmi)){
            //Si les 2 pseudonymes ne sont pas déjà amis on ajoute le couple (pseudo, pseudoAmi) à la table DemandeAmis
            gestionBD.setDemandeAmis(pseudo, pseudoAmi);
        }
        
        response.sendRedirect("monProfil");
    }
}
