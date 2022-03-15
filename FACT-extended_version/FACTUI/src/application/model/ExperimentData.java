
package application.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import application.view.MainViewController;
import confidenceIntervalCalculation.AlphaStartEndStep;
import confidenceIntervalCalculation.Interval;
import framework.FactFramework;
import inputOutput.FactInput;
import inputOutput.Time;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import logging.FACTLogger;
import observations.ObservationCount;
import observations.StateParameterObservation;
import observations.TransitionParameter;
import parametricModelChecking.PrismDTMCModel;
import parametricModelChecking.PrismPCTLProperty;


/**
 * This class manages the experimental data on disk and stored properties that 
 * are edited through the view controller class MainViewController.
 * <p/>
 * The class allows Models, Properties and Observation data to be loaded
 * individually from files on disk.
 * <p/>
 * A status string is used to report the success or failure of actions within
 * the class. All data is private and accessed through getter and setter 
 * functions.
 * 
 * @author Colin Paterson
 */
public class ExperimentData {
	public static MainViewController mVC;

	public Time timer;

	public Float cnfLower;
	public Float cnfUpper;
	public Float cnfStep;

	public Float graphYMax = 1.0f;
	public Float graphYMin = 0.0f;
	public Float graphXMin = 0.9f;
	public Float graphXMax = 1.0f;

	public Boolean running = false;
	public Boolean resultsUnsaved = false;

	public FACTLogger flog = new FACTLogger();

	public Float[] shareArray = new Float[10];
	public Integer shareCounter = 0;

	public ArrayList<Observation> observations = new ArrayList<Observation>();
	
	public Map<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
	public int countN=0;
	public Map<String, LoopInfo> link = new HashMap<String, LoopInfo>();
		
	public FactFramework fact = new FactFramework();

	private String lastError =""; 

	/** The text containing the code for the model.*/
	private String modelCode = ""; 
	private String parsedModel = "";

	/** The text containing the properties against which the model is checked.*/
	private String propCode = "";
	
	public int n_entries=0;

	private String evalProp = "";
	public String getEvalProp() {
		return evalProp;
	}

	public void setEvalProp(String evalProp) {
		this.evalProp = evalProp;
	}

	public String getLastError(){
		String s = lastError;
		lastError = "";
		return s;
	}

	public void setLastError(String s){
		lastError = s;
	}

	private String evalObsFile = "";
	public String getEvalObsFile() {
		return evalObsFile;
	}

	public void setEvalObsFile(String evalObsFile) {
		this.evalObsFile = evalObsFile;
	}

	private Integer confidenceValue = 0;
	public Integer getConfidenceValue() {
		return confidenceValue;
	}
	public void setconfidenceValue(Float l, Float u, Float s) {
		this.cnfLower = l;
		this.cnfUpper = u;
		this.cnfStep = s;
	}


	public XYChart.Series<Number, Number> chartSeries0 = new XYChart.Series<>();
	public XYChart.Series<Number, Number> chartSeries1 = new XYChart.Series<>();


	private ObservableList<ModelProperty> propertyData = FXCollections.observableArrayList();


	/** The status of the last operation undertaken by the class.*/
	private String status = "";

	public SimpleStringProperty output = new SimpleStringProperty();

	public FactInput fInput = new FactInput();
	public static String PRISMLOCATION = "/Users/ColinPaterson/Documents/prism-4.2.beta1-osx64/bin/prism";



	/**
	 * Class instantiation initialises the code blocks with user instructions.
	 */
	public ExperimentData(){
		modelCode = "Initialise the model in here or load one.";
		propCode = "";

	}

	/** Retrieve the model code */
	public String getModelCode(){
		return modelCode;
	}

	/** Retrieve the properties code */
	public ObservableList<ModelProperty> getPropertyData(){
		return propertyData;
	}


	/** Retrieve the class status */
	public String getStatus(){
		return status;
	}

	/** Set the value of the model code, allows the user to edit a model. */
	public void setModel(String text) {
		modelCode = text;
	}

	/** Set the value of the properties code, allows the user to edit a properties file. */
	public void setPropCode(String text) {
		propCode = text;
	}


