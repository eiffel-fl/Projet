/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francis
 */
public class test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SftpException {

        String lol = "Thierry9000";
        GestionBD gbd = new GestionBD();

        if (gbd.Connexion(lol, lol)) {
            System.out.println(lol + " connected");

            System.out.println("\n Amis de " + lol);
            gbd.getAmis(lol);

            System.out.println("\n Document travaillé par " + lol);
            gbd.getTravailleSur(lol);

            System.out.println(gbd.sontAmis("Thierry9000", "Monique838"));
            System.out.println(gbd.sontAmis("Monique838", "Thierry9000"));
        } else {
            System.out.println(lol + " not connecte");
        }
        try {
            ChannelSftp sftp = (ChannelSftp) GestionBD.session.openChannel("sftp");
            sftp.connect();
            System.out.println(sftp.pwd());

            OutputStream out = sftp.put("grostestdelamort");
            out.close();

            sftp.disconnect();
        } catch (JSchException ex) {
            Logger.getLogger(test.class.getName()).log(Level.SEVERE, null, ex);
        }

        gbd.Close();
    }
}
