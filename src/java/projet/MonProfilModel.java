package projet;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui sert de modèle à monProfil
 *
 * @author francis
 */
public class MonProfilModel extends ProfilModel {

    public List<String> lDemandeAmis = new ArrayList<>();

    /**
     * Constructeur appelé dans la servlet MonProfil
     *
     * @see ProfilModel#ProfilModel(projet.GestionBD, java.lang.String)
     * @see GestionBD#getDemandeAmis(java.lang.String)
     * @param gestionBD gestionBD le GestionBD utilisé pour communiquer avec la
     * base de données
     * @param profil le pseudonyme du profil actuellement consulté
     * @author francis
     */
    public MonProfilModel(GestionBD gestionBD, String profil) {
        super(gestionBD, profil);
        String demandeAmis = gestionBD.getDemandeAmis(profil);

        String[] parties = demandeAmis.split(",");
        String[] parties2;

        //même traitement que pour la liste d'amis
        for (String party : parties) {
            parties2 = party.split("-");
            lDemandeAmis.add(parties2[0]);
        }

    }

}
