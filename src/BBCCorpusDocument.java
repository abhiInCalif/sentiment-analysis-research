import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;


public class BBCCorpusDocument {
	
	private String date;
	private String year;
	private String month;
	private String day;
	private String solrDatetime;
	private String headline;
	private String source;
	private String body;
	
	/** The annotation entries follow **/
	private ArrayList<CoreLabel> tokens;
	private ArrayList<Tree> sentenceTrees;
	private ArrayList<SemanticGraph> sentenceSemanticGraphs;
	private Map<Integer, CorefChain> coreferenceLinkGraph;
	private float documentSentiment;
	
	public BBCCorpusDocument() {
		this.setTokens(new ArrayList<CoreLabel>());
		this.setSentenceTrees(new ArrayList<Tree>());
		this.setSentenceSemanticGraphs(new ArrayList<SemanticGraph>());
		this.setCoreferenceLinkGraph(new HashMap<Integer, CorefChain>());
	}
	
	
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}
	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}
	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
	/**
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}
	/**
	 * @return the day
	 */
	public String getDay() {
		return day;
	}
	/**
	 * @param day the day to set
	 */
	public void setDay(String day) {
		this.day = day;
	}
	/**
	 * @return the solrDatetime
	 */
	public String getSolrDatetime() {
		return solrDatetime;
	}
	/**
	 * @param solrDatetime the solrDatetime to set
	 */
	public void setSolrDatetime(String solrDatetime) {
		this.solrDatetime = solrDatetime;
	}
	/**
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}
	/**
	 * @param headline the headline to set
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}
	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}
	/**
	 * @return the body
	 */
	public String getBody() {
		return body;
	}
	/**
	 * @param body the body to set
	 */
	public void setBody(String body) {
		this.body = body;
	}


	public ArrayList<CoreLabel> getTokens() {
		return tokens;
	}


	public void setTokens(ArrayList<CoreLabel> tokens) {
		this.tokens = tokens;
	}
	
	public void addToken(CoreLabel token) {
		this.tokens.add(token);
	}


	public ArrayList<Tree> getSentenceTrees() {
		return sentenceTrees;
	}


	public void setSentenceTrees(ArrayList<Tree> sentenceTrees) {
		this.sentenceTrees = sentenceTrees;
	}
	
	public void addSentenceTree(Tree sentenceTree) {
		this.sentenceTrees.add(sentenceTree);
	}


	public ArrayList<SemanticGraph> getSentenceSemanticGraphs() {
		return sentenceSemanticGraphs;
	}


	public void setSentenceSemanticGraphs(ArrayList<SemanticGraph> sentenceSemanticGraphs) {
		this.sentenceSemanticGraphs = sentenceSemanticGraphs;
	}
	
	public void addSentenceSemanticGraph(SemanticGraph sentenceSemanticGraph) {
		this.sentenceSemanticGraphs.add(sentenceSemanticGraph);
	}


	public Map<Integer, CorefChain> getCoreferenceLinkGraph() {
		return coreferenceLinkGraph;
	}


	public void setCoreferenceLinkGraph(Map<Integer, CorefChain> coreferenceLinkGraph) {
		this.coreferenceLinkGraph = coreferenceLinkGraph;
	}
	
	public String toString() {
		String toString = "\n ========= Start Output ========== \n";
		StringBuilder st = new StringBuilder(toString);
//		StringBuilder st = new StringBuilder(); (testing purposes only)
		
		st.append("tokens \n \n");
		for (int i = 0; i < tokens.size(); i++) {
			st.append(tokens.get(i).toString()).append("\n");
		}
		
		st.append("sentenceTrees \n \n");
		for (int i = 0; i < this.sentenceTrees.size(); i++) {
			st.append(sentenceTrees.get(i).toString()).append("\n");
		}
		
		st.append("sentenceSemanticGraphs \n \n");
		for (int i = 0; i < sentenceSemanticGraphs.size(); i++) {
			st.append(sentenceSemanticGraphs.get(i).toString()).append("\n");
		}
		
		st.append("coreferenceLinkGraph \n \n");
		for (Entry<Integer, CorefChain> e : this.coreferenceLinkGraph.entrySet()) {
			st.append(e.toString()).append("\n");
		}
		st.append(this.documentSentiment);
		st.append("document sentiment: ").append(this.documentSentiment).append("\n");
		
		st.append("\n ============ End Output ============== \n");
		
		return st.toString();
	}


	public float getDocumentSentiment() {
		return documentSentiment;
	}


	public void setDocumentSentiment(float documentSentiment) {
		this.documentSentiment = documentSentiment;
	}
}
