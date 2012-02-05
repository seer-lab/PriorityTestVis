package thesis.views;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import thesis.Activator;
import thesis.data.TestResult;

public class Timeline extends ViewPart{
	public static final String ID = "testview.views.SampleView";
	private ArrayList<TestResult> testData;
	private Canvas canvas;
	
	public Timeline(){super();}
	
	@Override
	public void createPartControl(Composite parent) {
		canvas=new Canvas(parent,SWT.NONE);
	}

	private void updateGraphics(){
		
		GC gc=new GC(canvas);
		gc.drawRectangle(0,0,50,50);
		gc.dispose();
	}
	
	public void setFocus() {
		updateGraphics();
		canvas.setFocus();
	}

}
