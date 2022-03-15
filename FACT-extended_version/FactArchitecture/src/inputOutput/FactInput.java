package inputOutput;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import observations.ObservationCount;
import observations.StateParameterObservation;
import observations.TransitionParameter;
import confidenceIntervalCalculation.AlphaStartEndStep;
import confidenceIntervalCalculation.ConfidenceInterval;
import parametricModelChecking.AlgebraicExpression;
import parametricModelChecking.ParametricProbabilisticModel;
import parametricModelChecking.Property;


public class FactInput {
	public String description;//a text comment of this Fact input
	public AlgebraicExpression algebraicExpression;//store the algebraic expression in the factinput may be null
	public ParametricProbabilisticModel model;	
	public AlphaStartEndStep alphaStartEndStep;
	public Config config;
	public ArrayList<StateParameterObservation> observations;
	public Property property;
	public Time timing;
	
	public ArrayList<ConfidenceInterval> ciResults;	
	//------------------------------------------------------------------------------------------
	
	//------------------------------------------------------------------------------------------
	public boolean hasNextAlpha(){
		return this.alphaStartEndStep.hasNext();
	}
	//------------------------------------------------------------------------------------------
	public FactInput(){
		this.observations = new ArrayList<StateParameterObservation>();
		this.config = new Config();		
		this.model = null;
		this.algebraicExpression = null;	
		this.property = null;	
		this.ciResults = new ArrayList<ConfidenceInterval>();
	}
	//------------------------------------------------------------------------------------------
	public String toString(){
		return "FactInput: "+description;
	}
	//------------------------------------------------------------------------------------------
	/**
	 * This method reads an input file with the original FACT format and creates 
	 * a queue of FactInput objects
	 * */
	public static Queue<FactInput> readFactInputFromOriginalFile(String inputFileLocationAndName){
		Queue<FactInput> factinputs = new LinkedList<FactInput>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(inputFileLocationAndName));
			String line = null;

			while ((line = reader.readLine())!=null){
				FactInput factInput = new FactInput();

				String description="";
				while (line.contains(";")){//handle comments first.
					description+=line;
					line=reader.readLine();
				}

				factInput.description = description;//read the comments for the input

				factInput.algebraicExpression = new AlgebraicExpression(line);//read the algebraic expression after the description

				//parse the values for AlphaStartEndStep of the form 0.90 0.99 0.01
				String[] tokens = reader.readLine().split(" ");
				AlphaStartEndStep  alphas = new AlphaStartEndStep(
						Double.parseDouble(tokens[0]),
						Double.parseDouble(tokens[1]),
						Double.parseDouble(tokens[2]));

				factInput.alphaStartEndStep = alphas;

				//now, read the state parameters, of the form, e.g. p 2 10000 
				while(((line=reader.readLine())!=null)&&(!line.contains("END"))){	
					
						tokens = line.split(" ");
						String parameterName = tokens[0];
						int numberOfParameters = Integer.parseInt(tokens[1]);
						int totalNumberOfObservations = Integer.parseInt(tokens[2]);
	
						StateParameterObservation stateParameterObservation = new StateParameterObservation();
	
						for(int i=1;i<=numberOfParameters;i++){
							ObservationCount oc = new ObservationCount();
							oc.totalObservationCountForStateParameter = totalNumberOfObservations;
							oc.transitionObservationCount = Integer.parseInt(reader.readLine());
							stateParameterObservation.addObservation(new TransitionParameter(parameterName+i),oc);
						}
						
						factInput.observations.add(stateParameterObservation);
				}

				//add this fact input to the queue of fact inputs
				factinputs.add(factInput);
			}
			reader.close();
		}catch (IOException e) {			
			e.printStackTrace();
		}
		return factinputs;
	}

	//we are creating new observations, so remove the old ones.
	public void resetObservations() {
		this.observations = new ArrayList<StateParameterObservation>();
	}
	
	public void modelReset() {
		this.model = null;		
	}
}