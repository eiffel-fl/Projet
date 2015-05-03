package projet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Classe qui permet de créer des documents XML à partir d'un DocumentModel
 *
 * @see DocumentModel
 * @author Guillaume
 */
public class Parser {

    public DOMSource source;

    /**
     * A partir de l'argument on créé le fichier XML
     * 
     * @param docModel le DocumentModel pour lequel on veut créer le fichier XML
     * associé
     */
    public Parser(DocumentModel docModel) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            //le fichier XML commence par la balise <document>
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("document");
            doc.appendChild(rootElement);

            //on ajoute ensuite la balise <titre> et sa valeur
            Element titre = doc.createElement("titre");
            titre.appendChild(doc.createTextNode(docModel.titre));
            rootElement.appendChild(titre);
            
            //même traitement qu'au dessus mais pour lecture
            Element lecture = doc.createElement("lecture");
            lecture.appendChild(doc.createTextNode(docModel.lecture));
            rootElement.appendChild(lecture);

            //même traitement qu'au dessus mais pour écriture
            Element ecriture = doc.createElement("ecriture");
            ecriture.appendChild(doc.createTextNode(docModel.ecriture));
            rootElement.appendChild(ecriture);

            //on créé un balise <travailleurs>
            Element travailleurs = doc.createElement("travailleurs");
            rootElement.appendChild(travailleurs);

            for (String lTravailleur : docModel.lTravailleur) {
                //Pour chaque travailleur on créé une balise <travailleur> avec son pseudonyme
                Element travailleur = doc.createElement("travailleur");
                travailleur.appendChild(doc.createTextNode(lTravailleur));

                //on ajoute un attribut auteur
                Attr attr = doc.createAttribute("auteur");
                if (lTravailleur.equals(docModel.auteur)) {
                    attr.setValue("oui"); //on le met à oui si le pseudonyme courant est l'auteur
                } else {
                    attr.setValue("non");
                }
                travailleur.setAttributeNode(attr); //on ajoute l'attribut

                //on ajoute la balise <travailleur> à <travailleurs>
                travailleurs.appendChild(travailleur);

            }

            //on créé une balise <contenu> qui contient le texte du document
            Element contenu = doc.createElement("contenu");
            contenu.appendChild(doc.createTextNode(docModel.fic));
            rootElement.appendChild(contenu);

            source = new DOMSource(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
}
