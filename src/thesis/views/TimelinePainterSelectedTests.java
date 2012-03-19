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
import thesis.data.TestResult;

public class TimelinePainterSelectedTests implements PaintListener {
	private final static int kTotal_time=Activator.TimeGoal;
	private final static int kMax_kills=200;
	
	private final static Color kOutline=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final static Color kEclipse=new Color(null,237,236,235);
	private final static Color kToolTip=new Color(null,232,242,250);
	
	private final static Color kUnique=new Color(null, 74, 88, 155);//Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kNonUnique=new Color(null, 144, 179, 222);
	private final static Color kTrueUnique=new Color(null, 25, 30, 99);//Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	
	private final static Color kSpecialUnique=new Color(null, 104, 118, 185);//Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kSpecialNonUnique=new Color(null, 174, 209, 252);
	private final static Color kSpecialTrueUnique=new Color(null, 55, 60, 129);
	
	private final static Color kNonSelectedTrueUnique=Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
	
	private static Canvas canvas;
	private static ArrayList<TestResult> selectedList;
	
	public TimelinePainterSelectedTests(Canvas c,ArrayList<TestResult> list){canvas=c;selectedList=list;}
	
	/**Used to hold the width so far of each of the tests*/
	private static ArrayList<Integer> widthSoFar=new ArrayList<Integer>();
	/**Used to hold the starting xvalues of each of the tests*/
	private static ArrayList<Integer> xStart=new ArrayList<Integer>();

	@Override
	public void paintControl(PaintEvent e) {drawGraphics(e.gc);}
	
	public void drawGraphics(GC gc){
		
		gc.setBackground(kEclipse);
		gc.fillRectangle(canvas.getClientArea());
		
		
		//Draw the Selected Tests
		for(int i=0;i<selectedList.size();i++){
			TestResult test=selectedList.get(i);
			drawTestResult(gc,test,i);
		}
		
		//Draw outline on the test that is selected
		if(selectedList.size()>0){
			TestResult selectedTest=selectedList.get(Activator.SelectedTest);
			gc.setForeground(kNonSelectedTrueUnique);
			int kills=(int)((double)canvas.getClientArea().height/(double)kMax_kills*selectedTest.getDetectedMutants().size());
			gc.drawRectangle(xStart.get(Activator.SelectedTest), canvas.getClientArea().height-kills, widthSoFar.get(Activator.SelectedTest), kills);
		}
		
		drawToolTip(gc);
	}
	
	private void drawToolTip(GC gc){
		//Draw tooltip of Test hovered over
		if(Activator.HoverTest>=0&&!Activator.poolTooltip){
			gc.setForeground(kOutline);
			gc.setBackground(kToolTip);
			TestResult selectedTest=selectedList.get(Activator.HoverTest);
			gc.drawText(selectedTest.getDetectedMutants().size()+" Mutants Detected\n"
					+selectedTest.getUniqueMutants().size()+" Newly Detected Mutants\n"
					+selectedTest.getTrueUniqueMutants().size()+" Uniquely Detected Mutants\n"
					+selectedTest.getTime()/1000.0+" Seconds\n"
					,xStart.get(Activator.HoverTest+1), canvas.getClientArea().height/2);
		}
	}
	
	public void drawAfterToolTip(GC gc){
		if(Activator.HoverTest>-1&&Activator.HoverTest<selectedList.size()){
			gc.setBackground(kEclipse);
			gc.fillRectangle(xStart.get(Activator.HoverTest+1),0,
					canvas.getClientArea().width-widthSoFar.get(Activator.HoverTest+1),canvas.getClientArea().height);
			for(int i=Activator.HoverTest+1;i<selectedList.size();i++){
				TestResult test=selectedList.get(i);
				drawTestResult(gc,test,i);
			}
		}
	}
	
	public void update(ArrayList<TestResult> list){
		selectedList=list;
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
	
	private static void drawTestResult(GC gc,TestResult test,int index_of_this_test){
		int total_height=canvas.getClientArea().height;
		double height_ratio=(double)total_height/(double)kMax_kills;
		
		int kills,killsUnique,killsTrueUnique;

		//Draw non unique kills
		if(!(index_of_this_test==Activator.SelectedTest))
			gc.setBackground(kNonUnique);
		else
			gc.setBackground(kSpecialNonUnique);
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		gc.fillRectangle(xStart.get(index_of_this_test), total_height-kills, widthSoFar.get(index_of_this_test), kills);
		
		//Draw unique kills
		if(!(index_of_this_test==Activator.SelectedTest))
			gc.setBackground(kUnique);
		else
			gc.setBackground(kSpecialUnique);
		killsUnique=(int)(height_ratio*test.getUniqueMutants().size());
		gc.fillRectangle(xStart.get(index_of_this_test), total_height-killsUnique, widthSoFar.get(index_of_this_test), killsUnique);
		
		//Draw True Unique kills
		if(!(index_of_this_test==Activator.SelectedTest))
			gc.setBackground(kTrueUnique);
		else
			gc.setBackground(kSpecialTrueUnique);
		killsTrueUnique=(int)(height_ratio*test.getTrueUniqueMutants().size());
		gc.fillRectangle(xStart.get(index_of_this_test), total_height-killsTrueUnique, widthSoFar.get(index_of_this_test), killsTrueUnique);
		
		
		//Draw indicators for test relating to the selected test
		if(!(index_of_this_test==Activator.SelectedTest)&&Activator.SelectedTest>-1){
			int similarNumberOfTest=(int)(height_ratio*test.similarNumberOfTests(Timeline.testData.get(Activator.SelectedTest)));
			gc.setBackground(kNonSelectedTrueUnique);
			if(index_of_this_test<Activator.SelectedTest){
				gc.fillRectangle(xStart.get(index_of_this_test),total_height-killsUnique,widthSoFar.get(index_of_this_test),similarNumberOfTest);
			}else{
				gc.fillRectangle(xStart.get(index_of_this_test),total_height-kills,widthSoFar.get(index_of_this_test),similarNumberOfTest);
			}
		}
		
		//Draw outline of test
		gc.setForeground(kOutline);
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		gc.drawRectangle(xStart.get(index_of_this_test), total_height-kills, widthSoFar.get(index_of_this_test), kills);
	}

}
