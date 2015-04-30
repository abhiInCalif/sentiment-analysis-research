import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;


public class TopicSentimentExtractor implements PipelineComponent {
	
	private static final StanfordCoreInt stanfordCore = new StanfordCoreInt();
	private static int count = 0;

	@Override
	public void run(CorpusDocument doc) throws FileNotFoundException, UnsupportedEncodingException {
		// TODO Auto-generated method stub
		// when it runs, first it needs to get the topic distribution
		List<CoreMap> sentences = stanfordCore.getDocumentSentences(doc);
		PrintWriter writer = new PrintWriter("/Users/abhinavkhanna/Documents/Princeton/Independent Work/StanfordNLP/results/" + Integer.toString(count) + ".txt", "UTF-8");
		writer.println("Text::: " + doc.getBody() + ";;;");
		writer.println(); // print an extra newline into the output.
		
		Object[][] topicDistribution = doc.getTopicDistribution();
		Map<Object[], Double> topicSentiment = new HashMap<Object[], Double>();
		for (int i = 0; i < topicDistribution.length; i++) {
			Object[] topics = topicDistribution[i];
			if (topics.length == 0) continue; // skip this set of topics if they are empty
			
			List<CoreMap> relevantSentences = getRelevantSentences(sentences, topics);
			// then we take a nice socher scoring of each of these sentences.
			double topicSentimentScore = 0;
			int sentimentsAdded = 0;
			for (CoreMap sentence : relevantSentences) {
				Tree sentimentTree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
				int sentiment = RNNCoreAnnotations.getPredictedClass(sentimentTree);
				topicSentimentScore += sigmoid(sentiment);
				sentimentsAdded++;
			}
			topicSentimentScore = (double)(topicSentimentScore / (double) sentimentsAdded);
			topicSentiment.put(topics, topicSentimentScore);
			writer.print("Topics:::");
			for (int j = 0; j < topics.length; j++) {
				writer.print(" " + topics[j]);
			}
			writer.println(";;; Score::: " + topicSentimentScore + ";;;");
		}
		
		count++;
		writer.close();
	}
	
	private double sigmoid(double x) {
		return (1/( 1 + Math.pow(Math.E,(-1*(x-3)))));
	}
	
	private List<CoreMap> getRelevantSentences(List<CoreMap> sentences, Object[] topics) {
		List<CoreMap> relevantSentences = new ArrayList<CoreMap>();
		for(CoreMap sentence : sentences) {
			for (CoreLabel token: sentence.get(TokensAnnotation.class)) {
				String word = token.get(TextAnnotation.class);
				
				for (int i = 0; i < topics.length; i++) {
					String topic = (String) topics[i];
					if (topic.equals(word)) {
						if (!relevantSentences.contains(sentence)) {
							relevantSentences.add(sentence);
						}
					}
				}
			}
		}
		
		return relevantSentences;
	}
	
	@Override
	public Object getData() {
		// TODO Auto-generated method stub
		return null;
	}

}
