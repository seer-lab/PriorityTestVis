package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;

import thesis.Activator;
import thesis.data.TestResult;

public class TimelineMouseHover implements MouseMoveListener{
	private final long HOVER_THIS_LONG=2000;
	private static long hoverTime=0;
	private int previously_looking_at=-1;//The test we were looking at after the last mouse event
	private ArrayList<TestResult> testData;
	private static boolean toolTipDrawn=false;
	private boolean isThisTheTestPool;
	public TimelineMouseHover(ArrayList<TestResult> tests,boolean isPool){
		super();
		this.update(tests);
		isThisTheTestPool=isPool;
	}
	
	public void update(ArrayList<TestResult> tests){
		testData=tests;
	}
	@Override
	public void mouseMove(MouseEvent e) {
		int lookingAt=findCurrentlySelected(e.x,e.y);
		if(lookingAt!=previously_looking_at){
//			Timeline.cleanUpAfterToolTip();
			
			Activator.HoverTest=Integer.MIN_VALUE;
			Activator.poolTooltip=isThisTheTestPool;
			hoverTime=System.currentTimeMillis();
			previously_looking_at=lookingAt;
			toolTipDrawn=false;
		}else if(System.currentTimeMillis()-hoverTime>=HOVER_THIS_LONG&&!toolTipDrawn){
			drawTheToolTip(e.x, e.y);
			toolTipDrawn=true;
		}
	}
	private int findCurrentlySelected(int x, int y){
		//TODO for now I'm not going to use y coordinates
		//I'm just going to match the x value with the matching test
		//In the selected tests
		int total=0;
		for(int i=0;i<testData.size();i++){
			total+=testData.get(i).getTime()*Timeline.getUnselectedWidth()/Activator.TimeGoal;
			if(total>x)
				return i;
			
		}
		return Timeline.testSuite.size();
	}
	
	private void drawTheToolTip(int x,int y){
		System.out.println("Hovered on "+previously_looking_at);
		Activator.HoverTest=previously_looking_at;
		if(Activator.poolTooltip)
			Timeline.drawPool();
		else
			Timeline.drawSelection();
	}

}
