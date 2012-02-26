package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Display;

import thesis.data.TestResult;

public class TimelinePainter implements PaintListener {
	private final static int kTotal_time=10000;
	private final static int kMax_kills=200;
	
	private final static Color kOutline=Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	private final static Color kEclipseBackground=new Color(null,220,220,220);
	
	private final static Color kUnique=new Color(null, 74, 88, 155);//Display.getCurrent().getSystemColor( SWT.COLOR_BLUE);
	private static final Color kNonUnique=new Color(null, 25, 30, 99);//Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
	private final static Color kTrueUnique=new Color(null, 169, 126, 225);//Display.getCurrent().getSystemColor(SWT.COLOR_CYAN);
	
	
	private final static Color kNonSelectedNonUnique=Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED);
	private final static Color kNonSelectedUnique=Display.getCurrent().getSystemColor(SWT.COLOR_RED);
	private final static Color kNonSelectedTrueUnique=Display.getCurrent().getSystemColor(SWT.COLOR_MAGENTA);
	
	private static Canvas canvas;
	private static ArrayList<TestResult> selectedList;
	
	public TimelinePainter(Canvas c,ArrayList<TestResult> list){canvas=c;selectedList=list;}

	@Override
	public void paintControl(PaintEvent e) {
		drawGraphics(e.gc);
	}
	
	public void drawGraphics(GC gc){
		int current_x=0;
		int total_width=canvas.getClientArea().width;
		System.out.println(total_width);
		
//		gc.setBackground(kEclipseBackground);
//		gc.drawRectangle(0,0,total_width,canvas.getClientArea().height);
		
		double width_ratio=(double)total_width/kTotal_time;
		for(int i=0;i<selectedList.size();i++){
			TestResult test=selectedList.get(i);
			int width=(int)(test.getTime()*width_ratio);
			drawTestResult(gc,test,current_x, width, true);
			current_x+=width;
		}
	}
	
	public void update(ArrayList<TestResult> list){selectedList=list;}
	
	private static void drawTestResult(GC gc,TestResult test,int startx,int width,boolean detected){
		int total_height=canvas.getClientArea().height;
		double height_ratio=(double)total_height/(double)kMax_kills;
		
		int kills;
		Color nonUnique,unique,trueUnique;
		if(detected){
			nonUnique=kNonUnique;
			unique=kUnique;
			trueUnique=kTrueUnique;
		}else{
			nonUnique=kNonSelectedNonUnique;
			unique=kNonSelectedUnique;
			trueUnique=kNonSelectedTrueUnique;
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

}
