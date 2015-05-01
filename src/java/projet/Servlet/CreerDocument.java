package projet.Servlet;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import projet.GestionBD;

/**
 * Servlet appelée lors de la soumission du formulaire liée dans monProfil.jsp
 *
 * @author francis
 */
public class CreerDocument extends HttpServlet {

    /**
     * Quand le formulaire sera soumis on créera le document
     *
     * @see GestionBD#setNouveauDocument(java.lang.String, java.lang.String,
     * java.lang.String, java.lang.String)
     * @see GestionBD#setTravailleSur(java.lang.String, java.lang.String)
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

        String auteur = (String) session.getAttribute("pseudo");
        String titre = request.getParameter("titre");
        String lecture = request.getParameter("lecture");
        String ecriture = request.getParameter("ecriture");

        if (gestionBD.setNouveauDocument(titre, lecture, ecriture, auteur)) {
            //Si l'ajout du Document à la base s'est bien passé on creer le document sur mira
            String id = gestionBD.getIDDocument(titre, auteur);

            try {
                //on créé cet objet qui permettra de creer des fichiers ou des dossiers mais aussi de les lire
                ChannelSftp sftp = (ChannelSftp) GestionBD.session.openChannel("sftp");
                sftp.connect(); //on se connecte

                //le chemin de base sera ~/session 
                OutputStream out = sftp.put("Projet/" + auteur + "/" + id);
                //avec cet implémentation de put on créé le fichier id à l'emplacement ~/session/Projet/nom_auteur/
                out.close(); //on ferme l'OutputStream obtenu car on n'en a pas besoin

                sftp.disconnect(); // on se déconnecte
            } catch (JSchException | SftpException ex) {
                Logger.getLogger(CreerDocument.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        response.sendRedirect("monProfil");
    }
}
