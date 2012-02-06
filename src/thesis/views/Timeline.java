package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
		
		
		ArrayList<Integer> previously_detected_mutants=new ArrayList<Integer>();
		//Add the mutation blocks sequentialy until the time limit is reached
		int testsSelected=0; //This is a kludge variable used to store the number of tests selected
		while(x<testData.size()&&running_time<total_time){
			double time=testData.get(x).getTime();
			int kills=testData.get(x).getDetectedMutants().size();
			
			
			if(running_time<(total_time+time)){//We can add a new test now
				testsSelected++;
				testData.get(x).removeUniqueness(previously_detected_mutants);
				int uniqueKills=testData.get(x).getUniqueMutants().size();//TODO Move this until after the set of tests is selected
				
				previously_detected_mutants.addAll(testData.get(x).getDetectedMutants());
				int xStart=(int)(running_time*time_ratio);
				int xEnd=(int)((running_time+time)*time_ratio);
				int yEnd=(int)(kills*score_ratio);
				System.out.println(xStart+":"+xEnd);
				
				gc.setBackground(kNonUnique);
				gc.fillRectangle(xStart,total_height-yEnd,xEnd-xStart,yEnd);
				
				
				int yEnd2=(int)(uniqueKills*score_ratio);
				gc.setBackground(kUnique);
				gc.fillRectangle(xStart,total_height-yEnd2,xEnd-xStart,yEnd2);
				
				gc.setForeground(kOutline);
				gc.drawRectangle(xStart,total_height-yEnd,xEnd-xStart,yEnd);

			}
			running_time+=time;
			x++;
		}
		
		for(int i=0;i<testsSelected;i++){
			TestResult test=testData.get(x);
		}
		
		gc.dispose();
	}
	
	
	
	public void setFocus() {
		updateTestData();
		updateGraphics();
		canvas.setFocus();
	}

}
