package observations;

public class TransitionParameter implements Comparable<TransitionParameter>{
	String label;//x1,y3 etc.
	
	
	
	//returns, e.g. x, y, k from x1, y2 or k1
	public String getStateName(){
		return this.label.substring(0, 1);
	}
	
	public String getLabel(){
		return label;
	}
	
	public String toString(){
		return label;
	}
	
	public TransitionParameter(String label) {
		this.label= label;	
	}

	@Override
	public int compareTo(TransitionParameter o) {
		return (this.getLabel().compareTo(o.getLabel()));		
	}

	
}
