package thesis.data;

import java.util.ArrayList;

public class TestResult {
	private int testID;
	public int getID(){return testID;}
	
	private double time;
	public double getTime(){return time;}
	
	private ArrayList<Integer> detectedMutants;
	public ArrayList<Integer> getDetectedMutants(){return detectedMutants;}
	
	public TestResult(int id,double t,ArrayList<Integer> detected){
		testID=id;
		time=t;
		detectedMutants=detected;
	}
}
