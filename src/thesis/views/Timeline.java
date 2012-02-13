package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import thesis.Activator;
import thesis.data.TestResult;

public class Timeline extends ViewPart{
	public static final String ID = "testview.views.SampleView";
	private static ArrayList<TestResult> testData,selectedList,nonSelectedList;
	private static Canvas canvas;
	private static GC gc;
	private static Group selectionHolder;
	private final static int kTotal_time=10000;
	private final static int kMax_kills=200;
	
	private static ArrayList<Integer> previously_detected_mutants=new ArrayList<Integer>();
	
	private final static Color kOutline=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final static Color kUnique=Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kNonUnique=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	private final static Color kTrueUnique=Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	
	public static void update(ArrayList<TestResult> tests){
		testData=tests;
		nonSelectedList=testData;
		selectedList.clear();
		selectTestsToAddToSet();
		updateGraphics();
	}
	
	@Override
	public void createPartControl(Composite parent) {
//		selectionHolder=new Group(parent, SWT.SHADOW_NONE);
		canvas=new Canvas(parent,SWT.NONE);
		gc=new GC(canvas);
		testData=new ArrayList<TestResult>();
		selectedList=new ArrayList<TestResult>();
		nonSelectedList=new ArrayList<TestResult>();
	}

	private static void updateGraphics(){
		int current_x=0;
		int total_width=canvas.getClientArea().width;
		double width_ratio=(double)total_width/kTotal_time;
		for(int i=0;i<selectedList.size();i++){
			TestResult test=selectedList.get(i);
			int width=(int)(test.getTime()*width_ratio);
			drawTestResult(test,current_x, width, true);
			current_x+=width;
		}
	}
	
	private static void drawTestResult(TestResult test,int startx,int width,boolean detected){
		
		int total_height=canvas.getClientArea().height;
		double height_ratio=(double)total_height/(double)kMax_kills;
		
		int kills;
		Color nonUnique,unique,trueUnique;
		if(detected){
			nonUnique=kNonUnique;
			unique=kUnique;
			trueUnique=kTrueUnique;
		}else{//TODO change the colours for false
			nonUnique=kNonUnique;
			unique=kUnique;
			trueUnique=kTrueUnique;
		}
		//Draw non unique kills
		gc.setBackground(nonUnique);
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		gc.fillRectangle(startx, total_height-kills, width, kills);
		
		//Draw unique kills
		gc.setBackground(unique);
		kills=(int)(height_ratio*test.getUniqueMutants().size());
		gc.fillRectangle(startx, total_height-kills, width, kills);
		
		//Draw True Unique kills
		gc.setBackground(trueUnique);
		kills=(int)(height_ratio*test.getTrueUniqueMutants().size());
		gc.fillRectangle(startx, total_height-kills, width, kills);
		
		//Draw outline of test
		gc.setForeground(kOutline);
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		gc.drawRectangle(startx, total_height-kills, width, kills);
	}
	@Override
	public void dispose() {
		gc.dispose();
	};
	
	public void setFocus() {
		updateGraphics();
		canvas.setFocus();
	}
	
	private static void addTestToSet(TestResult testToAdd){
		nonSelectedList.remove(testToAdd);
		
		//Remove partial uniqueness
		testToAdd.removeTrueUniqueness(previously_detected_mutants);
		testToAdd.removeUniqueness(previously_detected_mutants);
		previously_detected_mutants.addAll(testToAdd.getDetectedMutants());
		
		//Remove true uniqueness
		for(int i=0;i<selectedList.size();i++){
			selectedList.get(i).removeTrueUniqueness(testToAdd.getDetectedMutants());
		}
		
		selectedList.add(testToAdd);
	}
	
	private void removeTestFromSet(TestResult testToRemove){
		selectedList.remove(testToRemove);
		nonSelectedList.add(testToRemove);
	}
	
	private static void selectTestsToAddToSet(){
		int currentTime=0;
		if(nonSelectedList.size()>0)
			while(currentTime<kTotal_time)
				addTestToSet(nonSelectedList.get(0));
	}

}
