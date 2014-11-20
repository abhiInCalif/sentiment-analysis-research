import org.python.core.PyObject;
import org.python.util.PythonInterpreter;


public class WordFrequencyModuleFactory {
	
	public WordFrequencyModuleFactory() {
        String cmd = "from WordFrequency import WordFrequency";
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec(cmd);
        jyWordFrequencyModule = interpreter.get("WordFrequency");
    }
	
	public WordFrequencyModule createWordFrequencyModule() {
        PyObject employeeObj = jyWordFrequencyModule.__call__();
        return (WordFrequencyModule)employeeObj.__tojava__(WordFrequencyModule.class);
    }
	
	private PyObject jyWordFrequencyModule;
	
}
