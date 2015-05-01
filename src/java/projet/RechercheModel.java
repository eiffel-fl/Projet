/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe qui sert de modèle au recherche
 *
 * @author francis
 */
public class RechercheModel {

    public List<String[]> lRecherche = new ArrayList<>();

    /**
     * Constructeur appelé dans la servlet Recherche
     *
     * @see GestionBD#getRecherche(java.lang.String, java.lang.String)
     * @param type
     * @param toSearch
     * @param gestionBD
     */
    public RechercheModel(String type, String toSearch, GestionBD gestionBD) {
        String recherches = gestionBD.getRecherche(type, toSearch);

        String[] parties = recherches.split(",");
        String[] parties2;

        //on exécute des traitements sur la chaîne retournée par la requête
        for (String party : parties) {
            //chaque ligne est un tableau de chaîne, on a donc une liste de String[]
            parties2 = party.split("-");
            lRecherche.add(parties2);
        }

    }

}
