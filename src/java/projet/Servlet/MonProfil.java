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
 * Servlet permettant l'affichage de monProfil.jsp
 *
 * @author francis
 */
public class MonProfil extends HttpServlet {

    /**
     * doGet exécutant diverses requêtes comme getAmis(), getTravailleSur() et
     * getDemandeAmis()
     *
     * @see GestionBD#getAmis(java.lang.String)
     * @see GestionBD#getTravailleSur(java.lang.String)
     * @see GestionBD#getDemandeAmis(java.lang.String)
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

        String psd = (String) session.getAttribute("pseudo");

        String amis = gestionBD.getAmis(psd);

        String[] parties = amis.split(",");
        String[] parties2;

        List<String> lAmis = new ArrayList<>();

        //on exécute des traitements sur la chaîne retournée et on ajoute les amis à une liste de String
        for (String party : parties) {
            parties2 = party.split("-");
            lAmis.add(parties2[0]);
        }

        String travailleSur = gestionBD.getTravailleSur(psd);
        parties = travailleSur.split(",");

        //idem que pour la liste d'amis mais on aura ici une liste de String[] car on envoie le titre du document et son id
        List<String[]> lDoc = new ArrayList<>();
        for (String party : parties) {
            parties2 = party.split("-");
            lDoc.add(parties2);
        }

        String demandeAmis = gestionBD.getDemandeAmis(psd);

        parties = demandeAmis.split(",");

        List<String> lDemandeAmis = new ArrayList<>();

        //même traitement que pour la liste d'amis
        for (String party : parties) {
            parties2 = party.split("-");
            lDemandeAmis.add(parties2[0]);
        }

        request.setAttribute("lAmis", lAmis);
        request.setAttribute("lDocument", lDoc);
        request.setAttribute("lDemandeAmis", lDemandeAmis);
        request.getRequestDispatcher("/WEB-INF/monProfil.jsp").forward(request, response);
    }
}
