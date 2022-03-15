package confidenceIntervalCalculation;

public class Alpha implements Comparable<Alpha>{
	
	public Double alphaValue;
	
	public Alpha(double alphaValue){
		this.alphaValue=alphaValue;
	}
	
	public String toString(){
		return "a"+this.alphaValue;
	}

	@Override
	public int compareTo(Alpha o) {
		
		return this.alphaValue.compareTo(o.alphaValue);
	}

}
