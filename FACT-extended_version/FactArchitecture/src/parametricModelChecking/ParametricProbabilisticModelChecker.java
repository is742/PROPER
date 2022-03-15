package parametricModelChecking;

import logging.FACTLogger;

public interface ParametricProbabilisticModelChecker {
	
	public AlgebraicExpression modelCheck(ParametricProbabilisticModel m,Property p, FACTLogger flog);
	

}
