package thesis.actions;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import thesis.Activator;
import thesis.data.TestResult;

public class RunTestSuiteAction implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;
	public RunTestSuiteAction(){}
	@Override
	public void run(IAction action) {
		MessageDialog.openInformation(
				window.getShell(),
				"Thesis",
				"Allow the user to select the test suite they wish to use and" +
				"run it against the generated mutants" +
				"\n "+Activator.getDefault().mutantList.size()+" mutants");
		Random rand=new Random();
		
		for(int x=0;x<157;x++){
			long time=rand.nextInt(300);
			time+=50;
			try{Thread.sleep(time);}catch(InterruptedException e){};
			int number_killed=rand.nextInt(150)+50;
			ArrayList<Integer> detected_mutants=new ArrayList<Integer>();
			for(int n=0;n<number_killed;n++){
				detected_mutants.add(rand.nextInt(10000));
			}
			TestResult e=new TestResult(x,time,detected_mutants);
			Activator.getDefault().testList.add(e);
		}
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}

	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {
		this.window=window;
	}

}
