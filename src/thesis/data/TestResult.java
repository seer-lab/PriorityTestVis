package thesis.data;

import java.util.ArrayList;

public class TestResult {
	private int testID;
	public int getID(){return testID;}
	
	private double time;
	public double getTime(){return time;}
	
	private ArrayList<Integer> detectedMutants;
	public ArrayList<Integer> getDetectedMutants(){return detectedMutants;}
	
	private ArrayList<Integer> uniqueMutants;
	public ArrayList<Integer> getUniqueMutants(){return uniqueMutants;}
	
	public void removeUniqueness(ArrayList<Integer> comparableData){
		uniqueMutants=detectedMutants;
		
		for(int x=0;x<comparableData.size();x++)
			if(uniqueMutants.contains(comparableData.get(x)))
				uniqueMutants.remove(comparableData.get(x));
	}
	
	
	public TestResult(int id,double t,ArrayList<Integer> detected){
		testID=id;
		time=t;
		detectedMutants=detected;
		uniqueMutants=detected;
	}
}
