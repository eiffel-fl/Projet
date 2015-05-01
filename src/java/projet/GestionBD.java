package projet; //package general du projet

//import pour le SSH
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.Serializable;

//import pour le pool de connexion
import org.apache.commons.dbcp.BasicDataSource;

//import pour jdbc
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

//import pour les exceptions
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JavaBean qui permet les interactions entre l'application et la base de
 * données. A chaque connexion d'un utilisateur un bean sera instancié et stocké
 * dans sa session
 *
 * @author Francis
 */
public class GestionBD {
    /*
     Pour chaque GestionBD on aura une Connection, un PreparedStatement, un ResultSet
     et un ResultSetMetaData
     */

    private Connection c;
    private PreparedStatement pst;
    private ResultSet rs;
    private ResultSetMetaData rsmd;

    private static Session session = null; //La Session est un singleton qui permet de se connecter à mira

    //public BoneCP connexionPool = null; //Le connection pool est initialisé à nul
    private BasicDataSource ds = null;

    private int nbLignes; //nombre de ligne du rsmd

    /**
     * Méthode qui permet de faire un ssh. Méthode récupérée à cette adresse :
     * http://cryptofreek.org/2012/06/06/howto-jdbc-over-an-ssh-tunnel/
     * Nécéssite une bibliothèque : jsch.jar téléchargeable ici
     * http://www.jcraft.com/jsch/
     *
     * @param strSshUser login utilisé pour le SHH (login de la fac)
     * @param strSshPassword mot de passe correspondant à votre login
     * @param strSshHost adresse de mira
     * @param nSshPort un port
     * @param strRemoteHost localhost de mira
     * @param nLocalPort le port sur lequel vous serez branché avec mira
     * @param nRemotePort le port 3306 pour le jdbc
     * @throws JSchException
     */
    private static void doSshTunnel(String strSshUser, String strSshPassword, String strSshHost, int nSshPort, String strRemoteHost, int nLocalPort, int nRemotePort) throws JSchException {
        final JSch jsch = new JSch();
        session = jsch.getSession(strSshUser, strSshHost, 22);
        session.setPassword(strSshPassword);

        final Properties config = new Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
        session.setPortForwardingL(nLocalPort, strRemoteHost, nRemotePort);
    }

