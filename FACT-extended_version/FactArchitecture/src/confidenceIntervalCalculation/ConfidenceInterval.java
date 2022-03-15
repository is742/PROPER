package confidenceIntervalCalculation;
/**
 * This class represents a confidence interval of the form 
 * [a,b] with confidence minusAlpha%
 * */
public class ConfidenceInterval extends Interval implements Comparable<ConfidenceInterval> {
	public Alpha minusAlpha;
	//------------------------------------------
	public String toString(){
		return "["+this.getLowerBound()+","+this.getUpperBound()+"]"+" "+minusAlpha;
	}

	public ConfidenceInterval(){
		super();		
		this.minusAlpha = new Alpha(0);
	}
	//------------------------------------------
	public ConfidenceInterval(double a, double b, Alpha alpha) {
		super(a,b);
		this.minusAlpha = alpha;				
	}
	//------------------------------------------
	public Alpha minusAlpha(){
		return minusAlpha;
	}
	//------------------------------------------
	public void setMinusAlpha(double alpha) {
		this.minusAlpha.alphaValue=alpha;
	}
	//------------------------------------------
	@Override
	public int compareTo(ConfidenceInterval o) {
		return this.minusAlpha.compareTo(o.minusAlpha);
	}

	public double width() {
		
		return Math.abs(this.getUpperBound() - this.getLowerBound());
	}
}