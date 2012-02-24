package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import thesis.data.TestResult;

public class Timeline extends ViewPart{
	public static final String ID = "testview.views.SampleView";
	private static ArrayList<TestResult> testData,selectedList,nonSelectedList;
	private static Canvas canvas;
	private static GC gc;
	private static Composite selectionAndPoolHolder;
	private static Group selectionHolder;
	private static Group poolHolder;
	private final static int kTotal_time=10000;
	private final static int kMax_kills=200;
	
	private static ArrayList<Integer> previously_detected_mutants=new ArrayList<Integer>();
	
	private static TimelinePainter tlPainter;
	
	
	
	public static void update(ArrayList<TestResult> tests){
		testData=tests;
		nonSelectedList=testData;
		selectedList.clear();
		selectTestsToAddToSet();
		updateGraphics();
	}
	
	@Override
	public void createPartControl(Composite parent) {
//		selectionAndPoolHolder=new Composite(parent, SWT.NONE);
//		selectionHolder=new Group(parent, SWT.SHADOW_NONE);
		canvas=new Canvas(parent,SWT.NO_REDRAW_RESIZE);
		gc=new GC(canvas);
		testData=new ArrayList<TestResult>();
		selectedList=new ArrayList<TestResult>();
		nonSelectedList=new ArrayList<TestResult>();
		tlPainter=new TimelinePainter(canvas,selectedList);
		canvas.addPaintListener(tlPainter);
	}

	private static void updateGraphics(){
		tlPainter.drawGraphics(gc);
	}
	@Override
	public void dispose() {
		gc.dispose();
	};
	
	public void setFocus() {
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
		tlPainter.update(selectedList);
	}
	
	private void removeTestFromSet(TestResult testToRemove){
		selectedList.remove(testToRemove);
		nonSelectedList.add(testToRemove);
	}
	
	private static void selectTestsToAddToSet(){
		int currentTime=0;
		if(nonSelectedList.size()>0){
			while(currentTime<kTotal_time){
				if(nonSelectedList.size()==0){
					break;
				}else{
					addTestToSet(nonSelectedList.get(0));
				}
			}
		}
	}


}
