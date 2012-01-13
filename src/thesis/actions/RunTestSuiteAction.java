package thesis.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class RunTestSuiteAction implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;
	public RunTestSuiteAction(){}
	@Override
	public void run(IAction action) {
		MessageDialog.openInformation(
				window.getShell(),
				"Thesis",
				"Allow the user to select the test suite they wish to use and" +
				"run it against the generated mutants");
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
