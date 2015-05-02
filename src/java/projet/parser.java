package projet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class parser {

    public DOMSource source;

    public parser(DocumentModel docModel, String id, GestionBD gestionBD) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("document");
            doc.appendChild(rootElement);
            
            Element lecture = doc.createElement("lecture");
            lecture.appendChild(doc.createTextNode(docModel.lecture));
            rootElement.appendChild(lecture);

            Element ecriture = doc.createElement("ecriture");
            ecriture.appendChild(doc.createTextNode(docModel.ecriture));
            rootElement.appendChild(ecriture);

            Element travailleurs = doc.createElement("travailleurs");
            rootElement.appendChild(travailleurs);

            for (String lTravailleur : docModel.lTravailleur) {
                Element travailleur = doc.createElement("travailleur");
                travailleur.appendChild(doc.createTextNode(lTravailleur));

                Attr attr = doc.createAttribute("auteur");
                if (lTravailleur.equals(docModel.auteur)) {
                    attr.setValue("oui");
                } else {
                    attr.setValue("non");
                }
                travailleur.setAttributeNode(attr);

                travailleurs.appendChild(travailleur);

            }

            Element contenu = doc.createElement("contenu");
            contenu.appendChild(doc.createTextNode(docModel.fic));
            rootElement.appendChild(contenu);

            source = new DOMSource(doc);
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        }
    }
}
