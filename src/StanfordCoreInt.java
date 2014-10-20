import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations.CorefChainAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import cc.mallet.types.Instance;

import java.io.File;
import java.io.IOException;


public class StanfordCoreInt {

	private static Properties props = new Properties();
	private static StanfordCoreNLP pipeline;
	private static int totalThreads = 0;
	private static ArrayList<Thread> workers = new ArrayList<Thread>();
	private static ArrayList<BBCCorpusDocument> documents = new ArrayList<BBCCorpusDocument>();
	private static final int MAX_THREADS = 0;

	public static ArrayList<BBCCorpusDocument> readXML(String directory) {

		// define multiple threading class here
		class AnnotationRunner implements Runnable {
			private BBCCorpusDocument doc;

			AnnotationRunner(BBCCorpusDocument bbcDoc){
				doc = bbcDoc;
			}

			public void run() {
				applyOperations(doc);
				totalThreads--;
			}
		}

		try {			
			ArrayList<File> files = new ArrayList<File>();
			File dir = new File(directory);
			for (final File fileEntry : dir.listFiles()) {
				if (!fileEntry.isDirectory()) {
					files.add(fileEntry);
				}
			}

			for (int j = 0; j < files.size(); j++) {
				File fXmlFile = files.get(j);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);

				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();

				NodeList nList = doc.getElementsByTagName("item");

				for (int i = 0; i < nList.getLength(); i++) {

					Node nNode = nList.item(i);

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

						BBCCorpusDocument bbcDoc = new BBCCorpusDocument();
						bbcDoc.setBody(body);
						bbcDoc.setDate(date);
						bbcDoc.setYear(year);
						bbcDoc.setMonth(month);
						bbcDoc.setDay(day);
						bbcDoc.setSolrDatetime(solrDatetime);
						bbcDoc.setHeadline(headline);
						bbcDoc.setSource(source);
						
						if (totalThreads < MAX_THREADS) {
							Runnable annotateRunnable = new AnnotationRunner(bbcDoc);
							Thread worker = new Thread(annotateRunnable);
//							documents.add(bbcDoc);
							worker.start();
							workers.add(worker);
							totalThreads++;
						} else {
//							documents.add(bbcDoc);
							applyOperations(bbcDoc);
						}						
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// wait for all of them to finish, then proceed to make graph!
		for (int i = 0; i < workers.size(); i++) {
			try {
				workers.get(i).join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return documents;

	}
	
	public static void applyOperations(BBCCorpusDocument doc) {
		
//		annotate(doc);
		
		// process the topic model on the document as well
		ArrayList<BBCCorpusDocument> documents = new ArrayList<BBCCorpusDocument>();
		documents.add(doc);
		
		processTopicModels(documents);
	}

	public static void annotate(BBCCorpusDocument bbcDocument) {
		String text = bbcDocument.getBody();
		//	    System.out.println(text);

		// create an empty Annotation just with the given text
		Annotation document = new Annotation(text);

		// run all Annotators on this text
		pipeline.annotate(document);

		float documentSentiment = 0;

		// these are all the sentences in this document
		// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
		List<CoreMap> sentences = document.get(SentencesAnnotation.class);

		for(CoreMap sentence: sentences) {
			// traversing the words in the current sentence
			// a CoreLabel is a CoreMap with additional token-specific methods
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				// this is the text of the token
				String word = token.get(TextAnnotation.class);
				// this is the POS tag of the token
				String pos = token.get(PartOfSpeechAnnotation.class);
				// this is the NER label of the token
				String ne = token.get(NamedEntityTagAnnotation.class);
				//	        System.out.println(word + " " + pos + " " + ne);
				bbcDocument.addToken(token);
			}

			// this is the parse tree of the current sentence
			Tree tree = sentence.get(TreeAnnotation.class);
			bbcDocument.addSentenceTree(tree);
			//	      System.out.println(tree.toString());
			Tree sentimentTree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
			int sentiment = RNNCoreAnnotations.getPredictedClass(sentimentTree);
			documentSentiment += sentiment;

			// this is the Stanford dependency graph of the current sentence
			SemanticGraph dependencies = sentence.get(CollapsedCCProcessedDependenciesAnnotation.class);
			//	      System.out.println(dependencies.toString());

			bbcDocument.addSentenceSemanticGraph(dependencies);
		}

		// add document sentiment
		bbcDocument.setDocumentSentiment((float) documentSentiment / sentences.size());

		// This is the coreference link graph
		// Each chain stores a set of mentions that link to each other,
		// along with a method for getting the most representative mention
		// Both sentence and token offsets start at 1!
		Map<Integer, CorefChain> graph = 
				document.get(CorefChainAnnotation.class);
		bbcDocument.setCoreferenceLinkGraph(graph);

		System.out.println(bbcDocument.toString());
	}
	
	public static void processTopicModels(ArrayList<BBCCorpusDocument> documents) {
		BBCCorpusTopicModel bbcTopic = new BBCCorpusTopicModel(25, 1.0, 0.01, 1, 50);
		for (int i = 0; i < documents.size(); i++) {
			Instance inst = bbcTopic.createNewInstance(documents.get(i).getDate(), 
									documents.get(i).getHeadline(), documents.get(i).getBody());
			bbcTopic.addInstance(inst);
		}
		
		try {
			bbcTopic.processTopicModels();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution 
		props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref, sentiment");
		pipeline = new StanfordCoreNLP(props);

		System.out.println("Reached Here");
		
		// documents will be empty right now because of lack of memory issues
		ArrayList<BBCCorpusDocument> documents = readXML("../corpus");
		
		System.out.println(documents.size());
	}

}
