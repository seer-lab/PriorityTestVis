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
	private final static Color kSelected=new Color(null,220,220,70);
	
	private final static Color kUnique=new Color(null, 74, 88, 155);//Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kNonUnique=new Color(null, 169, 126, 225);//Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	private final static Color kTrueUnique=new Color(null, 25, 30, 99);//Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	
	private final static Color kSpecialUnique=new Color(null, 104, 118, 185);//Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kSpecialNonUnique=new Color(null, 199, 176, 255);//Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	private final static Color kSpecialTrueUnique=new Color(null, 55, 60, 129);
	
	private final static Color kNonSelectedTrueUnique=Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
	
	private static Canvas canvas;
	private static ArrayList<TestResult> selectedList;
	
	public TimelinePainterSelectedTests(Canvas c,ArrayList<TestResult> list){canvas=c;selectedList=list;}

	@Override
	public void paintControl(PaintEvent e) {drawGraphics(e.gc);}
	
	public void drawGraphics(GC gc){
		int current_x=0;
		int total_width=canvas.getClientArea().width;
		
//		gc.setBackground(kEclipseBackground);
//		gc.drawRectangle(0,0,total_width,canvas.getClientArea().height);
		
		double width_ratio=(double)total_width/kTotal_time;
		
		//Draw the Selected Tests
		int runningTotal=0;
		for(int i=0;i<selectedList.size();i++){
			TestResult test=selectedList.get(i);
			int width=(int)(test.getTime()*width_ratio);
			
			boolean selected=(i==Activator.SelectedTest);
			if(i<Activator.SelectedTest)
				runningTotal+=width;
			drawTestResult(gc,test,current_x, width,selected,i);
			current_x+=width;
		}
		
		//Draw outline on the test that is moused over
		if(selectedList.size()>0){
			TestResult selectedTest=selectedList.get(Activator.SelectedTest);
			gc.setForeground(kSelected);
			int kills=(int)((double)canvas.getClientArea().height/(double)kMax_kills*selectedTest.getDetectedMutants().size());
			gc.drawRectangle(runningTotal, canvas.getClientArea().height-kills, (int)(selectedTest.getTime()*width_ratio), kills);
		}
		
	}
	
	public void update(ArrayList<TestResult> list){selectedList=list;}
	
	private static void drawTestResult(GC gc,TestResult test,int startx,int width,boolean selected,int index_of_this_test){
		int total_height=canvas.getClientArea().height;
		double height_ratio=(double)total_height/(double)kMax_kills;
		
		int kills,killsUnique,killsTrueUnique;

		//Draw non unique kills
		if(!selected)
			gc.setBackground(kNonUnique);
		else
			gc.setBackground(kSpecialNonUnique);
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		gc.fillRectangle(startx, total_height-kills, width, kills);
		
		//Draw unique kills
		if(!selected)
			gc.setBackground(kUnique);
		else
			gc.setBackground(kSpecialUnique);
		killsUnique=(int)(height_ratio*test.getUniqueMutants().size());
		gc.fillRectangle(startx, total_height-killsUnique, width, killsUnique);
		
		//Draw True Unique kills
		if(!selected)
			gc.setBackground(kTrueUnique);
		else
			gc.setBackground(kSpecialTrueUnique);
		killsTrueUnique=(int)(height_ratio*test.getTrueUniqueMutants().size());
		gc.fillRectangle(startx, total_height-killsTrueUnique, width, killsTrueUnique);
		
		
		//Draw indicators for test relating to the selected test
		if(!selected){
			int similarNumberOfTest=(int)(height_ratio*test.similarNumberOfTests(Timeline.testData.get(Activator.SelectedTest)));
			gc.setBackground(kNonSelectedTrueUnique);
			if(index_of_this_test<Activator.SelectedTest){
				gc.fillRectangle(startx,total_height-killsUnique,width,similarNumberOfTest);
			}else{
				gc.fillRectangle(startx,total_height-kills,width,similarNumberOfTest);
			}
		}
		
		//Draw outline of test
//		if(!selected)
			gc.setForeground(kOutline);
//		else
//			gc.setForeground(kNonSelectedTrueUnique);
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		gc.drawRectangle(startx, total_height-kills, width, kills);
	}

}
