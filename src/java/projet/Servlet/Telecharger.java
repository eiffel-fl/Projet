package projet.Servlet;

import com.jcraft.jsch.ChannelSftp;
import static com.jcraft.jsch.ChannelSftp.RESUME;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import projet.parser;

/**
 *
 * @author francis
 */
public class Telecharger extends HttpServlet {

    /**
     * Handles the HTTP <code>POST</code> method.
     *
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

        String id = (String) request.getParameter("id"); //on récupère l'id grâce à l'url

        DocumentModel doc = new DocumentModel(gestionBD, id, pseudo);

        parser p = new parser(doc, id, gestionBD);

        ChannelSftp sftp = null;
        InputStream iS = null;
        OutputStream oS = null;
        BufferedOutputStream bOS = null;
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            sftp = (ChannelSftp) gestionBD.getSession().openChannel("sftp");
            sftp.connect(); //on se connecte

            oS = sftp.put(doc.titre + id + ".xml");

            StreamResult result = new StreamResult(oS);

            transformer.transform(p.source, result);

            iS = sftp.get(doc.titre + id + ".xml");
            bOS = new BufferedOutputStream(response.getOutputStream(), 1024);

            SftpATTRS attr = sftp.stat(doc.titre + id + ".xml");
            long size = attr.getSize();
            
            response.reset();
            response.setContentType("text/xml");
            response.setBufferSize(10420);
            response.setHeader("Content-Length", String.valueOf(size));
            response.setHeader("Content-Disposition", "attachment; filename=\"" + doc.titre + ".xml" + "\"");

            byte[] tamp = new byte[1024];
            int lgr;
            while ((lgr = iS.read(tamp)) > 0) {
                bOS.write(tamp, 0, lgr);
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
                sftp.rm(doc.titre + id + ".xml");
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
