package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Display;

import thesis.Activator;
import thesis.data.TestResult;

public class TimelineMouseHover implements MouseMoveListener{
	private final long HOVER_THIS_LONG=5000;
	private static long hoverTime=0;
	private int previously_looking_at=-1;//The test we were looking at after the last mouse event
	private ArrayList<TestResult> testData;
	public TimelineMouseHover(ArrayList<TestResult> tests){
		super();
		this.update(tests);
	}
	
	public void update(ArrayList<TestResult> tests){
		testData=tests;
	}
	@Override
	public void mouseMove(MouseEvent e) {
		int lookingAt=findCurrentlySelected(e.x,e.y);
		if(lookingAt!=previously_looking_at){
			hoverTime=System.currentTimeMillis();
			previously_looking_at=lookingAt;
		}else if(System.currentTimeMillis()-hoverTime>=HOVER_THIS_LONG){
			drawTheToolTip(e.x, e.y);
		}
//		if(Activator.SelectedTest!=lookingAt){
//			Activator.SelectedTest=lookingAt;
//			Timeline.update();
//		}
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
		return Timeline.selectedList.size();
	}
	
	private void drawTheToolTip(int x,int y){
		System.out.println("Hovered on "+previously_looking_at);
	}

}
