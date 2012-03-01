package thesis.data;

import java.util.ArrayList;

public class TestResult {
	private int testID;
	public int getID(){return testID;}
	
	private double time;
	public double getTime(){return time;}
	
	private ArrayList<Integer> detectedMutants;
	public ArrayList<Integer> getDetectedMutants(){return detectedMutants;}
	
	private ArrayList<Integer> trueUniqueMutants;
	public ArrayList<Integer> getTrueUniqueMutants(){return trueUniqueMutants;}
	
	private ArrayList<Integer> uniqueMutants;
	public ArrayList<Integer> getUniqueMutants(){return uniqueMutants;}
	
	public void removeTrueUniqueness(ArrayList<Integer> comparableData){
		for(int x=0;x<comparableData.size();x++)
			if(trueUniqueMutants.contains(comparableData.get(x)))
				trueUniqueMutants.remove(comparableData.get(x));
	}
	
	public void removeUniqueness(ArrayList<Integer> comparableData){
		for(int x=0;x<comparableData.size();x++)
			if(uniqueMutants.contains(comparableData.get(x)))
				uniqueMutants.remove(comparableData.get(x));
	}
	
//	public void resetUniqueness(){
//		
//	}
	
	public int similarNumberOfTests(TestResult t){
		ArrayList<Integer> mutants=new ArrayList<Integer>();
//		int similarity=mutants.size();
		mutants.addAll(t.detectedMutants);
		mutants.removeAll(detectedMutants);
		
//		similarity-=mutants.size();
		
//		return similarity;
//		return mutants.size();
		return t.detectedMutants.size()-mutants.size();
	}
	
	
	public TestResult(int id,double t,ArrayList<Integer> detected){
		detectedMutants=new ArrayList<Integer>();
		trueUniqueMutants=new ArrayList<Integer>();
		uniqueMutants=new ArrayList<Integer>();
		testID=id;
		time=t;
		detectedMutants.addAll(detected);
		trueUniqueMutants=detected;
		uniqueMutants.addAll(detectedMutants);
	}
}
