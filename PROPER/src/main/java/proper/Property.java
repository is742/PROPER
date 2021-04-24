//==============================================================================
//	
//	Copyright (c) 2021-
//	Authors:
//	* Ioannis Stefanakos (University of York)
//	
//------------------------------------------------------------------------------
//	
//	This file is part of PROPER.
//	
//==============================================================================
package proper;

/**
 * Property class
 * @author istefanakos
 *
 */
public class Property {
	private int state;
	private double value;
	
	public Property(int state, double value) {
		this.state=state;
		this.value=value;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
