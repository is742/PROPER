package confidenceIntervalCalculation;

import java.util.Iterator;

public class AlphaStartEndStep implements Iterator<Alpha>{


	public double startAlpha;
	public double endAlpha;
	public double stepAlpha;

	private double currentAlpha;

	public AlphaStartEndStep(double start,double end,double step){
		
		if (end < start)
			throw new IllegalArgumentException("Cannot have an end point less than start point");
		
		if(step <= 0)
			throw new IllegalArgumentException("Must have a positive step value");
		
		this.startAlpha = start;
		this.endAlpha = end;
		this.stepAlpha = step;
		this.currentAlpha=start;//denotes the current point in the iteration
	}

	@Override
	public boolean hasNext() {
		return (currentAlpha+stepAlpha <= endAlpha+stepAlpha);//inclusive of endpoints.		
		
	}
	@Override
	public Alpha next() {		
		double old = currentAlpha;
		currentAlpha+=stepAlpha;
		return new Alpha(old);
	}
	@Override
	public void remove() {
		// TODO Auto-generated method stub

	}
	
	public String toString(){
		return this.startAlpha+":"+this.endAlpha+":"+this.stepAlpha;
	}

	public double getCurrentAlpha() {
		// TODO Auto-generated method stub
		return this.currentAlpha;
	}

}
