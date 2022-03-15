package parametricModelChecking;

import java.util.ArrayList;
import java.util.HashMap;
import confidenceIntervalCalculation.Interval;
import observations.StateParameterObservation;
import observations.TransitionParameter;

/**
 * ParametricProbabilisticModel is the abstract base class for all Parametric models input into the FACT tool.
 * 
 *  
 * @author kjohnson
 * Version 1.0
 * 
 * */
abstract public class ParametricProbabilisticModel {
	private HashMap<String,Interval> params;
	private String model;
	//------------------------------------------------------------------------------------------
	public ParametricProbabilisticModel(String model){
		this.model=model;
		this.params = new HashMap<String,Interval>();
	}
	//------------------------------------------------------------------------------------------
	public HashMap<String,Interval> getParams(){
		return this.params;
	}
	//------------------------------------------------------------------------------------------
	public void addParameter(String name,Interval interval){
		this.params.put(name, interval);
	}

	//------------------------------------------------------------------------------------------
	public String getModel() {
		return model;
	}
	//------------------------------------------------------------------------------------------
	public void setModel(String model) {
		this.model = model;
	}
	//------------------------------------------------------------------------------------------
	/**
	 * reset the parameters in a model and then update them according
	 * to the parameters listed in the observations, using the default interval [0,1]
	 * */	
	public void setParameters(ArrayList<StateParameterObservation> observations) {
		this.params = new HashMap<String,Interval>();
		for(StateParameterObservation sp : observations){
			for(TransitionParameter tp : sp.getTransitionParameters()){
				addParameter(tp.getLabel(),new Interval(0.0,0.999));
			}
		}


	}
}
