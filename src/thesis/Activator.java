package thesis;

/**
 * @author Ben Waters
 * @version 1.0
 * @since 2012-03-17
 */
import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import thesis.data.Mutant;
import thesis.data.TestResult;

/**
 * The activator class controls the plug-in life cycle
 * 
 * This class is largely auto-generated, but static variables related to the
 * entire plugin life have also been added in here
 */
public class Activator extends AbstractUIPlugin {

	/**The ID used for the plugin*/
	public static final String PLUGIN_ID = "thesis";

	/**The shared instance*/
	private static Activator plugin;
	
	/**The List of all mutants*/
	public static ArrayList<Mutant> mutantList;
	/**The Test Suite we are evaluating*/
	public static ArrayList<TestResult> testList;
	/**The time limit we have set for our test evaluation*/
	public static int TimeGoal=15000;
	
	/**The test the user has selected*/
	public static int SelectedTest=-1;
	public static int UnselectedTests=-1;
	
	/**The index of the test that is being hovered over*/
	public static int HoverTest=-1;
	
	/**Is the hovered test in the pool or the selected area*/
	public static boolean poolTooltip=false;
	
	/**
	 * The constructor initializes the static lists
	 */
	public Activator() {
		mutantList=new ArrayList<Mutant>();
		testList=new ArrayList<TestResult>();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
