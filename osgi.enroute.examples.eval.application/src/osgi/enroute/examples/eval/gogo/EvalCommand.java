package osgi.enroute.examples.eval.gogo;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import osgi.enroute.debug.api.Debug;
import osgi.enroute.examples.eval.api.Eval;

/**
 * This is the implementation. It registers the Gogo interface and calls it
 * through a Gogo command.
 * 
 */
@Component( //
		service = EvalCommand.class, //
		property = { //
				Debug.COMMAND_SCOPE + "=eval", //
				Debug.COMMAND_FUNCTION + "=expr" //
		}, //
		name = "osgi.enroute.examples.eval.gogo")
public class EvalCommand {

	@Reference
	private Eval target;

	public Object expr(String message) throws Exception {
		return target.eval(message);
	}
}
