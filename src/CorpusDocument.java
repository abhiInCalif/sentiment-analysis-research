import java.util.ArrayList;
import java.util.Map;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.trees.Tree;


public interface CorpusDocument {
	
	/**
	 * @return the date
	 */
	public String getDate();
	
	/**
	 * @param date the date to set
	 */
	public void setDate(String date);
	
	/**
	 * @return the year
	 */
	public String getYear();
	
	/**
	 * @param year the year to set
	 */
	public void setYear(String year);
	
	/**
	 * @return the month
	 */
	public String getMonth();
	
	/**
	 * @param month the month to set
	 */
	public void setMonth(String month);
	
	/**
	 * @return the day
	 */
	public String getDay();
	
	/**
	 * @param day the day to set
	 */
	public void setDay(String day);
	
	/**
	 * @return the solrDatetime
	 */
	public String getSolrDatetime();
	
	/**
	 * @param solrDatetime the solrDatetime to set
	 */
	public void setSolrDatetime(String solrDatetime);
	
	/**
	 * @return the headline
	 */
	public String getHeadline();
	
	/**
	 * @param headline the headline to set
	 */
	public void setHeadline(String headline);
	
	/**
	 * @return the source
	 */
	public String getSource();
	
	/**
	 * @param source the source to set
	 */
	public void setSource(String source);
	
	/**
	 * @return the body
	 */
	public String getBody();
	
	/**
	 * @param body the body to set
	 */
	public void setBody(String body);
	
	public ArrayList<CoreLabel> getTokens();

	public void setTokens(ArrayList<CoreLabel> tokens);
	
	public void addToken(CoreLabel token);

	public ArrayList<Tree> getSentenceTrees();

	public void setSentenceTrees(ArrayList<Tree> sentenceTrees);
	
	public void addSentenceTree(Tree sentenceTree);

	public ArrayList<SemanticGraph> getSentenceSemanticGraphs();

	public void setSentenceSemanticGraphs(ArrayList<SemanticGraph> sentenceSemanticGraphs);
	
	public void addSentenceSemanticGraph(SemanticGraph sentenceSemanticGraph);

	public Map<Integer, CorefChain> getCoreferenceLinkGraph();

	public void setCoreferenceLinkGraph(Map<Integer, CorefChain> coreferenceLinkGraph);
	
	public String toString();

	public float getDocumentSentiment();

	public void setDocumentSentiment(float documentSentiment);
	
	public void setTopicDistribution(Object[][] distribution);
	
	public Object[][] getTopicDistribution();
	
	public void setWordFrequency(Map<String, Integer> frequencyTable);
	
	public Map<String, Integer> getWordFrequency();
	
}
