package parametricModelChecking;

import java.io.File;
import inputOutput.FileUtils;
import logging.FACTLogger;

public class PrismParametricModelChecker implements ParametricProbabilisticModelChecker{

	//hard-coded tempfiles to create when invoking prism's CLI
	private static final String tempModelFile  = "modelprism";
	private static final String tempPropertyFile  = "prismtempproperty1";
	private String prismExeFile;
	private OperatingSystem os;

	
	public PrismParametricModelChecker(String executableLocation,OperatingSystem os){
		this.prismExeFile = executableLocation;
		
		String operatingSystem = System.getProperty("os.name");
		if (operatingSystem.toLowerCase().startsWith("win"))
			this.os = OperatingSystem.WINDOWS;
		else
			this.os = OperatingSystem.MAC;
		
	}

	
	private String executeMacPrism(ParametricProbabilisticModel m, Property p, FACTLogger flog){

		File modelFile = FileUtils.writeTemporaryTextFile(tempModelFile, m.getModel());
		File propertyFile = FileUtils.writeTemporaryTextFile(tempPropertyFile, p.getProperty());

		String paramList="";

		for(String parm : m.getParams().keySet()){			
			paramList+=parm+"="+m.getParams().get(parm)+",";
		}

		paramList=paramList.substring(0, paramList.lastIndexOf(','));
		paramList = modelFile.getAbsolutePath()+" "+propertyFile.getAbsolutePath()+" -param "+paramList;		
		String modelCheckerOutput = FileUtils.execute(this.prismExeFile, paramList, flog);
		int leftCurlyBracket = modelCheckerOutput.lastIndexOf('{');//changed oct 6th KJ
		int rightCurlyBracket = modelCheckerOutput.lastIndexOf('}');//changed oct 6th KJ
		if((leftCurlyBracket < rightCurlyBracket)&&(leftCurlyBracket >= 0)&&(rightCurlyBracket >= 0)){
			String poly=
			  modelCheckerOutput.substring(leftCurlyBracket+1, rightCurlyBracket-1);
			return poly;

		}

		return "";

	}

	private String executeWinPrism(ParametricProbabilisticModel m, Property p, FACTLogger flog){

		File modelFile = FileUtils.writeTemporaryTextFile(tempModelFile, m.getModel());
		File propertyFile = FileUtils.writeTemporaryTextFile(tempPropertyFile, p.getProperty());

		String paramList="";

		for(String parm : m.getParams().keySet()){			
			paramList+=parm+"="+m.getParams().get(parm)+",";
		}

		paramList=paramList.substring(0, paramList.lastIndexOf(','));
		paramList = modelFile.getAbsolutePath()+" "+propertyFile.getAbsolutePath()+" -param "+paramList;		
		String modelCheckerOutput = FileUtils.executeWinBatch(this.prismExeFile, paramList, flog);
		int leftCurlyBracket = modelCheckerOutput.lastIndexOf('{');//changed oct 6th KJ
		int rightCurlyBracket = modelCheckerOutput.lastIndexOf('}');//changed oct 6th KJ
		if((leftCurlyBracket < rightCurlyBracket)&&(leftCurlyBracket >= 0)&&(rightCurlyBracket >= 0)){
			String poly=
			  modelCheckerOutput.substring(leftCurlyBracket+1, rightCurlyBracket-1);
			return poly;

		}

		return "";

	}

	@Override
	public AlgebraicExpression modelCheck(ParametricProbabilisticModel m, Property p, FACTLogger flog) {
		if(os == OperatingSystem.MAC){
			String ae = this.executeMacPrism(m,p,flog);
			AlgebraicExpression a = new AlgebraicExpression(ae);
			return a;
		}
		else if (os == OperatingSystem.WINDOWS)// assume Windows as we have not tested Linux yet
		{
			String ae = this.executeWinPrism(m,p,flog);
			AlgebraicExpression a = new AlgebraicExpression(ae);
			return a;
		}
		return null;
	}
	
	
	

}
