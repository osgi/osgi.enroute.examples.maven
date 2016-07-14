package osgi.enroute.examples.eval.provider;

import java.lang.ref.WeakReference;
import java.lang.reflect.Proxy;
import java.util.List;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import com.google.common.math.DoubleMath;

//import com.google.common.math.DoubleMath;

import osgi.enroute.examples.eval.api.Eval;
import parsii.eval.Expression;
import parsii.eval.Function;
import parsii.eval.Parser;
import parsii.eval.Scope;

@Component
public class EvalImpl implements Eval {
	Scope scope = new Scope();

	@Activate
	void activate() {
		Function fact = new Function() {

			@Override
			public int getNumberOfArguments() {
				return 1;
			}

			@Override
			public double eval(List<Expression> args) {
				double value = args.get(0).evaluate();
				return DoubleMath.factorial((int) value);
			}

			@Override
			public boolean isNaturalFunction() {
				return true;
			}
		};

	 	 Parser.registerFunction("factorial", proxyToPreventStaleReferences(fact));
	}

	public double eval(String expression) throws Exception {
		Expression expr = Parser.parse(expression);
		return expr.evaluate();
	}

	/**
	 * We proxy the function because the registration is done with a static
	 * method. This static registration could cause a reference to our bundle
	 * long after we're gone. We therefore proxy the function via a weak
	 * reference. Ugly, but blame static methods!
	 * 
	 * @param fact
	 * @return
	 */
	private Function proxyToPreventStaleReferences(Function fact) {
		WeakReference<Function> ref = new WeakReference<>(fact);
		return (Function) Proxy.newProxyInstance(fact.getClass().getClassLoader(), new Class<?>[] { Function.class },
				(proxy, method, args) -> {
					Function function = ref.get();
					if (function == null)
						return Double.NaN;
					return method.invoke(function, args);
				});
	}
}
