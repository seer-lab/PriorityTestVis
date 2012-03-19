package thesis.views;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

import thesis.Activator;

public class TimelineMouseClicker implements MouseListener{
	public TimelineMouseClicker(){super();}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDown(MouseEvent e) {
		System.out.println(e.button+" mouse button clicked");
		if(e.button==1){
			int x_location=e.x;
			int y_location=e.y;
			int lookingAt=findCurrentlySelected(x_location,y_location);
			if(Activator.SelectedTest!=lookingAt){
				Activator.SelectedTest=lookingAt;
				Timeline.update();
				
			}
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
//		int x_location=e.x;
//		int y_location=e.y;
		
	}
	
	private int findCurrentlySelected(int x, int y){
		//TODO for now I'm not going to use y coordinates
		//I'm just going to match the x value with the matching test
		//In the selected tests
		int total=0;
		for(int i=0;i<Timeline.selectedList.size();i++){
			total+=Timeline.selectedList.get(i).getTime()*Timeline.getUnselectedWidth()/Activator.TimeGoal;
			if(total>x)
				return i;
			
		}
		return Timeline.selectedList.size();
	}

}
