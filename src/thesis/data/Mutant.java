package thesis.data;

//This class holds all the data a Mutant should have
public class Mutant {
	private String change,file;
	public String getChange(){return change;}
	public String getFile(){return file;}
	
	private int line;
	public int getLine(){return line;}
	
	private boolean alive,previous_state;
	public boolean getAlive(){return alive;}
	public boolean getPreviousState(){return previous_state;}
	
	public void setAlive(boolean b){
		previous_state=alive;
		alive=b;
	}
	
	
	public Mutant(String file,int line,String change){
		this.file=file;
		this.line=line;
		this.change=change;
		alive=previous_state=true;
	}
}
