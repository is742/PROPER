package mathEngine;

public class MinMaxResult extends MathsResult{

	private double min;
	private double max;


	public double width(){

		return Math.abs((this.getMax() - this.getMin()));

	}

	public MinMaxResult(double min,double max){

		if (min <0) min=Math.abs(min);
		if (max <0) max=Math.abs(max);		

		if(min>max){
			this.min = max;
			this.max = min;
		} else{
			this.min = min;
			this.max = max;
		}
	}


	public MinMaxResult(){
		this.min = Double.MAX_VALUE;
		this.max = Double.MIN_VALUE;
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}
	//------------------------------------------------------------------
	public void setMax(double max) {
		this.max = max;
	}
	//------------------------------------------------------------------
	public String toString(){
		return "min: "+this.getMin()+" max: "+this.getMax();
	}

	//------------------------------------------------------------------
	@Override
	public int compareTo(MathsResult o) {
		Double  thisWidth = this.width();
		Double oWidth = ((MinMaxResult)o).width(); 		
		return thisWidth.compareTo(oWidth); 
	}
	//------------------------------------------------------------------
}
