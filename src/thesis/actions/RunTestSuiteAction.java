package thesis.actions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import thesis.Activator;
import thesis.data.TestResult;
import thesis.views.Timeline;

public class RunTestSuiteAction implements IWorkbenchWindowActionDelegate{
	private IWorkbenchWindow window;
	public RunTestSuiteAction(){System.out.println("Hello from runTestSuite - constructor");}
	@Override
	public void run(IAction action) {
		System.out.println("Hello from runTestSuite - run");
//		MessageDialog.openInformation(
//				window.getShell(),
//				"Thesis",
//				"Allow the user to select the test suite they wish to use and" +
//				"run it against the generated mutants" +
//				"\n "+Activator.getDefault().mutantList.size()+" mutants");
		//if(Activator.getDefault().mutantList.size()!=0){
			
			
			Activator.getDefault().testList.clear();
			Random rand=new Random();
			
			//Generate test data
			/**
			int mutants_created=Activator.mutantList.size();
			for(int x=0;x<157;x++){
				long time=rand.nextInt(300);
				time+=50;
//				try{Thread.sleep(time);}catch(InterruptedException e){};
				int number_killed=rand.nextInt(150)+50;
				ArrayList<Integer> detected_mutants=new ArrayList<Integer>();
				for(int n=0;n<number_killed;n++){
					detected_mutants.add(rand.nextInt(mutants_created));
				}
				TestResult e=new TestResult(x,time,detected_mutants);
				Activator.getDefault().testList.add(e);
			}
			**/
			
			String f1 = "C:/Users/100455689/Desktop/Thesis/muJava/testset/TOne.txt";
			String f2 = "C:/Users/100455689/Desktop/Thesis/muJava/testset/TTwo.txt";
			String f3 = "C:/Users/100455689/Desktop/Thesis/muJava/testset/TThree.txt";
			String f4 = "C:/Users/100455689/Desktop/Thesis/muJava/testset/TFour.txt";
			String f5 = "C:/Users/100455689/Desktop/Thesis/muJava/testset/TFive.txt";
			String fail = "FAILURES!!!";
			String success = "OK (1 test)";
			long time=300;
			int id = 0;
			
			try {
				ArrayList<BufferedReader> readers = new ArrayList<BufferedReader>();
				readers.add(new BufferedReader(new FileReader(f1)));
				readers.add(new BufferedReader(new FileReader(f2)));
				readers.add(new BufferedReader(new FileReader(f3)));
				readers.add(new BufferedReader(new FileReader(f4)));
				readers.add(new BufferedReader(new FileReader(f5)));
				
				// Read each file
				for(BufferedReader input : readers) {
					String line = "";
					int mutantNum = 0;
					ArrayList<Integer> detected = new ArrayList<Integer>();
					
					while ((line = input.readLine()) != null) {
						if(line.equals(success)) {
							mutantNum++;
							detected.add(mutantNum);
						} else if(line.equals(fail)) {
							mutantNum++;
						}
					}
					
					//time += 50;
					TestResult toAdd = new TestResult(id, time, detected);
					id++;
					Activator.getDefault().testList.add(toAdd);
					
					//try{Thread.sleep(time);}catch(InterruptedException e){};
					input.close();
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Hello from runTestSuite");
			for(TestResult i : Activator.getDefault().testList) {
				System.out.println(i.getID());
			}
			
			//Update canvas display with initial data
			Timeline.update(Activator.getDefault().testList);
			//Print out timeline data here
			//Because of static use, threading problems
		//}
		
	}

	@Override
	public void selectionChanged(IAction action, ISelection selection) {}

	@Override
	public void dispose() {}

	@Override
	public void init(IWorkbenchWindow window) {
		System.out.println("Hello from runTestSuite - init");
		this.window=window;
	}

}
