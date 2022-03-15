package casestudies;

import mathEngine.MatLabEngine;

public class TestCaseStudies {

	public static void main(String args[]){
		CaseStudy caseStudy=null;

		//caseStudy = new webapp();
		//caseStudy = new Nand();
		//caseStudy = new ZeroConf();
		//caseStudy = new Crowds();

		caseStudy.modelCheck();
		System.out.println("Polynomial sent to Matlab:");
		System.out.println(caseStudy.factInput.algebraicExpression);
		
		//caseStudy.run();
		
		caseStudy.fact.setMathsEngine(new MatLabEngine());

		while(caseStudy.factInput.hasNextAlpha()){
			caseStudy.fact.nextStepCalculateConfidenceInterval(caseStudy.factInput);	
			System.out.println(caseStudy.fact.getLastComputedCI());		
		}
		caseStudy.fact.terminate();


	}

}