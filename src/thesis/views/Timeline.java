package thesis.views;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.part.ViewPart;

import thesis.Activator;
import thesis.data.TestResult;

public class Timeline extends ViewPart{
	
	/**Identifies this view for eclipse*/
	public static final String ID = "testview.views.SampleView";
	
	/**The list of all the Test data for the test suite*/
	public static ArrayList<TestResult> testData;
	/**The list of all tests which have been removed from the pool*/
	public static ArrayList<TestResult> selectedList;
	/**The list of all tests in the pool*/
	public static ArrayList<TestResult> nonSelectedList;
	
	private static Canvas canvasSelected,canvasUnselected;
	private static GC gcSelected,gcUnselected;
	private static Group selectionHolder;
	private static Group poolHolder;
	
	private static ArrayList<Integer> previously_detected_mutants=new ArrayList<Integer>();
	
	/**Paints graphics for the selected tests*/
	private static TimelinePainterSelectedTests tlPainterSelected;
	/**Paints graphics for the test pool*/
	private static TimelinePainterTestPool tlPainterUnSelected;
	/**Used to listen for hover events on the selected tests*/
	private static TimelineMouseHover tlMouseHoverSelected;
	/**Used to listen for hover events on the test pool*/
	private static TimelineMouseHover tlMouseHoverPool;
	/**Used to handle mouse clicks over the selected tests*/
	private static TimelineMouseClicker tlMouseClicker;
	
	private static TestPoolMouseListener tlPoolMouseListener;
	
	/**
	 *  
	 */
	public static void update(ArrayList<TestResult> tests){
		testData=tests;
		nonSelectedList=testData;
		selectedList.clear();
		selectTestsToAddToSet();
		updateGraphics();
	}
	
	/**If no tests are specified we only update the graphics*/
	public static void update(){
		updateGraphics();
	}
	
	public static void drawPool(){
		tlPainterUnSelected.drawGraphics(gcUnselected);
	}
	
	public static void drawSelection(){
		tlPainterSelected.drawGraphics(gcSelected);
	}
	public static void cleanUpAfterToolTip(){
		if(!Activator.poolTooltip)
			tlPainterSelected.drawAfterToolTip(gcSelected);
		else
			tlPainterUnSelected.drawAfterToolTip(gcUnselected);
	}
	
	@Override
	public void createPartControl(Composite parent) {
		FillLayout fill=new FillLayout();
		fill.type=SWT.VERTICAL;
		parent.setLayout(fill);
		selectionHolder=new Group(parent, SWT.SHADOW_NONE);
		selectionHolder.setText("Selected Tests");
		selectionHolder.setLayout(new FillLayout());
		canvasSelected=new Canvas(selectionHolder,SWT.NONE);
		gcSelected=new GC(canvasSelected);
		poolHolder=new Group(parent, SWT.SHADOW_NONE);
		poolHolder.setText("Test Pool");
		poolHolder.setLayout(new FillLayout());
		canvasUnselected=new Canvas(poolHolder, SWT.NONE|SWT.H_SCROLL);
		gcUnselected=new GC(canvasUnselected);
		
		testData=new ArrayList<TestResult>();
		selectedList=new ArrayList<TestResult>();
		nonSelectedList=new ArrayList<TestResult>();
		
		tlPainterSelected=new TimelinePainterSelectedTests(canvasSelected,selectedList);
		canvasSelected.addPaintListener(tlPainterSelected);
		tlMouseHoverSelected=new TimelineMouseHover(selectedList,false);
		canvasSelected.addMouseMoveListener(tlMouseHoverSelected);
		
		tlMouseHoverPool=new TimelineMouseHover(nonSelectedList,true);
		canvasUnselected.addMouseMoveListener(tlMouseHoverPool);
		
		tlMouseClicker=new TimelineMouseClicker();
		canvasSelected.addMouseListener(tlMouseClicker);
		
		tlPoolMouseListener=new TestPoolMouseListener();
		canvasUnselected.addMouseListener(tlPoolMouseListener);
		
		tlPainterUnSelected=new TimelinePainterTestPool(canvasUnselected, nonSelectedList);
		canvasUnselected.addPaintListener(tlPainterUnSelected);
	}
	