    /**
     * Le constructeur de la classe initie la connexion en créant le tunnel SHH
     * et la connexion
     *
     * @author Francis
     */
    public GestionBD() {
        System.out.println("On créé un gestionBD");
        try {
            String strSshUser = "lf03440m";                  // SSH loging username
            String strSshPassword = "16121994";                   // SSH login password
            String strSshHost = "mira.c2m.univ-st-etienne.fr";          // hostname or ip or SSH server
            int nSshPort = 22;                                    // remote SSH host port number
            String strRemoteHost = "localhost";  // hostname or ip of your database server
            int nLocalPort = 3366;                                // local port number use to bind SSH tunnel
            int nRemotePort = 3306;                               // remote port number of your database 
            String strDbUser = "lf03440m";                    // database loging username
            String strDbPassword = "UUSHJJPD";                    // database login password

            if (session == null) {
                doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);
            }

            if (session != null) {
                if (!session.isConnected()) {
                    doSshTunnel(strSshUser, strSshPassword, strSshHost, nSshPort, strRemoteHost, nLocalPort, nRemotePort);
                }
            }

            if (ds == null) {
                ds = new BasicDataSource();
                ds.setDriverClassName("com.mysql.jdbc.Driver");
                ds.setUsername(strDbUser);
                ds.setPassword(strDbPassword);
                ds.setUrl("jdbc:mysql://localhost:" + nLocalPort + "/" + strDbUser);
                ds.setPoolPreparedStatements(true);
            }

            c = ds.getConnection();
        } catch (SQLException | JSchException ex) {
            Logger.getLogger(GestionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Méthode qui affiche un rsmd, utilisé pour le debugging
     *
     * @param rs le ResultSet à afficher
     * @throws SQLException
     * @author Francis
     */
    public void afficherRes(ResultSet rs) throws SQLException {
        int nbColonnes;
        int i;

        String resultat = "";
        int numLigne = 1;

        rsmd = rs.getMetaData();
        nbColonnes = rsmd.getColumnCount();

        System.out.print("   ");
        for (i = 0; i < nbColonnes; i++) {
            System.out.print(rsmd.getColumnName(i + 1) + " ");
        }

        System.out.println();

        while (rs.next()) {
            for (i = 0; i < nbColonnes; i++) {
                resultat += rs.getString(i + 1) + " ";
            }

            System.out.println(numLigne + " : " + resultat);
            numLigne++;
            resultat = "";
        }
    }

    /**
     * Méthode appelé lorsqu'un utilisateur se connecte
     *
     * @param psd le pseudo de l'utilisateur
     * @param pwd le mot de passe de l'utilisateur
     * @return un booléen qui indique si la connexion s'est bien déroulée
     * @author Francis
     */
    public boolean Connexion(String psd, String pwd) {
        try {
            pst = c.prepareStatement("SELECT Pseudo FROM Utilisateur WHERE Pseudo = ? AND MotDePasse = PASSWORD(?);");
            pst.setString(1, psd);
            pst.setString(2, pwd);

            rs = pst.executeQuery();

            if (rs.next()) { //on teste si le rs contient "quelque chose"
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    /**
     * Cette méthode va intercaler des "délimiteurs" entre les chaînes résultats
     * du ResultSet. Cette chaine sera ensuite redécoupée dans les servlets pour
     * l'affichage dans les JSP.
     *
     * @param rs le ResultSet dont il faut extraire les informations
     * @return une chaine pouvant être exploitée par les servlets pour ensuite
     * être affichée dans les JSP
     * @author Francis
     */
    public String extraireInfosRS(ResultSet rs) {
        String toReturn = "";
        try {
            rsmd = rs.getMetaData();
            while (rs.next()) {
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    toReturn += rs.getString(i + 1) + "-"; //delimiteur sur les colonnes
                }
                toReturn += ","; //delimiteur sur les lignes
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return toReturn;
    }

    /**
     * Méthode utilisée pour lire et écrire un document. Le titre sera juste
     * affiché. Le chemin servira à ouvrir le document pour le lire ou écrire.
     * Ensuite les paramètres Lecture et Ecriture définieront si l'utilisateur
     * courant peut exécuter des actions sur le document. Le paramètre Auteur
     * vient compléter les deux précédents paramètres
     *
     * @param id l'IDDocument du Document à récupérer
     * @return une chaîne contenant des informations sur le document qui seront
     * utilisées par la servlet Document
     * @author Francis
     */
    public String getDocument(String id) {
        String resultat = "";
        try {
            pst = c.prepareStatement("SELECT Titre, Chemin, Lecture, Ecriture, Auteur FROM Document WHERE IDDocument = ?;");
            pst.setString(1, id);

            rs = pst.executeQuery();

            resultat = extraireInfosRS(rs);

        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    /**
     * Méthode qui "affiche" les amis de l'utilisateur.
     *
     * @param psd le pseudo de l'utilisateur
     * @author Francis
     * @return une chaîne exploitée par les servlet MonProfil et Profil qui
     * permettra dans les JSP monProfil.jsp et profil.jsp d'afficher les amis de
     * l'utilisateur
     */
    public String getAmis(String psd) {
        String resultat = "";
        try {
            pst = c.prepareStatement("SELECT Pseudo FROM Utilisateur WHERE Pseudo IN (SELECT Utilisateur1 FROM Amis WHERE Utilisateur2= ?) OR Pseudo IN (SELECT Utilisateur2 FROM Amis WHERE Utilisateur1=?);");
            pst.setString(1, psd);
            pst.setString(2, psd);

            rs = pst.executeQuery();

            resultat = extraireInfosRS(rs);

        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    /**
     * Méthode qui "affiche" les documents sur lequel travaille l'utilisateur.
     *
     * @param psd pseudo de l'utilisateur
     * @author Francis
     * @return une chaîne exploitée par les servlet MonProfil et Profil qui
     * permettra dans les JSP monProfil.jsp et profil.jsp d'afficher les
     * documents de l'utilisateur
     */
    public String getTravailleSur(String psd) {
        String resultat = "";
        try {
            pst = c.prepareStatement("SELECT IDDocument, Titre FROM Document WHERE IDDocument IN (SELECT Document FROM TravailleSur WHERE  Utilisateur=?);");
            pst.setString(1, psd);

            rs = pst.executeQuery();

            resultat = extraireInfosRS(rs);

        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    /**
     * Méthode qui affiche les Emetteurs des demandes d'amis où psd est le
     * Destinataire.
     *
     * @param psd le pseudo de l'utilisateur pour lequel on veut récupérer les
     * demandes d'amis
     * @return une chaîne exploitée par la servlet MonProfil pour afficher dans
     * la JSP monProfil les demandes d'amis reçues par l'utilisateur
     * @author Francis
     */
    public String getDemandeAmis(String psd) {
        String resultat = "";
        try {
            pst = c.prepareStatement("SELECT Emetteur FROM DemandeAmis WHERE Destinataire=?;");
            pst.setString(1, psd);

            rs = pst.executeQuery();

            resultat = extraireInfosRS(rs);

        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    /**
     * On peut faire une recherche soit par Titre de Document, soit par nom de
     * l'Auteur de Document.
     *
     * @param type le type de recherche peut prendre 2 valeurs : Titre ou Auteur
     * @param toSearch la chaîne à rechercher, puisqu'on utilise LIKE on peut
     * utiliser les joker SQL comme %
     * @return une chaîne exploitée par la servlet Recherche pour afficher les
     * documents dans la JSP recherche.jsp
     */
    public String getRecherche(String type, String toSearch) {
        String resultat = "";
        try {
            pst = c.prepareStatement("SELECT IDDocument, Titre, Auteur, Lecture, Ecriture, (SELECT COUNT(*) FROM TravailleSur WHERE Document = IDDocument) FROM Document WHERE ? LIKE ?;");
            pst.setString(1, type);
            pst.setString(2, toSearch);

            rs = pst.executeQuery();

            resultat = extraireInfosRS(rs);

        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    /**
     * Méthode qui étant donné le couple clé primaire (Titre, Auteur) renvoie
     * l'IDDocument associé. Méthode utilisée par servlet Document
     *
     * @param titre Titre du document dont on veut l'IDDocument associé
     * @param auteur Auteur du document dont on veut l'IDDocument associé
     * @return l'IDDocument correspondant sous la forme d'une chaîne
     * @author Francis
     */
    public String getIDDocument(String titre, String auteur) {
        String resultat = "";
        try {
            pst = c.prepareStatement("SELECT IDDocument FROM Document WHERE Titre=? AND Auteur=?;");
            pst.setString(1, titre);
            pst.setString(2, auteur);

            rs = pst.executeQuery();

            if (rs.next()) {
                resultat = rs.getString(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return resultat;
    }

    /**
     * Méthode appelée quand un utilisateur s'inscrit. Méthode utilisée par la
     * servlet Inscription.
     *
     * @param psd le pseudo de l'utilisateur
     * @param pwd le mot de passe de l'utilisateur
     * @param mail le mail de l'utilisateur
     * @return un booléen indiquant si la requête a bien été éxecutée
     * @author Francis
     */
    public boolean setNouvelUtilisateur(String psd, String pwd, String mail) {
        try {
            pst = c.prepareStatement("INSERT INTO Utilisateur VALUES(?, PASSWORD(?), ?);");
            pst.setString(1, psd);
            pst.setString(2, pwd);
            pst.setString(3, mail);

            nbLignes = pst.executeUpdate();

            if (nbLignes == 1) {
                return true;

            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Méthode appelée lorsqu'un utilisateur créé un document. Méthode utilisée
     * par la servlet CreerDocument
     *
     * @param titre le titre du document qui sera créé
     * @param lecture son paramètre de lecture(Public/Amis/Utilisateur)
     * @param ecriture son paramètre d'écriture(Public/Amis/Utilisateur)
     * @param auteur le pseudo de son auteur
     * @return un booléen indiquant si la requête a bien été éxecutée
     * @author Francis
     */
    public boolean setNouveauDocument(String titre, String lecture, String ecriture, String auteur) {
        //testé et approuvé
        try {
            pst = c.prepareStatement("INSERT INTO Document(Titre, Lecture, Ecriture, Auteur, Chemin) VALUES(?, ?, ?, ?, 'provisoire');");
            pst.setString(1, titre);
            pst.setString(2, lecture);
            pst.setString(3, ecriture);
            pst.setString(4, auteur);

            nbLignes = pst.executeUpdate();

            if (nbLignes == 1) {
                pst = c.prepareStatement("SELECT IDDocument FROM Document WHERE Titre = ? AND Auteur = ?");
                pst.setString(1, titre);
                pst.setString(2, auteur);

                rs = pst.executeQuery();

                if (rs.next()) {
                    String id = rs.getString(1);
                    setTravailleSur(auteur, id);

                    pst = c.prepareStatement("UPDATE Document SET Chemin=? WHERE IDDocument=?");
                    pst.setString(1, "Projet/" + auteur + "/" + id);
                    pst.setString(2, id);

                    nbLignes = pst.executeUpdate();

                    if (nbLignes == 1) {
                        return true;
                    }

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Méthode qui créé un lien d'amitié entre 2 utilisateurs. Méthode appelée
     * par la servlet GererDemandeAmis
     *
     * @param psd1 pseudo de la première personne
     * @param psd2 pseudo de la seconde personne
     * @return un booléen indiquant si la requête a bien été éxecutée
     * @author Francis
     */
    public boolean setAmis(String psd1, String psd2) {
        //testé et approuvé
        try {
            pst = c.prepareStatement("INSERT INTO Amis VALUES(?, ?);");
            pst.setString(1, psd1);
            pst.setString(2, psd2);

            nbLignes = pst.executeUpdate();

            if (nbLignes == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Méthode appelé quand un utilisateur créé un document ou qu'il commence à
     * travailler sur un document. Méthode utilisée par les servlets Document et
     * CreerDocument
     *
     * @param psd Le pseudo de la personne qui a commencé à travailler sur le
     * document
     * @param IDDocument L'id du Document concerné
     * @return un booléen indiquant si la requête a bien été éxecutée
     * @author Francis
     */
    public boolean setTravailleSur(String psd, String IDDocument) {
        //testé et approuvé
        try {
            pst = c.prepareStatement("INSERT INTO TravailleSur VALUES(?, ?);");
            pst.setString(1, psd);
            pst.setString(2, IDDocument);

            nbLignes = pst.executeUpdate();

            if (nbLignes == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean setDemandeAmis(String pseudo, String pseudoAmi) {
        //INSERT INTO DemandeAmis VALUES('Gerard440', 'Thierry9000');
        try {
            pst = c.prepareStatement("INSERT INTO DemandeAmis VALUES(?, ?);");
            pst.setString(1, pseudo);
            pst.setString(2, pseudoAmi);

            nbLignes = pst.executeUpdate();

            if (nbLignes == 1) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Méthode qui verifie l'existence d'un lien d'amitié entre 2 membres.
     * Méthode appelée par la servlet Document
     *
     * @param psd1 Le pseudo du premier utilisateur
     * @param psd2 Le pseudo du second utilisateur
     * @return un boolean indiquant si les deux pseudos sont Amis
     * @author Francis
     */
    public boolean sontAmis(String psd1, String psd2) {
        try {
            pst = c.prepareStatement("SELECT * FROM Amis WHERE Utilisateur1=? AND Utilisateur2=? OR Utilisateur1=? AND Utilisateur2=?;");
            //On test dans les 2 sens car la relation d'amitié est symétrique
            pst.setString(1, psd1);
            pst.setString(2, psd2);

            pst.setString(3, psd2);
            pst.setString(4, psd1);

            rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;

            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Méthode appelée pour fermer tout ce qui est en rapport avec la base de
     * données et le "ssh"
     *
     * @author Francis
     */
    public void Close() {
        try {
            if (c != null) {
                c.close();
            }

            if (rs != null) {
                rs.close();
            }

            if (pst != null) {
                pst.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("tout est fermé");
    }

    /**
     * Méthode qui efface la ligne concernée de la table DemandeAmis. Méthode
     * appellée par la servlet GererDemandeAmis
     *
     * @param emetteur pseudo de l'emetteur de la demande
     * @param destinataire pseudo du destinataire de la demande
     */
    public void dropDemande(String emetteur, String destinataire) {
        //testé et approuvé
        try {
            pst = c.prepareStatement("DELETE FROM DemandeAmis WHERE Emetteur=? AND Destinataire=?;");
            pst.setString(1, emetteur);
            pst.setString(2, destinataire);

            pst.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean getExisteTravaille(String pseudo, String id) {
        try {
            pst = c.prepareStatement("SELECT * FROM TravailleSur WHERE Utilisateur=? AND Document=?;");
            //On test dans les 2 sens car la relation d'amitié est symétrique
            pst.setString(1, pseudo);
            pst.setString(2, id);

            rs = pst.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;

            }
        } catch (SQLException ex) {
            Logger.getLogger(GestionBD.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    /**
     * Getter sur la variable private c
     *
     * @return la Connection du Bean GestionBD
     */
    public Connection getC() {
        return c;
    }

    /**
     * Getter sur la variable private pst
     *
     * @return le PreparedStatement du Bean GestionBD
     */
    public PreparedStatement getPst() {
        return pst;
    }

    /**
     * Getter sur la variable private rs
     *
     * @return le ResultSet du Bean GestionBD
     */
    public ResultSet getRs() {
        return rs;
    }

    /**
     * Getter sur la variable private rsmd
     *
     * @return le ResulSetMetaData du Bean GestionBD
     */
    public ResultSetMetaData getRsmd() {
        return rsmd;
    }

    /**
     * Getter sur la variable private session
     *
     * @return la Session du Bean GestionBD
     */
    public static Session getSession() {
        return session;
    }

    /**
     * Getter sur la variable private ds
     *
     * @return le BasicDataSource du Bean GestionBD
     */
    public BasicDataSource getDs() {
        return ds;
    }

    /**
     * Getter sur la variable private nbLignes
     *
     * @return l'int du Bean GestionBD
     */
    public int getNbLignes() {
        return nbLignes;
    }
}
