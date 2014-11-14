/**
 * CODE MODIFIED FROM MALLET.
 * NO CLAIM THAT THIS IS THE ORIGINAL WORK OF ABHINAV KHANNA
 * SIMPLY A MODIFICATION
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.TokenSequenceRemoveStopwords;
import cc.mallet.topics.HierarchicalLDA;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;


public class MalletTopicModel implements PipelineComponent {
	
	private boolean DEBUG_ON = false;
	private int numSubTopics;
	private ArrayList<Pipe> pipeList;
	private InstanceList instances;
	private int numTopics;
	private double sumAlpha_t;
	private double beta_w;
	private int numThreads;
	private int numIterations;
	
	public MalletTopicModel(int numTopics, 
								double sumAlpha_t, double beta_w, int numThreads, 
								int numIterations, int numSubTopics, boolean debugOn) {
		this.numThreads = numThreads;
		this.numTopics = numTopics;
		this.sumAlpha_t = sumAlpha_t;
		this.beta_w = beta_w;
		this.numIterations = numIterations;
		this.numSubTopics = numSubTopics;
		this.DEBUG_ON = debugOn;
		this.pipeList = new ArrayList<Pipe>();
		
		// run through some nice cleanup.
		pipeList.add( new CharSequenceLowercase() );
		pipeList.add( new CharSequence2TokenSequence(Pattern.compile("\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
		pipeList.add( new TokenSequenceRemoveStopwords(new File("stoplists/en.txt"), "UTF-8", false, false, false) );
		pipeList.add( new TokenSequence2FeatureSequence() );
		
		this.instances = new InstanceList(new SerialPipes(pipeList));
	}
	
	public void addInstance(Instance instance) {
		this.instances.addThruPipe(instance);
	}
	
	public Instance createNewInstance(String filename, String name, String data) {
		Instance inst = new Instance(data, null, name, filename);
		return inst;
	}
	
	public void processHLDATopicModels() throws IOException {
		HierarchicalLDA hlda = new HierarchicalLDA();
		hlda.initialize(instances, instances, 10, new Randoms());
		
		hlda.samplePath(0, 10);
	}
	
	public Object[][] processParallelTopicModel() throws IOException {
		ParallelTopicModel model = new ParallelTopicModel(numTopics, sumAlpha_t, beta_w);

		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
		//  statistics after every iteration.
		model.setNumThreads(numThreads);

		// Run the model for 50 iterations and stop (this is for testing only, 
		//  for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(numIterations);
		model.estimate();
		
		Object[][] topicDistribution = (Object[][]) model.getTopWords(numSubTopics);
		
		if (DEBUG_ON) {
			model.printTopWords(System.out, numSubTopics, false);
			double[] topicProb = model.getTopicProbabilities(0);
			System.out.println(Arrays.toString(topicProb));
		}

		return topicDistribution;
	}

	@Override
	public void run(CorpusDocument doc) {
		try {
			// add documents to be modeled
			// restart the instance list at each iteration, since we are doing
			// this analysis 1 document at a time.
			this.instances = new InstanceList(new SerialPipes(pipeList));
			Instance inst = this.createNewInstance(doc.getDate(), 
												doc.getHeadline(), doc.getBody());
			this.addInstance(inst);
			Object[][] topicDistribution = processParallelTopicModel();
			doc.setTopicDistribution(topicDistribution);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * As of now this pipeline component really doesn't have
	 * any data that it would find useful in returning.
	 * Return null for now.
	 */
	public Object getData() {
		return null;
	}
}
