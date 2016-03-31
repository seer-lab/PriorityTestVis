package thesis.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
	public static ArrayList<TestResult> testPool;
	/**The list of all tests which have been removed from the pool*/
	public static ArrayList<TestResult> testSuite;
	/**The list of all tests in the pool*/
	public static ArrayList<TestResult> unusedTests;
	
	private static Canvas canvasSelected,canvasUnselected;
	private static GC gcSelected,gcUnselected;
	private static Group selectionHolder;
	private static Group poolHolder;
	
	private static ArrayList<Integer> previously_detected_mutants=new ArrayList<Integer>();
	
	/**Paints graphics for the selected tests*/
	private static TimelinePainterTestSuite tlPainterSelected;
	/**Paints graphics for the test pool*/
	private static TimelinePainterTestPool tlPainterUnSelected;
	/**Used to listen for hover events on the selected tests*/
	private static TimelineMouseHover tlMouseHoverSelected;
	/**Used to listen for hover events on the test pool*/
	private static TimelineMouseHover tlMouseHoverPool;
	/**Used to handle mouse clicks over the selected tests*/
	private static TimelineMouseClicker tlMouseClicker;
	
	// Target parameters for fitness functions set by user
	private double TargetTime;
	int TargetScore;
	
//	private static TestPoolMouseListener tlPoolMouseListener;
	
	/**
	 *  
	 */
	
	//TODO: Replace all assignment operators with a clear() and addAll()
	public static void update(ArrayList<TestResult> tests){
		//testData=tests;
		testPool.clear();
		testPool.addAll(tests);
		
		System.out.println("Hello from timeline.update");
		for(TestResult i : testPool) {
			System.out.println(i.getID() + "$");
		}
		
		//nonSelectedList=testData;
		unusedTests.clear();
		unusedTests.addAll(testPool);
		
		testSuite.clear();
		selectTestsToAddToSet();
		greedySolution(testSuite, unusedTests);
		tlPainterSelected.update(testSuite);
		tlPainterUnSelected.update(unusedTests);
		updateGraphics();
		
		for(TestResult i : testPool) {
			System.out.println("%%%%%% " + i.getID());
			for(Integer j : i.getDetectedMutants()) {
				System.out.println("D " + j.toString());
			}
			for(Integer j : i.getUniqueMutants()) {
				System.out.println("U " + j.toString());
			}
		}
	}
	
	/**If no tests are specified we only update the graphics*/
	public static void update(){
		System.out.println("Empty update");
		updateGraphics();
	}
	
	public static void drawPool(){
		tlPainterUnSelected.drawGraphics(gcUnselected);
	}
	
	public static void drawSelection(){
		tlPainterSelected.drawGraphics(gcSelected);
	}
