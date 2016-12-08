
package hu.elte.inetsense.probe;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import hu.elte.inetsense.common.util.PropertyUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import hu.elte.inetsense.probe.controller.InetsenseProbeController;
import hu.elte.inetsense.probe.view.ProbeView;

public class InetsenseProbeLauncher {

	// !!!!! WARNING !!!!!
	// logger cannot be used before spring context is created!!!
	// !!!!!!!!!!!!!!!!!!!!

	public static void main(final String[] args) throws InvocationTargetException, InterruptedException {

		// HACK: non-JVM argument parsing because of JNLP verification
		if (0 < args.length && args[0].equals("--headless=true")) {
			System.setProperty("headless", "true");
		}

		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ProbeConfiguration.class);
		context.registerShutdownHook();
		InetsenseProbeController app = context.getBean(InetsenseProbeController.class);
		initView(context, app);
		app.start();
		registerShutdownHook(context);
	}

	private static void initView(AnnotationConfigApplicationContext context, InetsenseProbeController app)
			throws InterruptedException, InvocationTargetException {
		ProbeView view = context.getBean(ProbeView.class);

		if (view == null) {
			app.setGuiEnabled(false);
		} else {
			app.setGuiEnabled(true);
			SwingUtilities.invokeAndWait(() -> {
				view.setVisible(true);
			});
		}
	}

	private static void registerShutdownHook(AnnotationConfigApplicationContext context) {
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		    	if(context != null) {
		    		context.close();
		    	}
		    }
		});
	}
}
