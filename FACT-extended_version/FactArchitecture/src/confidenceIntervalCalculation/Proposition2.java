package confidenceIntervalCalculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import observations.StateParameterObservation;
import observations.TransitionParameter;
//--------------------------------------------------------------------------------------------------------
public class Proposition2 implements SimultaneousConfidenceIntervalCalculator{

	private int nStateParameters;
	private HashMap<TransitionParameter,ConfidenceInterval>  parameterCIs;
	private ArrayList<StateParameterObservation> observations;

	//--------------------------------------------------------------------------------------------------------
	public Proposition2(ArrayList<StateParameterObservation> observations) {
		this.parameterCIs = new  HashMap<TransitionParameter,ConfidenceInterval>();		
		this.observations = observations;

		ArrayList<TransitionParameter> tps = new ArrayList<TransitionParameter>();
		for(StateParameterObservation spo : observations)
			for(TransitionParameter tp : spo.getTransitionParameters()){
				parameterCIs.put(tp,  new ConfidenceInterval());
				tps.add(tp);
			}



		//need to count parameters. OCT 15th
		//this.nStateParameters = this.nStateParameters(tps);


		 this.nStateParameters = this.observations.size();

	}
	//--------------------------------------------------------------------------------------------------------
	@Override
	public ConfidenceInterval computeConfidenceInterval(Alpha confidence, double parameter,double observations) {

		//System.out.println("\nconfidence, parameter, observations: "+confidence+" "+parameter+" "+observations);
		double oneMinusAlpha = 1 - confidence.alphaValue;

		// the quantile will be = 1-(alpha/2)
		double zscore= StatisticsConversion.calculateOneSidedZscore(1.0 - (oneMinusAlpha / 2.0));

		double firstpart = parameter + (zscore*zscore)/(2.0*observations);
		double secondtpart = (zscore*zscore)*Math.sqrt( ( parameter*(1.0-parameter) + (zscore*zscore)/(4.0*observations))/observations );
		double denom = 1.0+(zscore*zscore)/observations;

		double lowerBound = (firstpart - secondtpart)/denom;
		double upperBound = (firstpart + secondtpart)/denom;

		if (lowerBound<0) lowerBound = 0;
		if (upperBound>1) upperBound = 1;

		
		
		return new ConfidenceInterval(lowerBound,upperBound,confidence);
	}
	//---------------------------------------------------------------------------------------------------
	
	public ConfidenceInterval computeConfidenceInterval2(Alpha confidence, double mean,double stddev, int size) {
		
		double oneMinusAlpha = 1 - confidence.alphaValue;

		// the quantile will be = 1-(alpha/2)
		double zscore= StatisticsConversion.calculateOneSidedZscore(1.0 - (oneMinusAlpha / 2.0));

		double lowerBound = mean - zscore * (stddev / Math.sqrt(size));
        double upperBound = mean + zscore * (stddev / Math.sqrt(size));
			
		return new ConfidenceInterval(lowerBound,upperBound,confidence);
	}
	//---------------------------------------------------------------------------------------------------

	@Override
	public void initialiseParameterConfidenceIntervals(Alpha alpha) {

		for(TransitionParameter tp : this.parameterCIs.keySet()){
			ConfidenceInterval ci = new ConfidenceInterval();

			//FIXED OCT 15th
			//we still need the number of state parameters here??? x: 0.9680187850024814
			/*
			 * sp.oneMinusAlpha = Math.pow(oneMinusBeta, 1.0/e.sps.size());//sps.size = 5,3,4,3,3 according to number of state parameters in req.
							//x: 0.85 0.9680187850024814
			 * System.out.println(sp.name+": "+oneMinusBeta+" "+sp.oneMinusAlpha);
			 * x: (beta,alpha,size) 0.85 0.9680187850024814 5
			 * */



			//System.out.println(obtainStateParameterList(tp).size());
			ci.setMinusAlpha(Math.pow(alpha.alphaValue, 1.0/this.nStateParameters));
			//System.out.println("(beta,alpha,size)"+alpha.alphaValue+" "+ci.minusAlpha+" "+this.nStateParameters);

			this.parameterCIs.put(tp, ci);
		}
	}
	//---------------------------------------------------------------------------------------------------
	@Override
	public ArrayList<StateParameterObservation> stateParameterObservations() {
		return this.observations;		
	}
	//---------------------------------------------------------------------------------------------------
	@Override
	public ArrayList<TransitionParameter> getTransitionParameters() {
		ArrayList<TransitionParameter> gtps = new ArrayList<TransitionParameter>();
		for(TransitionParameter tp : this.parameterCIs.keySet()){
			gtps.add(tp);
		}

		Collections.sort(gtps);

		return gtps;
	}
	//---------------------------------------------------------------------------------------------------
	@Override
	public ConfidenceInterval getConfidenceInterval(TransitionParameter tp) {
		return this.parameterCIs.get(tp);
	}
	//---------------------------------------------------------------------------------------------------
	@Override
	public void updateSimultaneousConfidenceIntervals(Alpha alpha) {

		for(StateParameterObservation obs : observations){
			for(TransitionParameter tp : obs.getTransitionParameters()){
				//System.out.println(tp);

				double totalStateParameterObservationCount = obs.observerations.get(tp).totalObservationCountForStateParameter;
				double transitionObservationCount = obs.observerations.get(tp).transitionObservationCount;

				double observationRatio = ((double)transitionObservationCount/totalStateParameterObservationCount);				

				ConfidenceInterval ci = this.parameterCIs.get(tp);
				if (!tp.toString().startsWith("n")) {
					ci = this.computeConfidenceInterval(ci.minusAlpha,observationRatio,totalStateParameterObservationCount);
				}else {
					ci = this.computeConfidenceInterval2(ci.minusAlpha,transitionObservationCount,totalStateParameterObservationCount,obs.observerations.get(tp).n_size);
				}
				System.out.println("%interval:"+tp.getLabel()+" "+ci.getLowerBound()+","+ci.getUpperBound());
				System.out.println("%width:"+tp.getLabel()+" "+ci.width());
				this.parameterCIs.put(tp, ci);
				
				

			}

		}
	}


}