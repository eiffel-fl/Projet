package projet;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import java.util.logging.Level;
import java.util.logging.Logger;

import projet.Servlet.Document;

/**
 * Classe qui sert de modèle aux documents
 *
 * @author francis
 */
public class DocumentModel {

    public String titre;
    public String chemin;
    public String lecture;
    public String ecriture;
    public String auteur;
    public String fic;

    /**
     * Constructeur appelé lors d'un doGet de la servlet Document
     *
     * @see GestionBD#getDocument(java.lang.String)
     * @param gestionBD gestionBD le GestionBD utilisé pour communiquer avec la
     * base de données
     * @param id l'id du document
     * @param pseudo le pseudonyme de l'utilisateur courant
     */
    public DocumentModel(GestionBD gestionBD, String id, String pseudo) {
        String result = gestionBD.getDocument(id); //on récupère des informations sur le Document d'IDDocument id

        String[] parties = result.split(",");

        parties = parties[0].split("-");

        titre = parties[0];
        chemin = parties[1];
        lecture = parties[2];
        ecriture = parties[3];
        auteur = parties[4];

        fic = "";

        boolean amis = gestionBD.sontAmis(pseudo, auteur);

        if (lecture.equals("Public") || (lecture.equals("Amis") && amis) || pseudo.equals(auteur)) {
            //si le Document est "public" alors pas de soucis
            //si la lecture du Document est réservé aux amis on regarde si l'auteur et l'utilisateur sont amis
            //si l'utilisateur est l'auteur du Document alors pas de soucis
            try {
                //on créé cet objet qui permettra de creer des fichiers ou des dossiers mais aussi de les lire
                ChannelSftp sftp = (ChannelSftp) GestionBD.getSession().openChannel("sftp");
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
            } catch (IOException ex) {
                Logger.getLogger(DocumentModel.class.getName()).log(Level.SEVERE, null, ex);
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

    }

    /**
     * Setter qui va mettre à jour le document sur le serveur. Il renverra aussi
     * le bon endroit où il faut redirect (pas très propre puisque le setter est
     * non void)
     *
     * @param gestionBD gestionBD le GestionBD utilisé pour communiquer avec la
     * base de données
     * @param id l'id du document
     * @param pseudo le pseudonyme de l'utilisateur courant
     * @param fic le contenu du textarea utilisé pour l'édition
     * @return l'endroit sur lequel il faut redirect
     */
    public String setFic(GestionBD gestionBD, String id, String pseudo, String fic) {
        boolean amis = gestionBD.sontAmis(pseudo, auteur);

        if (ecriture.equals("Public") || (ecriture.equals("Amis") && amis) || pseudo.equals(auteur)) {
            if (!gestionBD.getExisteTravaille(pseudo, id)) {
                gestionBD.setTravailleSur(pseudo, id);
            }
            try {
                ChannelSftp sftp = (ChannelSftp) GestionBD.getSession().openChannel("sftp");
                sftp.connect();

                OutputStream streamFic = sftp.put(chemin, ChannelSftp.OVERWRITE); //OVERWRITE permettra d'écraser le contenu du fichier
                PrintStream streamPrint = new PrintStream(streamFic); //on créé un PrintStream sur l'OutputStream pour faciliter l'écriture

                streamPrint.print(fic); //on écrit

                this.fic = fic;

                //on ferme ce dont on a plus besoin
                streamPrint.close();
                streamFic.close();
                sftp.disconnect();
            } catch (JSchException | SftpException ex) {
                Logger.getLogger(Document.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(DocumentModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            //on traite les cas où l'utilisateur ne peut pas écrire le Document
            String erreur = "";
            if (ecriture.equals("Amis")) {
                erreur = "amis";
            }
            if (ecriture.equals("Utilisateur")) {
                erreur = "utilisateur";
            }
            return "document?id=" + id + "&erreur=" + erreur + "&auteur=" + auteur;
        }
        return "document?id=" + id;
    }
}
