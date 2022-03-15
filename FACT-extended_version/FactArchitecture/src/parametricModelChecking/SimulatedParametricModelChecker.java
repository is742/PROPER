package parametricModelChecking;

import logging.FACTLogger;

public class SimulatedParametricModelChecker implements ParametricProbabilisticModelChecker{

	@Override
	public AlgebraicExpression modelCheck(ParametricProbabilisticModel m, Property p, FACTLogger flog) {
		return new AlgebraicExpression("(-160*w1*y2*x1-160*w1*y2*x2+77*k1*z1*y1+160*y2*x1-77*z1*y1-77*z2*y1+160*w1*y2+160*y1)/160");
	}

}
