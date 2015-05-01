package projet;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class parser {

    public static void main(String argv[]) {

        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("document");
            doc.appendChild(rootElement);

            Element travailleurs = doc.createElement("travailleurs");
            rootElement.appendChild(travailleurs);

            Element travailleur = doc.createElement("travailleur");
            travailleur.appendChild(doc.createTextNode("Joseph"));
            int test = 0;
            if (travailleurs.getFirstChild() == null) {
                test = 1;
            }
            travailleurs.appendChild(travailleur);

            Attr attr = doc.createAttribute("auteur");
            if (test == 1) {
                attr.setValue("oui");
            } else {
                attr.setValue("non");
            }
            travailleur.setAttributeNode(attr);

            Element contenu = doc.createElement("contenu");
            contenu.appendChild(doc.createTextNode("un contenu quelconque."));
            rootElement.appendChild(contenu);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("./doc.xml"));

            transformer.transform(source, result);

            System.out.println("fichier sauvé.");

        } catch (ParserConfigurationException | TransformerException pce) {
            pce.printStackTrace();
        }
    }
}
