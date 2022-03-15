package inputOutput;

import parametricModelChecking.AlgebraicExpression;
import parametricModelChecking.ParametricProbabilisticModel;
import parametricModelChecking.Property;

public class ParametricModelCheckingIO {

	public ParametricProbabilisticModel model;
	public Property property;
	public AlgebraicExpression algebraicExpressionResult;


	public ParametricModelCheckingIO(ParametricProbabilisticModel model,Property property){
		this.model = model;
		this.property = property;
		this.algebraicExpressionResult = null;
	}



}
