package projet.Servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.GestionBD;

/**
 * Servlet appelée lors de la soumission des formulaires de Demande d'Amis
 *
 * @author francis
 */
public class GererDemandeAmis extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println("<html><body><img src='http://img3.wikia.nocookie.net/__cb20120611165719/fairy-tail/fr/images/a/a5/Embl%C3%AAme_Fairy_Tail.jpg'></img><h1>Il faut croire au pouvoir de l'amitié</h1></body></html>");
    }

    
    /**
     * doPost utilisé pour gerer les demandes d'amis
     *
     * @see GestionBD#dropDemande(java.lang.String, java.lang.String)
     * @see GestionBD#setAmis(java.lang.String, java.lang.String)
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

        String destinataire = (String) session.getAttribute("pseudo");
        String emetteur = request.getParameter("amis");
        String choix = request.getParameter("choix");

        if (choix.equals("accepter")) {
            //Si l'utilisateur a accepté la demande on créé un nouveau lien d'amitié
            gestionBD.setAmis(emetteur, destinataire);
        }
        gestionBD.dropDemande(emetteur, destinataire); //quelque soit la réponse on enlève la demande la table car elle a été répondue
        response.sendRedirect("monProfil");
    }
}
