package mathEngine;

import java.util.ArrayList;

import confidenceIntervalCalculation.ConfidenceInterval;
import confidenceIntervalCalculation.SimultaneousConfidenceIntervalCalculator;
import observations.*;
import matlabcontrol.MatlabConnectionException;
import matlabcontrol.MatlabInvocationException;
import matlabcontrol.MatlabProxy;
import matlabcontrol.MatlabProxyFactory;
import matlabcontrol.MatlabProxyFactoryOptions;
import parametricModelChecking.AlgebraicExpression;

public class MatLabEngine implements MathsEngine{

	static private MatlabProxyFactory matLabFactory;
	static private MatlabProxy proxy;	
	//------------------------------------------------------------------------------------------------
	public MatLabEngine(){			
		if(matLabFactory==null){
			try {	 

				MatlabProxyFactoryOptions options = new MatlabProxyFactoryOptions.Builder()
						.setHidden(true)
						.setProxyTimeout(30000L)
						.setUsePreviouslyControlledSession(true)
						.build();
				matLabFactory = new MatlabProxyFactory(options);				
				try {

					proxy = matLabFactory.getProxy();

				} catch (MatlabConnectionException e1) {				
					e1.printStackTrace();
				}
			}finally{

			}
		}
	}
	//------------------------------------------------------------------------------------------------
	
	//------------------------------------------------------------------------------------------------
	@Override
	public MathsResult computeResult(AlgebraicExpression ae,SimultaneousConfidenceIntervalCalculator scic) {		
		try {

			
			
			ArrayList<TransitionParameter> parameters = new ArrayList<TransitionParameter>();
			for(TransitionParameter tp : scic.getTransitionParameters()){
			//	if(ae.poly.contains(tp.getLabel())){
					parameters.add(tp);
				//}
			}
					
					//scic.getTransitionParameters();

			String varDefinition="clear all";			
			String F = "\nF = [";			
			for(TransitionParameter tp : parameters){
				varDefinition+="\n"+tp.getLabel()+" = sdpvar(1,1)";
				ConfidenceInterval ci = scic.getConfidenceInterval(tp);
				
				F+=//"\n%"+tp.getLabel()+" width is "+this.width(ci.getLowerBound(), ci.getUpperBound())+
						 "\n"+ci.getLowerBound()+"<="+tp.getLabel()+"<="+ci.getUpperBound()+",";
				
				
			}

			//builds a constraint such that the parameters x1,x2,x3 for each state x sum to 1
			//e.g. x1+x2+x3 == 1
			//we do not do this for single parameters, since it does not make sense to have kFail=1.0 
			for(StateParameterObservation sp : scic.stateParameterObservations()){

				//PROPER addition
				if (!sp.getTransitionParameters()[0].getLabel().startsWith("n")) {
				
					int nParameters = sp.getTransitionParameters().length;
					if(nParameters>1){
						String eq="\n";
						for(TransitionParameter tp : sp.getTransitionParameters()){
							//if(ae.poly.contains(tp.getLabel())){
								eq+=tp.getLabel()+"+";
								//System.out.println("transition: "+tp.getLabel());
							//} else{
								//System.out.println(tp+" not included in constraints");
						//	}
						}
	
						eq=eq.substring(0,eq.length()-1);//remove last +
						eq+="==1.0,";
						F+=eq;//add to constraint string
					}
				}
			}

			F=F.substring(0,F.length()-1)+"];";//remove last comma

			String requirement = "\nRmin = "+ae.getPolynomial()+"\n"+"Rmax = "+"-("+ae.getPolynomial()+")";

			String command = "\noptions = sdpsettings('solver','BMIBNB')"+
					"\noptimize(F,Rmin,options)";

			String cmd = varDefinition+F+requirement+command;
//			System.out.println("Evaluating: "+ cmd );
			proxy.eval(varDefinition+F+requirement+command);			
			Object[] returnArgumentsMin = proxy.returningEval("double(Rmin)", 1);
			//Retrieve the first (and only) element from the returned arguments
			Object minArgument = returnArgumentsMin[0];
			//Like before, cast and index to retrieve the double value
			double min = ((double[]) minArgument)[0];					
			proxy.eval("optimize(F,Rmax,options)");

			Object[] returnArgumentsMax = proxy.returningEval("double(Rmax)", 1);			
			Object maxArgument = returnArgumentsMax[0];	
			double max = ((double[]) maxArgument)[0];
			//System.out.println("optimize(F,Rmin,options) returned: "+min);
			//System.out.println("optimize(F,Rmax,options) returned: "+max);
			System.out.println("["+ min +","+ max +"]");


			return new MinMaxResult(min,max);

		} catch (MatlabInvocationException error) {

			error.printStackTrace();
		}
		return new MinMaxResult();


	}
	@Override
	public void stop() {
		proxy.disconnect();
		System.out.println("Disconnecting");	
	}
}
