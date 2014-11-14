import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Purpose of this class is to host the entrance point for
 * Running the topic based Neural Net sentiment Classifier Pipeline
 * And other NLP tasks that may need to be performed.
 * @author abhinavkhanna
 *
 */
public class Pipeline {
	
	private ArrayList<PipelineComponent> pipeline;
	private XmlFileIterator iterator;
	
	public Pipeline() {
		// construct all pipeline elements
		pipeline = new ArrayList<PipelineComponent>();
		iterator = new XmlFileIterator("../corpus");
		StanfordCoreInt sentimentAnalysis = new StanfordCoreInt();
		MalletTopicModel malletTopicModel = new MalletTopicModel(Constants.NUM_TOPICS, 1.0, 0.01, 1, 100, 
													Constants.NUM_SUBTOPICS, Constants.DEBUG_ON);
		WordFrequencyCounter wfc = new WordFrequencyCounter();
		
		// add all existing components to the pipeline.
		pipeline.add(sentimentAnalysis);
		pipeline.add(malletTopicModel);
		pipeline.add(wfc);
	}
	
	/**
	 * Construct pipeline from a custom directory
	 * @param directory
	 */
	public Pipeline(String directory) {
		// construct all pipeline elements
		pipeline = new ArrayList<PipelineComponent>();
		iterator = new XmlFileIterator(directory);
		StanfordCoreInt sentimentAnalysis = new StanfordCoreInt();
		MalletTopicModel malletTopicModel = new MalletTopicModel(Constants.NUM_TOPICS, 1.0, 0.01, 1, 100, 
													Constants.NUM_SUBTOPICS, Constants.DEBUG_ON);
		WordFrequencyCounter wfc = new WordFrequencyCounter();
		
		// add all existing components to the pipeline.
		pipeline.add(sentimentAnalysis);
		pipeline.add(malletTopicModel);
		pipeline.add(wfc);
	}
	
	/**
	 * Runs the pipeline with all components
	 * enabled.
	 */
	public void run() {
		while(iterator.hasNext()) {
			CorpusDocument doc = iterator.next();
			for (int i = 0; i < pipeline.size(); i++) {
				pipeline.get(i).run(doc);
			}
			
		}
	}
	
	/**
	 * Runs only certain components of the pipeline
	 * @param components -- the numerical values for the components
	 * 		you wish to run.
	 */
	public void run(int[] components) {
		while(iterator.hasNext()) {
			CorpusDocument doc = iterator.next();
			for (int i = 0; i < components.length; i++) {
				if (components[i] >= pipeline.size()) {
					throw new RuntimeException("Incorrect component array!");
				}
				
				pipeline.get(components[i]).run(doc);
			}
			
		}
	}
	
	/**
	 * Runs a single component
	 * @param component
	 */
	public void run(int component) {
		while(iterator.hasNext()) {
			CorpusDocument doc = iterator.next();
			if (component >= pipeline.size()) {
				throw new RuntimeException("Incorrect component number!");
			}

			pipeline.get(component).run(doc);
		}
	}
	
	public Object getData(int component) {
		PipelineComponent componentObject = pipeline.get(component);
		return componentObject.getData();
	}
	
	/**
	 * Testing endpoint to run the pipeline
	 * @param args
	 */
	public static void main(String[] args) {
		// create a new pipeline
		Pipeline p;
		if (args.length > 0) {
			// corpus designation specified
			p = new Pipeline(args[0]);
		} else {
			p = new Pipeline();
		}
		
		if (args.length > 1) {
			// specific component specified to be run
			int component = Integer.parseInt(args[1]);
			p.run(component);
		} else {
			p.run();
		}
		
		// testing the WordFrequencyList;
		// time to print as csv.
		Hashtable<String, Integer> t = (Hashtable<String, Integer>) p.getData(2);
		System.out.println("Word, Occurence");
		for (String k : t.keySet()) {
			System.out.println("" + k + ", " + t.get(k));
		}
	}

}
