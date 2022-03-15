package confidenceIntervalCalculation;
/**
 * 
 * Represents a closed interval.
 * */
public class Interval {
	private double a;
	private double b;
	//------------------------------------------
	public String toString(){
		return this.getLowerBound()+":"+this.getUpperBound();
	}
	//------------------------------------------
	public static Interval unitInterval(){
		Interval i = new Interval();
		i.setLowerBound(0.0);
		i.setUpperBound(1.0);
		return i;
	}
	//------------------------------------------
	public Interval(){
		this(0.0,0.0);
	}
	//------------------------------------------
	public Interval(double a,double b){
		this.setLowerBound(a);
		this.setUpperBound(b);
	}
	//------------------------------------------
	public void setLowerBound(double a){
		this.a=a;
	}
	//------------------------------------------
	public void setUpperBound(double b){
		this.b=b;
	}
	//------------------------------------------
	public double getLowerBound(){
		return a;
	}
	//------------------------------------------
	public double getUpperBound(){
		return b;
	}
	//------------------------------------------
}
