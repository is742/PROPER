package application.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModelProperty {

	private final StringProperty detail;
	
	public ModelProperty(){
		this(null);
	}
	
	public ModelProperty(String value){
		detail = new SimpleStringProperty(value);
	}
	
	public String getDetail(){
		return detail.get();
	}
	
	public StringProperty detailProperty(){
		return detail;
	}
	
	public void setDetail(String value){
		this.detail.set(value);	
	}
	
}