//	public static void cleanUpAfterToolTip(){
//		if(!Activator.poolTooltip)
//			tlPainterSelected.drawAfterToolTip(gcSelected);
//		else
//			tlPainterUnSelected.drawAfterToolTip(gcUnselected);
//	}
	
	@Override
	public void createPartControl(Composite parent) {
		FillLayout fill=new FillLayout();
		fill.type=SWT.VERTICAL;
		parent.setLayout(fill);
		selectionHolder=new Group(parent, SWT.SHADOW_NONE);
		selectionHolder.setText("Selected Tests");
		selectionHolder.setLayout(new FillLayout());
		canvasSelected=new Canvas(selectionHolder,SWT.DOUBLE_BUFFERED);
		
		gcSelected=new GC(canvasSelected);
		poolHolder=new Group(parent, SWT.SHADOW_NONE);
		poolHolder.setText("Test Pool");
		poolHolder.setLayout(new FillLayout());
		canvasUnselected=new Canvas(poolHolder, SWT.DOUBLE_BUFFERED|SWT.H_SCROLL);
		gcUnselected=new GC(canvasUnselected);
		
		System.out.println("Clearing test data");
		testPool=new ArrayList<TestResult>();
		testSuite=new ArrayList<TestResult>();
		unusedTests=new ArrayList<TestResult>();
		
		tlPainterSelected=new TimelinePainterTestSuite(canvasSelected,testSuite);
		canvasSelected.addPaintListener(tlPainterSelected);
		tlMouseHoverSelected=new TimelineMouseHover(testSuite,false);
		canvasSelected.addMouseMoveListener(tlMouseHoverSelected);
		
		tlMouseHoverPool=new TimelineMouseHover(unusedTests,true);
		canvasUnselected.addMouseMoveListener(tlMouseHoverPool);
		
		tlMouseClicker=new TimelineMouseClicker();
		canvasSelected.addMouseListener(tlMouseClicker);
		
//		tlPoolMouseListener=new TestPoolMouseListener();
//		canvasUnselected.addMouseListener(tlPoolMouseListener);
		
		tlPainterUnSelected=new TimelinePainterTestPool(canvasUnselected, unusedTests);
		canvasUnselected.addPaintListener(tlPainterUnSelected);
	}
	
	/**Calls all required methods to update the graphics of the view*/
	private static void updateGraphics(){
		System.out.println("Hello from timeline.update graphics");
		for(TestResult i : testPool) {
			System.out.println(i.getID() + "@");
		}
		//Kludge solution to flicker problem
		for(int i=0;i<2;i++){
		tlPainterSelected.drawGraphics(gcSelected);
		tlPainterUnSelected.drawGraphics(gcUnselected);
		}
	}
	
	/**Sets focus for the timeline view*/
	public void setFocus() {
		selectionHolder.setFocus();
		poolHolder.setFocus();
	}
	
	
	/**This will add the test to the set, and remove it from the testpool*/
	public static void addTestToSet(TestResult testToAdd){
		unusedTests.remove(testToAdd);
		
		//Remove partial uniqueness
//		testToAdd.removeTrueUniqueness(previously_detected_mutants);
//		testToAdd.removeUniqueness(previously_detected_mutants);
//		previously_detected_mutants.addAll(testToAdd.getDetectedMutants());
//		
//		//Remove true uniqueness
//		for(int i=0;i<selectedList.size();i++){
//			selectedList.get(i).removeTrueUniqueness(testToAdd.getDetectedMutants());
//		}
		
		testSuite.add(testToAdd);
		
		calculateTestStats();
		
		updateListeners();
	}
	
	/***/
	public static void addTestToSet(int x){
		TestResult test=unusedTests.get(x);
		unusedTests.remove(x);
		
		
//		//Remove partial uniqueness
//		test.removeTrueUniqueness(previously_detected_mutants);
//		test.removeUniqueness(previously_detected_mutants);
//		previously_detected_mutants.addAll(test.getDetectedMutants());
//		
//		//Remove true uniqueness
//		for(int i=0;i<selectedList.size();i++){
//			selectedList.get(i).removeTrueUniqueness(test.getDetectedMutants());
//		}
		if(Activator.SelectedTest>=0){//TODO Recalculate test stats after add
			testSuite.add(Activator.SelectedTest,test);
			Activator.SelectedTest++;
		}else{
			testSuite.add(test);
		}
		
		calculateTestStats();
		
		updateListeners();
		
		updateGraphics();
	}
	
	private static void updateListeners(){
		tlPainterSelected.update(testSuite);
		tlPainterUnSelected.update(unusedTests);
		tlMouseHoverSelected.update(testSuite); 
		tlMouseHoverPool.update(unusedTests);
	}
	
	/**This does not work yet, its just a place holder*/
	static void removeTestFromSet(int index){
		TestResult test=testSuite.get(index);
		testSuite.remove(index);
		unusedTests.add(test);
		
		if(index<Activator.SelectedTest)
			Activator.SelectedTest--;
		else if(index==Activator.SelectedTest)
			Activator.SelectedTest=-1;
		
//		//Remove the true unique mutants from previously detected
//		previously_detected_mutants.removeAll(test.getTrueUniqueMutants());
//		
//		//Update the newly detected mutants to shift them to the later test that detects them
//		ArrayList<Integer> mutants_detected=test.getUniqueMutants();
//		ArrayList<Boolean> already_been_added=new ArrayList<Boolean>();
//		for(int i=0;i<mutants_detected.size();i++)
//			already_been_added.add(false);
//		
//		for(int q=index;q<selectedList.size();q++){
//			for(int i=0;i<mutants_detected.size();i++){
//				if(selectedList.get(q).getDetectedMutants().contains(mutants_detected.get(i))){
//					selectedList.get(q).getUniqueMutants().add(mutants_detected.get(i));
//					mutants_detected.remove(i);
//					i=0;
//				}
//			}
//		}
//		
//		previously_detected_mutants.removeAll(mutants_detected);
		
		calculateTestStats();
		
		updateListeners();
		updateGraphics();
	}
	
	/**This automatically selects tests to add in so I can see results before 
	 * creating all the mouselisteners I will need for adding tests.
	 */
	private static void selectTestsToAddToSet(){
		int currentTime=0;
		if(unusedTests.size()>0){
			while(currentTime<Activator.TimeGoal){
				if(unusedTests.size()==0){
					System.out.println("nonSelectedList = 0");
					break;
				}else{
					currentTime+=unusedTests.get(0).getTime();
					addTestToSet(unusedTests.get(0));
				}
			}
		}
		tlPainterUnSelected.update(unusedTests);
	}

	/**Used so other classes can get the width from the canvas*/
	public static int getUnselectedWidth(){
		return Timeline.canvasUnselected.getClientArea().width;
	}
	
	private static void calculateTestStats(){
		//Clear current stats
		for(int i=0;i<testSuite.size();i++){
			testSuite.get(i).resetUniqueness();
		}
		//Calculate new stats
		previously_detected_mutants.clear();
		for(int i=0;i<testSuite.size();i++){
			testSuite.get(i).removeUniqueness(previously_detected_mutants);
			testSuite.get(i).removeTrueUniqueness(previously_detected_mutants);
			previously_detected_mutants.addAll(testSuite.get(i).getDetectedMutants());
		}
		
		for(int i=0;i<testSuite.size();i++){
			for(int q=0;q<testSuite.size();q++){
				if(i!=q)
					testSuite.get(i).removeTrueUniqueness(testSuite.get(q).getDetectedMutants());
			}
		}
		
	}
	
	// Fitness function that gives higher score based on better mutantscore without going over target time
	// Higher scores are better
	private static int fitnessOnScore(ArrayList<TestResult> solution, double targetTime) {
		double time = 0.0;
		int score = 0;
		
		for(TestResult i: solution) {
			score += i.getUniqueMutants().size();
			time += i.getTime();
		}
		
		if(time > targetTime) {
			return 0;
		} else {
			return score;
		}
	}
	
	// Fitness function that gives higher score based on best time while maintining minimum mutant score
	// Higher scores are better
	private static double fitnessOnTime(ArrayList<TestResult> solution, int targetScore) {
		double time = 0.0;
		int score = 0;
		
		for(TestResult i: solution) {
			score += i.getUniqueMutants().size();
			time += i.getTime();
		}
		
		if(score < targetScore) {
			return Double.MIN_VALUE;
		} else {
			return 1/time;
		}
	}
	
	// Hash Map represents a test suite and its corresponding unused tests
	private void geneticRemove(HashMap<ArrayList<TestResult>,ArrayList<TestResult>> solutions) {
		double targetTime = 8000;
		
		// Perform genetic operations
		Set<ArrayList<TestResult>> keys = solutions.keySet();
		for(ArrayList<TestResult> i: keys) {
			greedySwitch(i, solutions.get(i));
			
			// Sort new solutions based on scores
			HashMap<ArrayList<TestResult>,Integer> scores = new HashMap<ArrayList<TestResult>,Integer>();
			int score = fitnessOnScore(i, targetTime);
			
		}
		
		// Evalute against stopping criteria
		
		// Call genericRemove with new solutions
		
	}
	
	private static void greedySolution(ArrayList<TestResult> testSuite, ArrayList<TestResult> unused) {
		//TODO: Should be set by user
		double targetTime = 8000;
		int targetScore = 0;
		
		double time = 0;
		Iterator<TestResult> iter = testSuite.iterator();
		while(iter.hasNext()) {
			TestResult i = iter.next();
			double newTime = time + i.getTime();
			
			if (newTime <= targetTime) {
				time = newTime;
			} else {
				iter.remove();
				unused.add(i);
			}
		}
		
		boolean changed = true;
		while(changed) {
			changed = greedySwitch(testSuite,unused);
		}
		
	}
	
	private static boolean greedySwitch(ArrayList<TestResult> testSuite, ArrayList<TestResult> unused) {
		TestResult max = unused.get(0);
		TestResult min = testSuite.get(0);
		
		for(TestResult i: testSuite) {
			if(i.getUniqueMutants().size() < min.getUniqueMutants().size()) {
				min = i;
			}
		}
		
		for(TestResult i: unused) {
			if(i.getUniqueMutants().size() > max.getUniqueMutants().size()) {
				max = i;
			}
		}
		
		if(min.getUniqueMutants().equals(0) && !(max.getUniqueMutants().equals(0))) {
			int testSuiteLoc = testSuite.indexOf(min);
			testSuite.remove(testSuiteLoc);
			testSuite.add(testSuiteLoc, max);
			
			int unusedLoc = unused.indexOf(max);
			unused.remove(unusedLoc);
			unused.add(unusedLoc, min);
			
			return true;
		}
		
		return false;
		
		
	}

}
