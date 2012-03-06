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
	private final static Color kEclipseBackground=new Color(null,220,220,220);
	
	private final static Color kUnique=new Color(null, 74, 88, 155);//Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kNonUnique=new Color(null, 169, 126, 225);//Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	private final static Color kTrueUnique=new Color(null, 25, 30, 99);//Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	
	private static Canvas canvas;
	private static ArrayList<TestResult> unselectedList;

	public TimelinePainterTestPool(Canvas c,ArrayList<TestResult> list){canvas=c;unselectedList=list;}
	
	@Override
	public void paintControl(PaintEvent e) {drawGraphics(e.gc);}
	
	public void drawGraphics(GC gc){
		int current_x=0;
		int total_width=canvas.getClientArea().width;
		
//		gc.setBackground(kEclipseBackground);
//		gc.drawRectangle(0,0,total_width,canvas.getClientArea().height);
		
		double width_ratio=(double)total_width/kTotal_time;
		
		//Draw the Selected Tests
		for(int i=0;i<unselectedList.size();i++){
			TestResult test=unselectedList.get(i);
			int width=(int)(test.getTime()*width_ratio);
			drawTestResult(gc,test,current_x, width);
			current_x+=width;
		}
	}
	
	public void update(ArrayList<TestResult> list){unselectedList=list;}
	
	private static void drawTestResult(GC gc,TestResult test,int startx,int width){
		int total_height=canvas.getClientArea().height;
		double height_ratio=(double)total_height/(double)kMax_kills;
		
		int kills,killsUnique,killsTrueUnique;
		ArrayList<Integer> newly_detected_mutants=new ArrayList<Integer>();
		newly_detected_mutants.addAll(test.getDetectedMutants());
		ArrayList<TestResult> selectedList=Timeline.selectedList;
		
		kills=(int)(height_ratio*test.getDetectedMutants().size());
		
		int selectedPosition=Activator.SelectedTest;
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
		gc.fillRectangle(startx, total_height-kills, width, kills);
		
		//Draw unique kills
		//Because I am only placing on the end of the list, unique is equivalent
		//to true unique and thus unique will not be shown
		gc.setBackground(kUnique);
		gc.fillRectangle(startx, total_height-killsUnique, width, killsUnique);
		
		//Draw True Unique kills
		gc.setBackground(kTrueUnique);
		gc.fillRectangle(startx, total_height-killsTrueUnique, width, killsTrueUnique);
		
		//Draw outline of test
		gc.setForeground(kOutline);
		gc.drawRectangle(startx, total_height-kills, width, kills);
	}

}
