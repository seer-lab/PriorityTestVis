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
	private final int total_time=10000;
	
	public Timeline(){super();}
	
	@Override
	public void createPartControl(Composite parent) {
		canvas=new Canvas(parent,SWT.NONE);
		testData=new ArrayList<TestResult>();
	}
	
	private void updateTestData(){
		testData=Activator.getDefault().testList;
	}

	private void updateGraphics(){
		int total_width=canvas.getClientArea().width;
		int total_height=canvas.getClientArea().height;
		double time_ratio=total_width/total_time;
		int running_time=0;
		int x=0;
		
		GC gc=new GC(canvas);
		System.out.println(testData.size()+" "+running_time);
		while(x<testData.size()&&running_time<total_time){
			double time=testData.get(x).getTime();
			running_time+=time;
			System.out.print(".");
			if(running_time>0){
				int i=(int)(time*time_ratio);
				System.out.println(time);
				gc.drawRectangle((int)(running_time-time),0,running_time,total_height);
			}
			x++;
		}
		//gc.drawRectangle(0,0,50,50);
		gc.dispose();
	}
	
	public void setFocus() {
		updateTestData();
		updateGraphics();
		canvas.setFocus();
	}

}
