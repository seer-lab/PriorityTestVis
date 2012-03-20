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
			int lookingAt=findCurrentlySelected(e.x,e.y);
			if(Activator.SelectedTest!=lookingAt){
				Activator.SelectedTest=lookingAt;
//				Timeline.drawSelection();
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
	}

	@Override
	public void mouseUp(MouseEvent e) {}
	
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
