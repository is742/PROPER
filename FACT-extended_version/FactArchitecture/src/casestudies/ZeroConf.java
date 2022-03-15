package casestudies;

import confidenceIntervalCalculation.Interval;
import observations.ObservationCount;
import observations.StateParameterObservation;
import observations.TransitionParameter;

public class ZeroConf extends CaseStudy{

	public ZeroConf() {
		super("casestudies/zeroconf/zeroconf-10.pm","casestudies/zeroconf/zeroconf.pctl");


		//parameters for parametric model checking
		factInput.model.addParameter("p1", new Interval(0,1));
		factInput.model.addParameter("q1",new Interval(0,1));

		//observations for FACT confidence interval calculation
		//observations for FACT confidence interval calculation
		StateParameterObservation pStateParameter = new StateParameterObservation();

		ObservationCount p1Transitionobs = new ObservationCount();
		p1Transitionobs.transitionObservationCount=290;
		p1Transitionobs.totalObservationCountForStateParameter=1000;
		pStateParameter.addObservation(new TransitionParameter("p1"), p1Transitionobs);

		StateParameterObservation qStateParameter = new StateParameterObservation();
		ObservationCount q1Transitionobs = new ObservationCount();
		q1Transitionobs.transitionObservationCount=500;
		q1Transitionobs.totalObservationCountForStateParameter=1000;
		qStateParameter.addObservation(new TransitionParameter("q1"), q1Transitionobs);	

		//add the observations to the input object and update the confidence interval calculator.
		factInput.observations.add(qStateParameter);
		factInput.observations.add(pStateParameter);
		fact.resetSimultaneousConfidenceIntervalCalculator(factInput);

	}

}
