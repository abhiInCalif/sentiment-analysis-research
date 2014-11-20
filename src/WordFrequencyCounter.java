import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * The sole responsibility of this class is to call the Jython code that implements the
 * Word Frequency counting.
 * @author abhinavkhanna
 *
 */
public class WordFrequencyCounter implements PipelineComponent {
	
	private Hashtable<String, Integer> frequencyTable; // over all documents in a corpus
	
	public WordFrequencyCounter() {
		frequencyTable = new Hashtable<String, Integer>();
	}
	
	private String removeStopWords(String body) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader("stoplists/en.txt"));
			String line;
			while ((line = br.readLine()) != null) {
				// process the line.
				String word = line.trim().toLowerCase();
				body = body.replaceAll("\\b" + word + "\\b", " ");
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return body;
	}
	
	private Map<String, Integer> countWordFrequency(String body) {
		
		body = removeStopWords(body);
		Map<String, Integer> docFrequencyTable = new HashMap<String, Integer>();
		
		body = body.replaceAll("[,\\.\\!;:]\\n\\t", " "); // replace all punctuation
		String[] words = body.split(" "); // split on the space
		for (int i = 0; i < words.length; i++) {
			String key = words[i].trim().toLowerCase().replace("\n", "").replace("\t", "");
			Integer value = frequencyTable.get(key);
			Integer docValue = docFrequencyTable.get(key);
			
			if (value == null) {
				// new entry
				frequencyTable.put(key, 1);
			} else {
				// existing entry
				value = value + 1; // update the value
				frequencyTable.put(key, value);
			}
			
			if (docValue == null) {
				// new entry
				docFrequencyTable.put(key, 1);
			} else {
				// existing entry
				docValue = docValue + 1; // update the value
				docFrequencyTable.put(key, docValue);
			}
		}
		
		return docFrequencyTable;
	}

	@Override
	public void run(CorpusDocument doc) {
		String body = doc.getBody();
		Map<String, Integer> docFT = countWordFrequency(body);
		doc.setWordFrequency(docFT); // set the document's individual FT.
		
//		// run the python word frequency count as well.
//		WordFrequencyModuleFactory wfmf = new WordFrequencyModuleFactory();
//		WordFrequencyModule wfm = wfmf.createWordFrequencyModule();
//		wfm.calculateWordFrequency(body);
	}

	@Override
	public Object getData() {
		return this.frequencyTable;
	}

}