	/**
	 * Load a model file from disk.
	 * @param file The file handle as returned from a file open dialog.
	 */
	public void loadModel(File file){
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null){
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			modelCode = sb.toString(); 
		} catch (IOException e) {
			status = "Model loading failed";
			e.printStackTrace();
		} 

		status = "Model loaded";
	}

	/**
	 * Load a properties file from disk.
	 * @param file The file handle as returned from a file open dialog.
	 */
	public void loadProps(File file){
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			propertyData.clear();
			while (line != null){
				if (line.trim().length() > 0){
					ModelProperty mp = new ModelProperty(line);
					propertyData.add(mp);
				}
				line = br.readLine();
			}
			propCode = sb.toString(); 
		} catch (IOException e) {
			status = "Property loading failed";
			e.printStackTrace();
		} 
		status = "Properties loaded";
	}






	/**
	 * Save an edited model file to disk.
	 * @param file The file handle as returned from a file save dialog.
	 */
	public void saveModel(File file){

		saveFile(file,"Saving Model file", modelCode);

	}

	/**
	 * Save an edited properties file to disk.
	 * @param file The file handle as returned from a file save dialog.
	 */
	public void saveProps(File file){

		propCode = "";
		for (int i=0; i<propertyData.size(); i++){
			String s = propertyData.get(i).detailProperty().getValue();
			if (s.length() > 0)
				propCode += s +"\n";
		}

		saveFile(file,"Saving Property file",propCode);
	}


	private void saveFile(File file, String sMessage, String data){
		try {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.close();

			status = sMessage +" successful.";
		} catch (IOException e) {
			status = sMessage +" failed.";
			e.printStackTrace();
		}		
	}

	/**
	 * This function saves the complete experiment. A line of !--**-- indicates
	 * the end of each section of the model. Model files are given the extension
	 * fexp.
	 * <p/>
	 * information is stored in the order 1. Model, 2. Properties, 3. Observations.
	 * @param file The file ahndle where the experiment will be saved.
	 */
	public void saveExperiment(File file){
		try {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("!--**--\n");
			bw.write(modelCode+"\n");
			bw.write("!--**--\n");
			for (int i=0; i<propertyData.size(); i++){
				String sProp = propertyData.get(i).getDetail();
				bw.write(sProp+"\n");
			}
			bw.write("!--**--\n");
			bw.write(evalObsFile+"\n");
			bw.write("!--**--\n");
			bw.write(confidenceValue.toString()+"\n");
			bw.close();

			status = "Experiment saved.";
		} catch (IOException e) {
			status = "Saving experiment failed.";
			e.printStackTrace();
		}				
	}

	public void loadExperiment(File file){

		int mode = 0;
		propertyData.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(file))){
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null){
				if (line.equals("!--**--")){
					if (mode == 0){ // then starting model
						sb = new StringBuilder();
						mode++;
					} else if (mode == 1){
						modelCode = sb.toString();
						sb = new StringBuilder();
						mode++;
					} else if (mode == 2){
						propCode = sb.toString();
						sb = new StringBuilder();
						mode++;
					} else if (mode == 3){
						evalObsFile = sb.toString();
						sb = new StringBuilder();
						mode++;	
					} else if (mode == 4){
						confidenceValue = Integer.parseInt(sb.toString());
						sb = new StringBuilder();
						mode++;
					}
				} else{
					sb.append(line);
					sb.append(System.lineSeparator());
					if (mode == 2)	{
						ModelProperty mp = new ModelProperty(line);
						propertyData.add(mp);
					}


				}
				line = br.readLine();
			}
		} catch (IOException e) {
			status = "Experiment data loading failed";
			e.printStackTrace();
		} 



		status = "Experiment data loaded";


	}

	@SuppressWarnings("unchecked")
	public void clearChartData(){

		chartSeries0.getData().clear();
		chartSeries1.getData().clear();


		NumberAxis yAxis = (NumberAxis) ExperimentData.mVC.chtOutput.getYAxis();
		yAxis.setAutoRanging(false);
		yAxis.setUpperBound(1);
		yAxis.setLowerBound(0);

	}

	// Series can be 0 or 1, to indicate which series the data is being added to.
	public void addChartData(double x, double y, int series){
		if (series == 0)
			chartSeries0.getData().add(new XYChart.Data<>(x, y));
		else
			chartSeries1.getData().add(new XYChart.Data<>(x, y));
	}
	
	// This function reads the model and parses the comments.
	// where a comment is of the form
	// param double name = num num num ...;
	// then a parameter object is created with the upper and lower bounds
	public String parseParams(){

		// This is the new model with the parameters parsed out
		String newModel = "";
		InputStream is = new ByteArrayInputStream(modelCode.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		observations.clear();


		String line;
		try {
			while ((line = br.readLine()) != null){
				String testParam = "^\\s*param\\s+\\w+\\s+\\w+\\s*=(\\s+\\d+){1,};";
				if (line.matches(testParam)) {
					Observation obs = new Observation();
					if (!obs.initialise(line)){
						flog.add("Observation: Initialise failed - Syntax issue?");
						return "";
					}
					observations.add(obs);

					for (int i=0; i<obs.getSize(); i++){
						String newLine = "const "+obs.type+" "+obs.name+(i+1)+";";
						newModel += newLine+"\n";
					}
				}
				else
					newModel = newModel+line+"\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newModel;
	}
	
	public String parseParamsPROPER(){

		// This is the new model with the parameters parsed out
		String newModel = "";
		InputStream is = new ByteArrayInputStream(modelCode.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		observations.clear();


		String line;
		try {
			while ((line = br.readLine()) != null){
				String testParam = "^\\s*param\\s+\\w+\\s+\\w+\\s*=(\\s+\\d+){1,};";
				String testParamLoopData = "^\\s*param\\s+\\w+\\s+\\w+\\s*=\\s+\"(\\w+|\\.)(/\\w+)+\\.\\w+\";";
				if (line.matches(testParam)) {
					Observation obs = new Observation();
					if (!obs.initialise(line)){
						flog.add("Observation: Initialise failed - Syntax issue?");
						return "";
					}
					observations.add(obs);

					for (int i=0; i<obs.getSize(); i++){
						String newLine = "const "+obs.type+" "+obs.name+(i+1)+";";
						newModel += newLine+"\n";
					}
				}
				else if (line.matches(testParamLoopData)) {
					countN++;
					ArrayList<Integer> data = new ArrayList<Integer>();
					String fileName = line.substring(line.indexOf("=")+1).trim().replace("\"", "").replace(";","");
					try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
					    String bufferLine;
					    while ((bufferLine = reader.readLine()) != null) {
					        data.add(Integer.parseInt(bufferLine));
					    }
					}
					Collections.sort(data);
					map.put("n"+countN, data);
					//String newLine = "const double z = "+processDataFile(data)+";";
					//newModel += newLine+"\n";
					Observation obs = new Observation();
					String currentName="n"+countN;
					line = "param double "+currentName+" = "+calculateMean(currentName)+" "+calculateStdDev(currentName)+";";
					if (!obs.initialise(line)){
						flog.add("Observation: Initialise failed - Syntax issue?");
						return "";
					}
					observations.add(obs);

					for (int i=0; i<obs.getSize(); i++){
						String newLine = "const double "+obs.name+(i+1)+";";
						newModel += newLine+"\n";
					}
				}
				else
					newModel = newModel+line+"\n";
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return newModel;
	}
	
	public double calculateMean(String name) {
		ArrayList<Integer> numbers = new ArrayList<Integer>();
		for (String key : map.keySet()) {
		    if (key.equals(name)) {
		    	numbers = new ArrayList<Integer>(map.get(key));
		    }
		}
		int sum = 0;
	    int n = numbers.size();
	    n_entries=n;
	    //compute mean
	    for (int x : numbers) {
	        sum += x;
	    }
	    double mean = (double) sum / n;
	    
	    //compute variance
	    double variance = 0;
	    for (int i = 0; i < n; i++) {
            variance += (numbers.get(i) - mean) * (numbers.get(i) - mean);
        }
	    variance = variance / (n - 1);
	    
	    //compute stddev
        double stddev = Math.sqrt(variance);
        
        //remove outliers if any
        double cut_off = stddev*4;
        double lo_cut_off = mean - cut_off;
        double hi_cut_off = mean + cut_off;
        
        ArrayList<Integer> outliers = new ArrayList<Integer>();
        for (int i=0; i < n; i++) {
	        if (numbers.get(i) < lo_cut_off || numbers.get(i) > hi_cut_off) {
	        	outliers.add(i);
	        }
	    }

        Collections.sort(outliers, Collections.reverseOrder());
        for (int i : outliers)
        	numbers.remove(i);
  
        //re-compute mean, variance, stddev
        n=numbers.size();
        sum=0;
	    for (int x : numbers) {
	        sum += x;
	    }
	    mean = (double) sum / n;
	    
	    variance = 0;
	    for (int i = 0; i < n; i++) {
            variance += (numbers.get(i) - mean) * (numbers.get(i) - mean);
        }
	    variance = variance / (n - 1);
	    
	    stddev = Math.sqrt(variance);
	    
	    LoopInfo li = new LoopInfo(mean,stddev);
	    link.put(name,li);

        return mean;
	}
	
	public double calculateStdDev(String name) {
		double stddev=0;
		for (String key : link.keySet()) {
		    if (key.equals(name)) {
		    	LoopInfo li = link.get(key);
		    	stddev = li.getStddev();
		    }
		}
		return stddev;
	}

	public void populateFACT(){
		// Extract the parameter observations from the model comments
		parsedModel = parseParamsPROPER();

		fInput.resetObservations();
		fInput.modelReset();

		if (parsedModel == ""){
			mVC.setStatus("Unable to parse Model parameters");
		}

		fInput.model = new PrismDTMCModel();	
		fInput.model.setModel(parsedModel);


		fInput.property = new PrismPCTLProperty();		
		fInput.property.setProperty(evalProp);
		flog.add("Property to evaluate: " +evalProp);

		double lower = cnfLower/100.0d;
		double upper = cnfUpper/100.0d;
		double step =  cnfStep/100.0d;

		flog.add("Confidence: " + lower + ", "+ upper + ", "+ step);

		fInput.alphaStartEndStep = new AlphaStartEndStep(lower,upper,step);	

		for (Observation o:observations){

			/**
			 * 	for (int i=0; i< o.getSize(); i++){
				StateParameterObservation stateParameter = new StateParameterObservation();
				ObservationCount oCount = new ObservationCount();
				fInput.model.addParameter(o.name+(i+1), new Interval(0,1));
				oCount.transitionObservationCount = o.getParamByIndex(i);
				oCount.totalObservationCountForStateParameter = o.getTotal();
				stateParameter.addObservation(new TransitionParameter(o.name+(i+1)), oCount);
				flog.add("Adding Observation: "+o.name+(i+1)+", "+oCount.toString());
				fInput.observations.add(stateParameter);
			}

			 * */

			StateParameterObservation stateParameter = new StateParameterObservation();
			// cf. BRProtocol - instantiation.
			for (int i=0; i< o.getSize(); i++){				
				ObservationCount oCount = new ObservationCount();
				fInput.model.addParameter(o.name+(i+1), new Interval(0,1));
				if(!o.name.startsWith("n")) {
					oCount.transitionObservationCount = o.getParamByIndex(i);
					oCount.totalObservationCountForStateParameter = o.getTotal();
				}
				else {
					oCount.transitionObservationCount = o.getParamByIndex(0);
					oCount.totalObservationCountForStateParameter = o.getParamByIndex(1);//o.getTotal()-o.getParamByIndex(i);
					oCount.n_size = n_entries;
				}
				stateParameter.addObservation(new TransitionParameter(o.name+(i+1)), oCount);
				flog.add("Adding Observation: "+o.name+(i+1)+", "+oCount.toString());

			}
			fInput.observations.add(stateParameter);


		}
	}

	public void exportResults(File file){
		try {
			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			for (int i=0; i<fInput.ciResults.size(); i++){
				bw.write(fInput.ciResults.get(i).minusAlpha.alphaValue+","
						+fInput.ciResults.get(i).getLowerBound()+", "
						+fInput.ciResults.get(i).getUpperBound()+"\n");
			}


			bw.close();

			status = "Results saved.";
		} catch (IOException e) {
			status = "Saving results failed.";
			e.printStackTrace();
		}			
	}


	public void resetchartData(){
		chartSeries0.getData().clear();
		chartSeries1.getData().clear();
	}

}