	/**Calls all required methods to update the graphics of the view*/
	private static void updateGraphics(){
		tlPainterSelected.drawGraphics(gcSelected);
		tlPainterUnSelected.drawGraphics(gcUnselected);
	}
	
	/**Sets focus for the timeline view*/
	public void setFocus() {
		selectionHolder.setFocus();
		poolHolder.setFocus();
	}
	
	
	/**This will add the test to the set, and remove it from the testpool*/
	public static void addTestToSet(TestResult testToAdd){
		nonSelectedList.remove(testToAdd);
		
		//Remove partial uniqueness
		testToAdd.removeTrueUniqueness(previously_detected_mutants);
		testToAdd.removeUniqueness(previously_detected_mutants);
		previously_detected_mutants.addAll(testToAdd.getDetectedMutants());
		
		//Remove true uniqueness
		for(int i=0;i<selectedList.size();i++){
			selectedList.get(i).removeTrueUniqueness(testToAdd.getDetectedMutants());
		}
		
		selectedList.add(testToAdd);
		
		updateListeners();
	}
	
	/***/
	public static void addTestToSet(int x){
		TestResult test=nonSelectedList.get(x);
		nonSelectedList.remove(x);
		
		//Remove partial uniqueness
		test.removeTrueUniqueness(previously_detected_mutants);
		test.removeUniqueness(previously_detected_mutants);
		previously_detected_mutants.addAll(test.getDetectedMutants());
		
		//Remove true uniqueness
		for(int i=0;i<selectedList.size();i++){
			selectedList.get(i).removeTrueUniqueness(test.getDetectedMutants());
		}
		if(Activator.SelectedTest>=0){
			selectedList.add(Activator.SelectedTest,test);
			Activator.SelectedTest++;
		}else{
			selectedList.add(test);
		}
		updateListeners();
		
		updateGraphics();
	}
	
	private static void updateListeners(){
		tlPainterSelected.update(selectedList);
		tlPainterUnSelected.update(nonSelectedList);
		tlMouseHoverSelected.update(selectedList); 
		tlMouseHoverPool.update(nonSelectedList);
	}
	
	/**This does not work yet, its just a place holder*/
	static void removeTestFromSet(int index){
		TestResult test=selectedList.get(index);
		selectedList.remove(index);
		nonSelectedList.add(test);
		
		//Remove the true unique mutants from previously detected
		previously_detected_mutants.removeAll(test.getTrueUniqueMutants());
		
		//Update the newly detected mutants to shift them to the later test that detects them
		ArrayList<Integer> mutants_detected=test.getUniqueMutants();
//		System.out.print(mutants_detected.size());
		for(int q=index;q<selectedList.size();q++){
			for(int i=0;i<mutants_detected.size();i++){
				if(selectedList.get(q).getDetectedMutants().contains(mutants_detected.get(i))){
					selectedList.get(q).getUniqueMutants().add(mutants_detected.get(i));
					mutants_detected.remove(i);
					i=0;
					System.out.print("<"+q+":"+i+">");
				}
			}
		}
//		System.out.println(":"+mutants_detected.size()+":"+test.getTrueUniqueMutants().size());
		
		previously_detected_mutants.removeAll(mutants_detected);
		
		
		updateListeners();
		updateGraphics();
	}
	
	/**This automatically selects tests to add in so I can see results before 
	 * creating all the mouselisteners I will need for adding tests.
	 */
	private static void selectTestsToAddToSet(){
		int currentTime=0;
		if(nonSelectedList.size()>0){
			while(currentTime<Activator.TimeGoal){
				if(nonSelectedList.size()==0){
					break;
				}else{
					currentTime+=nonSelectedList.get(0).getTime();
					addTestToSet(nonSelectedList.get(0));
				}
			}
		}
		tlPainterUnSelected.update(nonSelectedList);
	}

	/**Used so other classes can get the width from the canvas*/
	public static int getUnselectedWidth(){
		return Timeline.canvasUnselected.getClientArea().width;
	}

}
