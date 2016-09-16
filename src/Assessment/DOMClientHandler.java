package Assessment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This class will read and write to an XML file which has stored IPAddresses
 * for the client.
 * 
 * @author Gary McGuire
 *
 */

public class DOMClientHandler {
	private static final String XMLFILE = "clientXmlFiles/serverIPAddresses.xml";
	private static final String XML_FILE = "clientXmlFiles/ServerId.xml";
	private static final String TRANSFORMER_HTTP = "{http://xml.apache.org/xslt}indent-amount";
	private Document serverIdHistory;

	public DOMClientHandler() {
	}

	public boolean serverIdsFileExists() {
		File serverIdsFile = new File(XMLFILE);
		return serverIdsFile.exists();
	}

	public void writeServerIdsToXML(List<String> serverIds)
			throws IOException, TransformerException, ParserConfigurationException {

		File serverIdsFile = new File(XML_FILE);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("root");

		for (String serverId : serverIds) {
			Element serverIdElement = document.createElement("serverid");
			serverIdElement.setTextContent(serverId);
			rootElement.appendChild(serverIdElement);
		}
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(TRANSFORMER_HTTP, "2");
		DOMSource source = new DOMSource(serverIdHistory);
		StreamResult result = new StreamResult(serverIdsFile);
		transformer.transform(source, result);
	}

	public List<String> readServerIdsFromXML() throws ParserConfigurationException, SAXException, IOException {

		List<String> serverIds = new ArrayList<>();
		File serverIdsFile = new File(XML_FILE);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(serverIdsFile);
		NodeList serverIdNodes = document.getElementsByTagName("serverid");
		int numberOfIds = serverIdNodes.getLength();
		for (int i = 0; i < numberOfIds; i++) {
			Node node = serverIdNodes.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				String serverId = ((Element) node).getTextContent();
				serverIds.add(serverId);
			}
		}
		return serverIds;
	}

	/**
	 * Creates and writes a XML file to store IPAddresses.
	 * 
	 * @param serverIPAddresses
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */
	public void writeServerIPAddressesToXML(List<String> serverIPAddresses)
			throws ParserConfigurationException, TransformerException, IOException {

		File serverIdsFile = new File(XMLFILE);
		serverIdsFile.createNewFile();
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

		Document document = documentBuilder.newDocument();
		Element rootElement = document.createElement("root");
		document.appendChild(rootElement);

		for (String serverIPAddress : serverIPAddresses) {
			Element serverIdElement = document.createElement("serveripAddress");
			serverIdElement.setTextContent(serverIPAddress);
			rootElement.appendChild(serverIdElement);
		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(TRANSFORMER_HTTP, "2");

		DOMSource source = new DOMSource(document);
		StreamResult result = new StreamResult(serverIdsFile);
		transformer.transform(source, result);

	}

	/**
	 * First checks to see if the XML file exists. If it does, it reads the
	 * contents and returns them in an ArrayList. If not, it returns a null
	 * ArrayList.
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */

	public List<String> readServerIdFromXml() throws ParserConfigurationException, SAXException, IOException {
		List<String> serverIPAddresses = new ArrayList<>();
		List<String> tempString = new ArrayList<>();
		File serverIPAddressFile = new File(XMLFILE);
		if (serverIPAddressFile.exists()) {
			DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(serverIPAddressFile);
			NodeList serverIdNodes = document.getElementsByTagName("serveripAddress");

			int numberOfNodes = serverIdNodes.getLength();

			for (int i = 0; i < numberOfNodes; i++) {
				Node node = serverIdNodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					String serverAddress = ((Element) node).getTextContent();
					serverIPAddresses.add(serverAddress);
				}
			}
			tempString = serverIPAddresses;

		} else {
			tempString = null;
		}
		return tempString;

	}
}
