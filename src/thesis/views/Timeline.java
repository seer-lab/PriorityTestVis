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
	private final int max_kills=200;
	
	private final Color kOutline=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final Color kUnique=Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private final Color kNonUnique=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	
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
		double time_ratio=(double)total_width/total_time;
		double score_ratio=(double)total_height/(double)max_kills;
		int running_time=0;
		int x=0;
		
		GC gc=new GC(canvas);
		while(x<testData.size()&&running_time<total_time){
//		if(testData.size()>0){
			double time=testData.get(x).getTime();
			int kills=testData.get(x).getDetectedMutants().size();
			
			if(running_time<(total_time+time)){
				int xStart=(int)(running_time*time_ratio);
				int xEnd=(int)((running_time+time)*time_ratio);
				int yEnd=(int)(kills*score_ratio);
				System.out.println(xStart+":"+xEnd);
				
				gc.setBackground(kUnique);
				gc.fillRectangle(xStart,total_height-yEnd,xEnd-xStart,yEnd);

				gc.setForeground(kOutline);
				gc.drawRectangle(xStart,total_height-yEnd,xEnd-xStart,yEnd);
			}
			running_time+=time;
			x++;
		}
		gc.dispose();
	}
	
	public void setFocus() {
		updateTestData();
		updateGraphics();
		canvas.setFocus();
	}

}
