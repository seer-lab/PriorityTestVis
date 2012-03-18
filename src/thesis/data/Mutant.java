package thesis.data;

/**
 * @author Benjamin Waters
 * @version 1.0
 * @since 2012-03-17
 */
public class Mutant {
	private String change,file;
	public String getChange(){return change;}
	public String getFile(){return file;}
	
	private int line;
	public int getLine(){return line;}
	
	private boolean alive,previous_state;
	public boolean getAlive(){return alive;}
	public boolean getPreviousState(){return previous_state;}
	
	/**
	 * Sets the mutatns detection status, and updates previous state
	 * 
	 * @param b  Is it Alive
	 */
	public void setAlive(boolean b){
		previous_state=alive;
		alive=b;
	}
	
	/**
	 * Constructor that holds data on each mutant
	 * 
	 * @param file	The name of the file that was mutated
	 * @param line	The line on the file that was mutated
	 * @param change	The change that occured in the mutation
	 */
	public Mutant(String file,int line,String change){
		this.file=file;
		this.line=line;
		this.change=change;
		alive=previous_state=true;
	}
}
