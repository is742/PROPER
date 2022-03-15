package confidenceIntervalCalculation;

import java.util.ArrayList;
import java.util.HashMap;

import observations.StateParameterObservation;
import observations.TransitionParameter;



public interface SimultaneousConfidenceIntervalCalculator {
	ConfidenceInterval computeConfidenceInterval(Alpha confidence,double parameter, double observations);
	void initialiseParameterConfidenceIntervals(Alpha alpha);
	
	public ArrayList<StateParameterObservation> stateParameterObservations();
	public ArrayList<TransitionParameter> getTransitionParameters();
	
	public ConfidenceInterval getConfidenceInterval(TransitionParameter tp);
	
	public void updateSimultaneousConfidenceIntervals(Alpha alpha);
	//public void updateAlpha(TransitionParameter tParameter,Alpha newAlpha);
	
}
