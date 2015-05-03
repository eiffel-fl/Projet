package projet.Servlet;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import projet.DocumentModel;
import projet.GestionBD;
import projet.Parser;

/**
 * Classe qui permet de proposer à l'utilisateur de telecharger le document au
 * format XML
 *
 * @author Guillaume
 */
public class Telecharger extends HttpServlet {

    /**
     * doPost qui permet de récupèrer des informations sur le document et de
     * créer son fichier XML
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @author Guillaume
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        GestionBD gestionBD = (GestionBD) session.getAttribute("gestionBD");

        String pseudo = (String) session.getAttribute("pseudo");

        String id = (String) request.getParameter("id"); //on récupère l'id grâce à l'url

        DocumentModel doc = new DocumentModel(gestionBD, id, pseudo);

        Parser p = new Parser(doc);

        ChannelSftp sftp = null;
        InputStream iS = null;
        OutputStream oS = null;
        BufferedOutputStream bOS = null;
        try {
            //Surement peu MVC mais on fini les traitements du document XML ici
            //En effet ainsi on ouvre un seul ChannelSftp pour la fin de création et le téléchargement
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            sftp = (ChannelSftp) gestionBD.getSession().openChannel("sftp");
            sftp.connect(); //on se connecte

            oS = sftp.put(doc.titre + id + ".xml"); //on créé le document

            StreamResult result = new StreamResult(oS);

            transformer.transform(p.source, result); //on le transforme en document XML

            iS = sftp.get(doc.titre + id + ".xml"); //on récupère un InputStream pour lire le document
            bOS = new BufferedOutputStream(response.getOutputStream(), 1024);

            SftpATTRS attr = sftp.stat(doc.titre + id + ".xml"); //SftpATTRS permet de récupèrer des infos sur le doc
            long size = attr.getSize();  //ici on récupère sa taille

            //on ajoute des informations à la response pour qu'elle propose le document au téléchargement
            response.reset();
            response.setContentType("text/xml");
            response.setBufferSize(10420);
            response.setHeader("Content-Length", String.valueOf(size));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + doc.titre + ".xml" + "\"");

            byte[] tamp = new byte[1024];
            int lgr;

            while ((lgr = iS.read(tamp)) > 0) {
                bOS.write(tamp, 0, lgr); //on écrit tamp dans la response
            }
        } catch (JSchException | TransformerException | SftpException ex) {
            Logger.getLogger(Telecharger.class.getName()).log(Level.SEVERE, null, ex);
        } finally { //on ferme ce dont on a plus besoin
            if (iS != null) {
                iS.close();
            }
            if (oS != null) {
                oS.close();
            }

            try {
                sftp.rm(doc.titre + id + ".xml"); //on détruit le document car on en a plus besoin
            } catch (SftpException ex) {
                Logger.getLogger(Telecharger.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (bOS != null) {
                bOS.close();
            }
            if (sftp != null) {
                sftp.disconnect();
            }
        }
    }
}
