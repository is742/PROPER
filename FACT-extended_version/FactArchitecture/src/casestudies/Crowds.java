package casestudies;

import confidenceIntervalCalculation.Interval;
import observations.ObservationCount;
import observations.StateParameterObservation;
import observations.TransitionParameter;

public class Crowds extends CaseStudy{

	public Crowds() {
		super("casestudies/crowds/crowds_3-5.pm","casestudies/crowds/crowds.pctl");


		//parameters for parametric model checking
		factInput.model.addParameter("PF1", new Interval(0,1));
		factInput.model.addParameter("badC1",new Interval(0,1));

		//observations for FACT confidence interval calculation
		//observations for FACT confidence interval calculation
		StateParameterObservation pFStateParameter = new StateParameterObservation();

		ObservationCount pF1Transitionobs = new ObservationCount();
		pF1Transitionobs.transitionObservationCount=990;
		pF1Transitionobs.totalObservationCountForStateParameter=1000;
		pFStateParameter.addObservation(new TransitionParameter("PF1"), pF1Transitionobs);

		StateParameterObservation badCStateParameter = new StateParameterObservation();
		ObservationCount badC1Transitionobs = new ObservationCount();
		badC1Transitionobs.transitionObservationCount=500;
		badC1Transitionobs.totalObservationCountForStateParameter=1000;
		badCStateParameter.addObservation(new TransitionParameter("badC1"), badC1Transitionobs);	

		//add the observations to the input object and update the confidence interval calculator.
		factInput.observations.add(badCStateParameter);
		factInput.observations.add(pFStateParameter);
		fact.resetSimultaneousConfidenceIntervalCalculator(factInput);

	}

}
