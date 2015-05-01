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
     * @see GestionBD#getDocument(java.lang.String)
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

        String result = gestionBD.getDocument(id); //on récupère des informations sur le Document d'IDDocument id

        String[] parties = result.split(",");

        parties = parties[0].split("-");

        String titre = parties[0];
        String chemin = parties[1];
        String lecture = parties[2];
        String auteur = parties[4];

        String fic = "";

        boolean amis = gestionBD.sontAmis(pseudo, auteur);

        if (lecture.equals("Public") || (lecture.equals("Amis") && amis) || pseudo.equals(auteur)) {
            //si le Document est "public" alors pas de soucis
            //si la lecture du Document est réservé aux amis on regarde si l'auteur et l'utilisateur sont amis
            //si l'utilisateur est l'auteur du Document alors pas de soucis
            try {
                //on créé cet objet qui permettra de creer des fichiers ou des dossiers mais aussi de les lire
                ChannelSftp sftp = (ChannelSftp) GestionBD.session.openChannel("sftp");
                sftp.connect(); //on se connecte

                InputStream streamFic = sftp.get(chemin); //on récupère le fichier id à l'adresse ~/session/Projet/auteur
                BufferedReader bufferFic = new BufferedReader(new InputStreamReader(streamFic)); //on créé un BufferReader sur l'InputStream

                String line;

                //on récupère le contenu du fichier
                while ((line = bufferFic.readLine()) != null) {
                    fic += line + "\n";
                }

                //on ferme ce dont a plus besoin
                streamFic.close();
                sftp.disconnect();

            } catch (JSchException | SftpException ex) {
                Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //on traite les cas ou l'utilisateur ne peut pas lire le Document
            if (lecture.equals("Amis")) {
                fic = "Ce document n'est visible que par les amis de " + auteur + " et vous n'êtes pas encore amis,"
                        + " n'hésitez pas à lui envoyer une demande d'amis !";
            }
            if (lecture.equals("Utilisateur")) {
                fic = "Ce document n'est visible que par " + auteur;
            }
        }

        request.setAttribute("titre", titre);
        request.setAttribute("fic", fic);
        request.setAttribute("id", id);

        request.getRequestDispatcher("/WEB-INF/document.jsp").forward(request, response);
    }

    /**
     * doPost est utilisée pour l'écriture de Document
     *
     * @see GestionBD#getDocument(java.lang.String)
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

        String result = gestionBD.getDocument(id); //grâce à l'id on récupère des informations sur le Document

        String[] parties = result.split(",");

        parties = parties[0].split("-");

        String chemin = parties[1];
        String ecriture = parties[3];
        String auteur = parties[4];

        boolean amis = gestionBD.sontAmis(pseudo, auteur);

        String fic = (String) request.getParameter("textarea");
        if (ecriture.equals("Public") || (ecriture.equals("Amis") && amis) || pseudo.equals(auteur)) {
            if(!gestionBD.getExisteTravaille(pseudo, id))
                gestionBD.setTravailleSur(pseudo, id);
            try {
                ChannelSftp sftp = (ChannelSftp) GestionBD.session.openChannel("sftp");
                sftp.connect();

                OutputStream streamFic = sftp.put(chemin, ChannelSftp.OVERWRITE); //OVERWRITE permettra d'écraser le contenu du fichier
                PrintStream streamPrint = new PrintStream(streamFic); //on créé un PrintStream sur l'OutputStream pour faciliter l'écriture

                streamPrint.print(fic); //on écrit

                //on ferme ce dont on a plus besoin
                streamPrint.close();
                streamFic.close();
                sftp.disconnect();
            } catch (JSchException | SftpException ex) {
                Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //on traite les cas où l'utilisateur ne peut pas écrire le Document
            PrintWriter out = response.getWriter();
            String erreur = "";
            if (ecriture.equals("Amis")) {
                erreur = "amis";
            }
            if (ecriture.equals("Utilisateur")) {
                erreur = "utilisateur";
            }
            response.sendRedirect("document?id=" + id + "&erreur=" + erreur + "&auteur=" + auteur);
            return;
        }
        response.sendRedirect("document?id=" + id);
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
