package thesis.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class Timeline extends ViewPart{
	public static final String ID = "testview.views.SampleView";
	private TableViewer viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		// TODO Auto-generated method stub
		
	}

	public void setFocus() {viewer.getControl().setFocus();}

}
