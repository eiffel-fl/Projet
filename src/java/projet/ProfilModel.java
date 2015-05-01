/*
 * Copyright (C) 2015 francis
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package projet;

import java.util.ArrayList;
import java.util.List;
/**
 * Classe qui sert de modèle au profil
 *
 * @author francis
 */
public class ProfilModel {

    public List<String> lAmis = new ArrayList<>();
    public List<String[]> lDoc = new ArrayList<>();

    /**
     * Constructeur appelé dans la servlet Profil
     * 
     * @see GestionBD#getAmis(java.lang.String)
     * @see GestionBD#getTravailleSur(java.lang.String)
     * @param gestionBD le GestionBD utilisé pour communiquer avec la base de
     * données
     * @param profil le pseudonyme du profil actuellement consulté
     * @author francis
     */
    public ProfilModel(GestionBD gestionBD, String profil) {
        String amis = gestionBD.getAmis(profil);

        String[] parties = amis.split(",");
        String[] parties2;

        lAmis = new ArrayList<>();

        for (String party : parties) {
            parties2 = party.split("-");
            lAmis.add(parties2[0]);
        }

        String travailleSur = gestionBD.getTravailleSur(profil);
        parties = travailleSur.split(",");

        lDoc = new ArrayList<>();
        for (String party : parties) {
            parties2 = party.split("-");
            System.out.println(parties2[0]);
            System.out.println(parties2[1]);
            lDoc.add(parties2);
        }
    }
}
