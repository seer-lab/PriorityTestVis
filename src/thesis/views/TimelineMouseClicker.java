package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import thesis.Activator;
import thesis.data.TestResult;

public class TimelineMouseClicker implements MouseListener{
	private boolean isThisTheTestPool;
	public TimelineMouseClicker(boolean isPool){
		super();
		isThisTheTestPool=isPool;
	}
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if(!isThisTheTestPool) {
			System.out.println(e.button+" mouse button clicked on test suite");
			if(e.button==1){
				int lookingAt=findCurrentlySelected(e.x,e.y);
				if(Activator.SelectedTest!=lookingAt){
					Activator.SelectedTest=lookingAt;
//					Timeline.drawSelection();
					Timeline.update();
				}else{
					Activator.SelectedTest=-1;
					Timeline.update();
				}
			}else if(e.button==3){
				int lookingAt=findCurrentlySelected(e.x,e.y);
				if(lookingAt<Activator.SelectedTest)
					Activator.SelectedTest--;
				else if(lookingAt==Activator.SelectedTest)
					Activator.SelectedTest=-1;
				Timeline.removeTestFromSet(lookingAt);
			}else if(e.button==2){
				
			}
		} else {
			System.out.println(e.button+" mouse button clicked on test pool");
			if(e.button==1){
				/**
				int lookingAt=findCurrentlySelected(e.x,e.y);
				if(Activator.SelectedTest!=lookingAt){
					Activator.SelectedTest=lookingAt;
//					Timeline.drawSelection();
					Timeline.update();
				}else{
					Activator.SelectedTest=-1;
					Timeline.update();
				}
				**/
			}else if(e.button==3){
				int lookingAt=findCurrentlySelected(e.x,e.y);
				if(lookingAt<Activator.SelectedTest)
					Activator.SelectedTest--;
				else if(lookingAt==Activator.SelectedTest)
					Activator.SelectedTest=-1;
				Timeline.addTestToSet(lookingAt);
			}else if(e.button==2){
				
			}
		}
		
		
	}

	@Override
	public void mouseUp(MouseEvent e) {}
	
	private int findCurrentlySelected(int x, int y){
		//TODO for now I'm not going to use y coordinates
		//I'm just going to match the x value with the matching test
		//In the selected tests
		if(!isThisTheTestPool) {
			int total=0;
			for(int i=0;i<Timeline.testSuite.size();i++){
				total+=Timeline.testSuite.get(i).getTime()*Timeline.getUnselectedWidth()/Activator.TimeGoal;
				if(total>x) {
					System.out.println("Looking at " + i);
					return i;
				}
					
				
			}
			return Timeline.testSuite.size();
		} else {
			int total=0;
			for(int i=0;i<Timeline.unusedTests.size();i++){
				total+=Timeline.unusedTests.get(i).getTime()*Timeline.getUnselectedWidth()/Activator.TimeGoal;
				if(total>x) {
					System.out.println("Looking at " + i);
					return i;
				}
					
				
			}
			
			return Timeline.unusedTests.size();
		}
	}

}
