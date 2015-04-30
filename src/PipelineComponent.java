import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;


public interface PipelineComponent {
	
	public void run(CorpusDocument doc) throws FileNotFoundException, UnsupportedEncodingException;
	
	public Object getData();

}
