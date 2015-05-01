package projet.Servlet;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import projet.GestionBD;

/**
 * Servlet permettant l'inscription d'un nouveau membre
 *
 * @author francis
 */
public class Inscription extends HttpServlet {

    /**
     * doPost permettant l'inscription d'un utilisateur après quelques
     * vérifications
     *
     * @see GestionBD#setNouvelUtilisateur(java.lang.String, java.lang.String,
     * java.lang.String)
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //GestionBD gestionBD = GestionBD.getGestionBD();
        GestionBD gestionBD = new GestionBD();

        String psd = request.getParameter("pseudo");
        String pwd = request.getParameter("password");
        String pwd2 = request.getParameter("password2");
        String mail = request.getParameter("mail");

        if (pwd.equals(pwd2) && psd.matches("(\\w|\\d){3,50}") && pwd.matches("(\\w|\\d){3,50}")) {
            //on vérifie la concordance des 2 mots de passes et si le pseudo et le mot de passe matchent la regex
            boolean inscription = gestionBD.setNouvelUtilisateur(psd, pwd, mail);

            if (inscription) {
                try {
                    ChannelSftp sftp = (ChannelSftp) GestionBD.getSession().openChannel("sftp");
                    sftp.connect();

                    System.out.println(sftp.pwd());

                    sftp.mkdir("Projet/" + psd);

                    sftp.disconnect();
                } catch (JSchException | SftpException ex) {
                    Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
                }

                HttpSession session = request.getSession();
                session.setAttribute("pseudo", psd);

                session.setAttribute("gestionBD", gestionBD);

                String conserver = (String) request.getParameter("conserver");

                if (conserver != null) {
                    Cookie cookiePSD = new Cookie("cookiePSD", psd);
                    Cookie cookiePWD = new Cookie("cookiePWD", pwd);

                    cookiePSD.setMaxAge(60 * 60 * 24 * 7);
                    cookiePWD.setMaxAge(60 * 60 * 24 * 7);

                    response.addCookie(cookiePSD);
                    response.addCookie(cookiePWD);
                }

                response.sendRedirect("monProfil");
            } else {
                System.out.println("connexion echouee");
                gestionBD.Close();
                response.sendRedirect("accueil");
            }
        }
    }
}
