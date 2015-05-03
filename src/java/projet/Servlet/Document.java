package projet.Servlet;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.ServletContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.DocumentModel;

import projet.GestionBD;
import projet.Message;

/**
 * Servlet gérant la lecture et l'écriture de Document
 *
 * @author Guillaume
 * @author Francis
 */
public class Document extends HttpServlet {

    /**
     * doGet est utilisée pour la lecture de Document
     *
     * @see DocumentModel#DocumentModel(projet.GestionBD, java.lang.String,
     * java.lang.String)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();

        ServletContext context = session.getServletContext();

        GestionBD gestionBD = (GestionBD) session.getAttribute("gestionBD");

        String pseudo = (String) session.getAttribute("pseudo");
       
        HashMap<String, ArrayList<Message>> mapMessage = (HashMap<String, ArrayList<Message>>) context.getAttribute("mapMessage");

        String id = (String) request.getParameter("id"); //on récupère l'id grâce à l'url

        DocumentModel doc = new DocumentModel(gestionBD, id, pseudo);

        ArrayList<Message> lMessage = null;

        if (mapMessage.containsKey(id)) {
            lMessage = mapMessage.get(id);
        }

        request.setAttribute("lMessage", lMessage);
        request.setAttribute("titre", doc.titre);
        request.setAttribute("fic", doc.fic);
        request.setAttribute("id", id);
        request.setAttribute("lTravailleur", doc.lTravailleur);
        
        request.getRequestDispatcher("/WEB-INF/document.jsp").forward(request, response);
    }

    /**
     * doPost est utilisée pour l'écriture de Document
     *
     * @see DocumentModel#DocumentModel(projet.GestionBD, java.lang.String,
     * java.lang.String)
     * @see DocumentModel#setFic(projet.GestionBD, java.lang.String,
     * java.lang.String, java.lang.String)
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
     * On initialise une HashMap<String, ArrayList<Message>>
     * La String correspond à l'IDDocument et l'ArrayList au chat associé
     * 
     * @throws ServletException 
     * @author Francis
     * @author Guillaume
     */
    @Override
    public void init() throws ServletException {
        HashMap<String, ArrayList<Message>> mapMessage = new HashMap<>();
        
        ServletContext contexte = getServletContext();
        contexte.setAttribute("mapMessage", mapMessage);
    }
}
