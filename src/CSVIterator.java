import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;



public class CSVIterator implements Iterator<CorpusDocument> {
	private ArrayList<File> files;
	private ArrayList<NYTimesDoc> docs;
	private int rowIndex;
	
	public CSVIterator(String directory) {
		rowIndex = 0;
		docs = new ArrayList<NYTimesDoc>();
		files = new ArrayList<File>();
		File dir = new File(directory);
		for (final File fileEntry : dir.listFiles()) {
			if (!fileEntry.isDirectory()) {
				files.add(fileEntry);
			}
		}
		
		loadDocs();
	}
	
	private void loadDocs() {
		for (File fCSVFile : files) {
			if (!fCSVFile.getAbsolutePath().contains("file3")) continue;
			try {
				boolean insideQuotes = false;
				FileReader fCSVStream = new FileReader(fCSVFile);
				BufferedReader fCSVReader = new BufferedReader(fCSVStream);
				System.out.println(fCSVFile.getAbsolutePath());
				// we have the input stream, now we need to take in data from it.
				// every CSV file has a new line at the end of each line.
				// read to the newline?
				fCSVReader.readLine(); // ignore the first line.....
				String line = "";
				String body = "";
				while((line = fCSVReader.readLine()) != null) {
					if (line.equals("\"") && insideQuotes == false) {
						insideQuotes = true;
					} else if (line.equals("\"") && insideQuotes == true) {
						// we have reached the close of one section. create a doc out of this line
						NYTimesDoc doc = new NYTimesDoc();
						doc.setBody(body);
						this.docs.add(doc);
						
						// reset everything
						insideQuotes = false;
						body = "";
					} else {
						body += line + " ";
					}
				}
				fCSVReader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
	}

	@Override
	public boolean hasNext() {
		if (rowIndex < docs.size()) {
			return true;
		}
		return false;
	}

	@Override
	public CorpusDocument next() {
		// TODO Auto-generated method stub
		CorpusDocument doc = this.docs.get(rowIndex);
		this.rowIndex = this.rowIndex + 1;
		return doc;
	}

	@Override
	public void remove() {
		// TODO Auto-generated method stub
		
	}

}
