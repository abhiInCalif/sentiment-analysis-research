import java.util.ArrayList;
import java.util.Map;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;


public class NYTimesDoc implements CorpusDocument {

	private String body;
	private ArrayList<CoreLabel> tokens;
	private ArrayList<Tree> sentenceTrees;
	private ArrayList<SemanticGraph> semanticGraph;
	private Map<Integer, CorefChain> coreferencelinkgraph;
	private float sentiment;
	private Object[][] topicDistro;
	private Map<String, Integer> wdfreq;

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setDate(String date) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getYear() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setYear(String year) {
		// TODO Auto-generated method stub	
	}

	@Override
	public String getMonth() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setMonth(String month) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getDay() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setDay(String day) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSolrDatetime() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setSolrDatetime(String solrDatetime) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getHeadline() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setHeadline(String headline) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSource() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public void setSource(String source) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBody() {
		// TODO Auto-generated method stub
		return this.body;
	}

	@Override
	public void setBody(String body) {
		// TODO Auto-generated method stub
		this.body = body;
	}

	@Override
	public ArrayList<CoreLabel> getTokens() {
		// TODO Auto-generated method stub
		return this.tokens;
	}

	@Override
	public void setTokens(ArrayList<CoreLabel> tokens) {
		// TODO Auto-generated method stub
		this.tokens = tokens;
	}

	@Override
	public void addToken(CoreLabel token) {
		// TODO Auto-generated method stub
		this.tokens.add(token);
	}

	@Override
	public ArrayList<Tree> getSentenceTrees() {
		// TODO Auto-generated method stub
		return this.sentenceTrees;
	}

	@Override
	public void setSentenceTrees(ArrayList<Tree> sentenceTrees) {
		// TODO Auto-generated method stub
		this.sentenceTrees = sentenceTrees;
	}

	@Override
	public void addSentenceTree(Tree sentenceTree) {
		// TODO Auto-generated method stub
		this.sentenceTrees.add(sentenceTree);
	}

	@Override
	public ArrayList<SemanticGraph> getSentenceSemanticGraphs() {
		// TODO Auto-generated method stub
		return this.semanticGraph;
	}

	@Override
	public void setSentenceSemanticGraphs(
			ArrayList<SemanticGraph> sentenceSemanticGraphs) {
		// TODO Auto-generated method stub
		this.semanticGraph = sentenceSemanticGraphs;
	}

	@Override
	public void addSentenceSemanticGraph(SemanticGraph sentenceSemanticGraph) {
		// TODO Auto-generated method stub
		this.semanticGraph.add(sentenceSemanticGraph);
	}

	@Override
	public Map<Integer, CorefChain> getCoreferenceLinkGraph() {
		// TODO Auto-generated method stub
		return this.coreferencelinkgraph;
	}

	@Override
	public void setCoreferenceLinkGraph(
			Map<Integer, CorefChain> coreferenceLinkGraph) {
		// TODO Auto-generated method stub
		this.coreferencelinkgraph = coreferenceLinkGraph;
	}

	@Override
	public float getDocumentSentiment() {
		// TODO Auto-generated method stub
		return this.sentiment;
	}

	@Override
	public void setDocumentSentiment(float documentSentiment) {
		// TODO Auto-generated method stub
		this.sentiment = documentSentiment;
	}

	@Override
	public void setTopicDistribution(Object[][] distribution) {
		// TODO Auto-generated method stub
		this.topicDistro = distribution;
	}

	@Override
	public Object[][] getTopicDistribution() {
		// TODO Auto-generated method stub
		return this.topicDistro;
	}

	@Override
	public void setWordFrequency(Map<String, Integer> frequencyTable) {
		// TODO Auto-generated method stub
		this.wdfreq = frequencyTable;
	}

	@Override
	public Map<String, Integer> getWordFrequency() {
		// TODO Auto-generated method stub
		return this.wdfreq;
	}

}
