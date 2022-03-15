package application.model;

import application.view.MainViewController;
import casestudies.BRProtocol;
import casestudies.CaseStudy;
import confidenceIntervalCalculation.ConfidenceInterval;
import framework.FactFramework;
import inputOutput.Time;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mathEngine.MatLabEngine;
import parametricModelChecking.OperatingSystem;
import parametricModelChecking.PrismParametricModelChecker;

public class FactExecution {
	
	public ExperimentData data = null;
	
    private Service<Void> backgroundThread;
    private Service<Void> builderThread;
    
    public Label lbStatus;
    
	static float minCI = Float.MAX_VALUE;
	static float maxCI= -Float.MIN_VALUE;

	public FactExecution(ExperimentData _data){	
		data = _data;
	}
	
	public  double roundToSignificantFigures(double num, int n, boolean rdown) {
	    if(num == 0) {
	        return 0;
	    }

	    final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
	    final int power = n - (int) d;

	    final double magnitude = Math.pow(10, power);
	    final long shifted;
	    if ( rdown)
	    	shifted = Math.round((num*magnitude)-0.5);
	    else
	    	shifted = Math.round(num*magnitude);
	    return shifted/magnitude;
	}
	
	public void run(){
		data.flog.newLog();
		data.flog.add("Run called");
		data.running = true;
		minCI = Float.MAX_VALUE;
		maxCI= Float.MIN_VALUE;

		data.timer = new Time();
		
		FactFramework fact = data.fact;
		Settings settings = new Settings();
		settings.read();
		
    	if (settings.get("PRISMLocation") != null){
    		ExperimentData.PRISMLOCATION = settings.get("PRISMLocation");
    	}
		
		fact.setPpmc(new PrismParametricModelChecker(ExperimentData.PRISMLOCATION,OperatingSystem.MAC));	

		// Initialise the case study: caseStudy = new BRProtocol();
		data.populateFACT();
		
		builderThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				
				return new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						data.fInput.ciResults.clear();
						fact.resetSimultaneousConfidenceIntervalCalculator(data.fInput);
						
						// This is the code from the casestudy modelCheck function : caseStudy.modelCheck();
						data.flog.add("Prism is attempting parametric model checking");
						data.fInput.algebraicExpression = fact.getPpmc().modelCheck(data.fInput.model, data.fInput.property, data.flog);	
					return null;
					}
				};
			}
		}; // End of backgroundThread
		
		
		backgroundThread = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws Exception {
						fact.nextStepCalculateConfidenceInterval(data.fInput);	
						return null;
					}
				};
			}
		}; // End of backgroundThread
		
		
		
		backgroundThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				
				ConfidenceInterval ci = fact.getLastComputedCI();
				data.flog.add(fact.getLastComputedCI().toString());		
				
				double valueL = ci.getLowerBound();
				double valueU = ci.getUpperBound();
				double alpha =  ci.minusAlpha().alphaValue;
				data.flog.add("Time to calculate: "+data.fInput.timing.toString()+"s");
				
				
				
	            data.addChartData(alpha, valueU, 0);
	            data.addChartData(alpha, valueL, 1);
	            
	            float lB = ((float)Math.round((valueL*100)-0.5))/100.0f;
	            float uB = ((float)Math.round((valueU*100)+0.5))/100.0f;
	            
	            float margin = (float) roundToSignificantFigures((uB-lB)/10,1,false);
	            
	            if ((float)lB < minCI){ minCI = lB;}
	            if ((float)uB > maxCI){ maxCI = uB;}
	            
	            // This is how I can change the graph
	            NumberAxis yAxis = (NumberAxis) ExperimentData.mVC.chtOutput.getYAxis();
	            yAxis.setAutoRanging(false);
	            
	            double axisMax = roundToSignificantFigures(maxCI+margin,3,false);
	            double axisMin = roundToSignificantFigures(minCI-margin,3,true);

	            if (valueL >= 0){
	            	if (valueU < 0.05){
			            yAxis.setUpperBound(0.05);
	            	}
	            	if (valueU < 0.04){
			            yAxis.setUpperBound(0.04);
	            	}
	            	if (valueU < 0.03){
			            yAxis.setUpperBound(0.03);
	            	}
	            	if (valueU < 0.02){
			            yAxis.setUpperBound(0.02);
	            	}
	            	if (valueU < 0.01){
			            yAxis.setUpperBound(0.01);
	            	}
		            yAxis.setLowerBound(0);
		            yAxis.setTickUnit(0.01);
	            } 
	            
	            if (valueU >= 0.05) {
		            yAxis.setUpperBound(axisMax);
		            yAxis.setLowerBound(axisMin);
		            
		            double dif = (axisMax - axisMin);
		            yAxis.setTickUnit(dif/5);
	            }
	    		if (data.fInput.hasNextAlpha()){
	    			backgroundThread.restart();
	    		} else {
	    			data.running = false;
	    			data.resultsUnsaved = true;
					data.flog.add("-----------------------------------------------------");
					data.flog.add("Run complete");
					data.flog.close();
					lbStatus.setText("Evaluation Complete");
	    		}
				
			}
		}); // End of OnSuccess

		
		builderThread.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				
				data.timer.stop();
				String sPoly = data.fInput.algebraicExpression.poly;
				if (sPoly.length() == 0){
					data.flog.add("Prism failed to return a polynomial");
					lbStatus.setText("Error: Model checking failed to return a polynomial");
					data.running = false;
					return;
				} else
					lbStatus.setText("Model checking complete");
				
				
				data.flog.add("-----------------------------------------------------");
				data.flog.add("Polynomial sent to Matlab:");
				data.flog.add(sPoly);
				data.flog.add("Calculated in " + data.timer + " seconds");
				data.flog.add("-----------------------------------------------------");
	      	  	data.flog.add("Hill Climbing Max Iterations: "+data.fInput.config.MAX_ITERATIONS);

				fact.setMathsEngine(new MatLabEngine());			
				if (data.fInput.hasNextAlpha()){
					backgroundThread.restart();
				}
				
			}
		});

		builderThread.restart();

	}
}
