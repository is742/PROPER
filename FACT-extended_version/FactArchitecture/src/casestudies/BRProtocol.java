package casestudies;


import confidenceIntervalCalculation.Interval;
import observations.ObservationCount;
import observations.StateParameterObservation;
import observations.TransitionParameter;


public class BRProtocol extends CaseStudy{

	public BRProtocol(){

		//read in the model and property files 
		super("casestudies/brp/brp_16-2.pm","casestudies/brp/brp.pctl");


		//parameters for parametric model checking
		factInput.model.addParameter("pL1", new Interval(0,1));
		factInput.model.addParameter("pK1",new Interval(0,1));

		//observations for FACT confidence interval calculation
		StateParameterObservation lStateParameter = new StateParameterObservation();

		ObservationCount l1Transitionobs = new ObservationCount();
		l1Transitionobs.transitionObservationCount=990;
		l1Transitionobs.totalObservationCountForStateParameter=1000;
		lStateParameter.addObservation(new TransitionParameter("pL1"), l1Transitionobs);

		StateParameterObservation kStateParameter = new StateParameterObservation();
		
		ObservationCount k1Transitionobs = new ObservationCount();
		k1Transitionobs.transitionObservationCount=500;
		k1Transitionobs.totalObservationCountForStateParameter=1000;
		kStateParameter.addObservation(new TransitionParameter("pK1"), k1Transitionobs);	
		
		//add the observations to the input object and update the confidence interval calculator.
		factInput.observations.add(kStateParameter);
		factInput.observations.add(lStateParameter);
		fact.resetSimultaneousConfidenceIntervalCalculator(factInput);
		
		
	}
}

