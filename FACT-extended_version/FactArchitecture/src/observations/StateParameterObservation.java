package observations;
import java.util.HashMap;

//Observations for a state parameter.
public class StateParameterObservation  {
	public  HashMap<TransitionParameter,ObservationCount> observerations;

	//--------------------------------------------------------------------------------
	public String toString(){
		if(this.observerations.keySet().size()>0){		
			TransitionParameter tp = (TransitionParameter) this.observerations.keySet().toArray()[0];
			return tp.getStateName();
			//return this.observerations.keySet().toArray()[0];
		}
		
		return "Empty Parameter";
	}
	//--------------------------------------------------------------------------------
	public int numberOfParameters(){
		return this.observerations.keySet().size();
	}
	//--------------------------------------------------------------------------------
	public TransitionParameter[] getTransitionParameters(){
		int size = this.observerations.keySet().size();
		TransitionParameter[] params = new TransitionParameter[size];
		int i=0;
		for(TransitionParameter p : this.observerations.keySet()){
			params[i] = p;
			i++;			
		}
		return params;
	}
	//--------------------------------------------------------------------------------
	public boolean hasParameter(String name){
		for(TransitionParameter p : observerations.keySet())
			if(p.label.contentEquals(name))
				return true;			
		return false;
	}
	//--------------------------------------------------------------------------------
	public boolean isEmpty(){
		return this.observerations.isEmpty();
	}
	//--------------------------------------------------------------------------------
	//TODO: Throw an appropriate exception when the parameter already exists
	public boolean addObservation(TransitionParameter tParameter, ObservationCount observationCount){
		if(this.hasParameter(tParameter.label))
			return false;

		this.observerations.put(tParameter, observationCount);
		return true;
	}
	//--------------------------------------------------------------------------------
	public StateParameterObservation(){
		this.observerations = new HashMap<TransitionParameter,ObservationCount>();
	}
	//--------------------------------------------------------------------------------
}