import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XmlFileIterator implements Iterator<CorpusDocument>{

	private ArrayList<File> files;
	private int fileIndex = 0;
	private int docIndex = 0;
	private static final String UTF8_BOM = "\uFEFF";

	public XmlFileIterator(String directory) {
		files = new ArrayList<File>();
		File dir = new File(directory);
		for (final File fileEntry : dir.listFiles()) {
			if (!fileEntry.isDirectory()) {
				files.add(fileEntry);
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		if (fileIndex < files.size()) {
			NodeList nList = getNodeList(fileIndex);
			if (nList == null) {
				return false; // error case;
			}
			if(docIndex < nList.getLength()) {
				return true;
			}
		} else {
			return false;
		}

		return false;
	}

	@Override
	public CorpusDocument next() {
		CorpusDocument bbcDoc = new BBCCorpusDocument();
		NodeList nList = getNodeList(fileIndex);
		if (nList == null) {
			return null;
		}
		
		Node nNode = nList.item(docIndex);

		if (nNode.getNodeType() == Node.ELEMENT_NODE) {

			Element eElement = (Element) nNode;

			String body = eElement.getElementsByTagName("body").item(0).getTextContent();
			String date = eElement.getElementsByTagName("date").item(0).getTextContent();
			String year = eElement.getElementsByTagName("year").item(0).getTextContent();
			String month = eElement.getElementsByTagName("month").item(0).getTextContent();
			String day = eElement.getElementsByTagName("day").item(0).getTextContent();
			String solrDatetime = eElement.getElementsByTagName("solr-datetime").item(0).getTextContent();
			String headline = eElement.getElementsByTagName("headline").item(0).getTextContent();
			String source = eElement.getElementsByTagName("source").item(0).getTextContent();

			bbcDoc.setBody(body);
			bbcDoc.setDate(date);
			bbcDoc.setYear(year);
			bbcDoc.setMonth(month);
			bbcDoc.setDay(day);
			bbcDoc.setSolrDatetime(solrDatetime);
			bbcDoc.setHeadline(headline);
			bbcDoc.setSource(source);
		}
		
		// increment the docIndex and fileIndex properly
		docIndex++;
		if (docIndex == nList.getLength()) {
			// time to shift the fileIndex and set docIndex to 0
			fileIndex++;
			docIndex = 0;
		}
		
		// return the new item;
		return bbcDoc;
	}

	@Override
	public void remove() {
		// do nothing.
	}
	
	private NodeList getNodeList(int fileIndex) {
		DocumentBuilder dBuilder = null;
		NodeList nList = null;
		try {
			File fXmlFile = files.get(fileIndex);
			InputStream fXmlStream = new FileInputStream(fXmlFile);
			System.out.println(fXmlFile.getAbsolutePath());
			
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlStream);
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			nList = doc.getElementsByTagName("item");
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return nList;
	}

}
