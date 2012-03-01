package thesis.views;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.widgets.Display;

import thesis.Activator;

public class TimelineMouseSelector implements MouseMoveListener{
	public TimelineMouseSelector(){
		super();
	}
	@Override
	public void mouseMove(MouseEvent e) {
		int x_location=e.x;
		int y_location=e.y;
		int lookingAt=findCurrentlySelected(x_location,y_location);
		if(Activator.SelectedTest!=lookingAt){
			Activator.SelectedTest=lookingAt;
			Timeline.update();
			
		}
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
