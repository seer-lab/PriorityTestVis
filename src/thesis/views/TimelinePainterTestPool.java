package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import thesis.Activator;
import thesis.data.Mutant;
import thesis.data.TestResult;

public class TimelinePainterTestPool implements PaintListener{
	private final static int kTotal_time=Activator.TimeGoal;
	private final static int kMax_kills=200;
	private final static Color kOutline=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final static Color kEclipseBackground=new Color(null,237,236,235);
	private final static Color kToolTip=new Color(null,232,242,250);
	
	private final static Color kUnique=new Color(null, 74, 88, 155);
	private static final Color kNonUnique=new Color(null, 144, 179, 222);
	private final static Color kTrueUnique=new Color(null, 25, 30, 99);
	
	private static Canvas canvas;
	private static ArrayList<TestResult> unselectedList;
	

	private static boolean hasBeenUpdated=false;
	
	/**Used to hold the width so far of each of the tests*/
	private static ArrayList<Integer> widthSoFar=new ArrayList<Integer>();
	/**Used to hold the starting xvalues of each of the tests*/
	private static ArrayList<Integer> xStart=new ArrayList<Integer>();
	
	public TimelinePainterTestPool(Canvas c,ArrayList<TestResult> list){canvas=c;unselectedList=list;}
	
	@Override
	public void paintControl(PaintEvent e) {drawGraphics(e.gc);}
	
	public void drawGraphics(GC gc){
		
		if(hasBeenUpdated){
			hasBeenUpdated=false;
			gc.setBackground(kEclipseBackground);
			gc.fillRectangle(canvas.getClientArea());
		}
		
		
		//Draw the Tests
		for(int i=0;i<unselectedList.size();i++){
			TestResult test=unselectedList.get(i);
			drawTestResult(gc,test,i);
		}
		
		//Draw tooltip of Test hovered over
		drawToolTip(gc);
	}
	
	private void drawToolTip(GC gc){
		//Draw tooltip of Test hovered over
		if(Activator.HoverTest>=0&&Activator.poolTooltip&&unselectedList.size()>0){
			gc.setForeground(kOutline);
			gc.setBackground(kToolTip);
			TestResult selectedTest=unselectedList.get(Activator.HoverTest);
			gc.drawText(selectedTest.getDetectedMutants().size()+" Mutants Detected\n"
					+selectedTest.getUniqueMutants().size()+" Newly Detected Mutants\n"
					+selectedTest.getTrueUniqueMutants().size()+" Uniquely Detected Mutants\n"
					+selectedTest.getTime()/1000.0+" Seconds\n"
					,xStart.get(Activator.HoverTest+1), canvas.getClientArea().height/2);
		}
	}
	
	public void drawAfterToolTip(GC gc){
		if(Activator.HoverTest>-1&&Activator.HoverTest<unselectedList.size()){
			gc.setBackground(kEclipseBackground);
			gc.fillRectangle(xStart.get(Activator.HoverTest+1),0,
					canvas.getClientArea().width-widthSoFar.get(Activator.HoverTest+1),canvas.getClientArea().height);
			for(int i=Activator.HoverTest+1;i<unselectedList.size();i++){
				TestResult test=unselectedList.get(i);
				drawTestResult(gc,test,i);
			}
		}
	}
	
	public void update(ArrayList<TestResult> list){
		System.out.println("TestPool paint update");
		hasBeenUpdated=true;
		unselectedList=list;
		widthSoFar.clear();
		xStart.clear();
		double width_ratio=(double)canvas.getClientArea().width/kTotal_time;
		int runningTotal=0;
		for(int i=0;i<list.size();i++){
			xStart.add(runningTotal);
			runningTotal+=((int)(list.get(i).getTime()*width_ratio));
			widthSoFar.add((int)(list.get(i).getTime()*width_ratio));
		}
	}
	
	private static void drawTestResult(GC gc,TestResult test,int index){
		int total_height=canvas.getClientArea().height;
		double height_ratio=(double)total_height/(double)kMax_kills;
		
		int kills,killsUnique,killsTrueUnique;
		ArrayList<Integer> newly_detected_mutants=new ArrayList<Integer>();
		newly_detected_mutants.addAll(test.getDetectedMutants());
		ArrayList<TestResult> selectedList=Timeline.testSuite;
		
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		
		int selectedPosition=Activator.SelectedTest;
		if(selectedPosition<0)
			selectedPosition=selectedList.size();
		//loop through the selected list and remove any mutants for this test
		//That have already been detected, up to the selected position
		for(int i=0;i<selectedPosition;i++){
			for(int j=0;j<selectedList.get(i).getDetectedMutants().size();j++){
				if(newly_detected_mutants.contains(selectedList.get(i).getDetectedMutants().get(j))){
					newly_detected_mutants.remove(selectedList.get(i).getDetectedMutants().get(j));
				}
			}
		}
		killsUnique=(int)(height_ratio*newly_detected_mutants.size());
		
		//find true uniqueness by removing uniqueness after the selected position
		if(selectedPosition>=0)
			for(int i=selectedPosition;i<selectedList.size();i++){
				for(int j=0;j<selectedList.get(i).getDetectedMutants().size();j++){
					if(newly_detected_mutants.contains(selectedList.get(i).getDetectedMutants().get(j))){
						newly_detected_mutants.remove(selectedList.get(i).getDetectedMutants().get(j));
					}
				}
			}
		
		killsTrueUnique=(int)(height_ratio*newly_detected_mutants.size());

		//Draw non unique kills
		gc.setBackground(kNonUnique);
		gc.fillRectangle(xStart.get(index), total_height-kills, widthSoFar.get(index), kills);
		
		//Draw unique kills
		//Because I am only placing on the end of the list, unique is equivalent
		//to true unique and thus unique will not be shown
		gc.setBackground(kUnique);
		gc.fillRectangle(xStart.get(index), total_height-killsUnique, widthSoFar.get(index), killsUnique);
		
		//Draw True Unique kills
		gc.setBackground(kTrueUnique);
		gc.fillRectangle(xStart.get(index), total_height-killsTrueUnique, widthSoFar.get(index), killsTrueUnique);
		
		//Draw outline of test
		gc.setForeground(kOutline);
		gc.drawRectangle(xStart.get(index), total_height-kills, widthSoFar.get(index), kills);
		

	}


}
