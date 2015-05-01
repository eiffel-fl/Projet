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
 * Servlet utilisée lors de la soumission du formulaire de recherche.jsp mais
 * aussi pour afficher recherche.jsp
 *
 * @author francis
 */
public class Recherche extends HttpServlet {
    /**
     * doPost qui va appeller GestionBD.getRecherche puis renvoyer des
     * informations nécéssaires à l'affichage
     *
     * @see GestionBD#getRecherche(java.lang.String, java.lang.String)
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

        String type = (String) request.getParameter("type");
        String toSearch = (String) request.getParameter("toSearch");

        String recherches = gestionBD.getRecherche(type, toSearch);

        String[] parties = recherches.split(",");
        String[] parties2;

        List<String[]> lRecherche = new ArrayList<>();

        //on exécute des traitements sur la chaîne retournée par la requête
        for (String party : parties) {
            //chaque ligne est un tableau de chaîne, on a donc une liste de String[]
            parties2 = party.split("-");
            lRecherche.add(parties2);
        }

        request.setAttribute("lRecherche", lRecherche);

        request.getRequestDispatcher("/WEB-INF/recherche.jsp").forward(request, response);
    }
}
