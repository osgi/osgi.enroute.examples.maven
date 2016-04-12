package osgi.enroute.examples.eval.api;

/**
 *  Simple Expression evaluator
 */
public interface Eval {
	
	/**
	 * Evaluate an expression 
	 */
	
	double eval(String expression) throws Exception;
	
}
