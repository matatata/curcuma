package de.ceruti.curcuma.keyvaluebinding;

import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;



public class BindingInfoImpl implements BindingInfo {
	private Object observedObject;
	private BindingOptions options;
	private String observedKeyPath;
	private String name;
	
	
	private int nestingLevel;
	
	public void enterUpdatingSubject() {
		nestingLevel++;
	}

	public void exitUpdatingSubject() {
		nestingLevel--;
	}
	
	public int nestingLevelUpdatingSubject(){
		return nestingLevel;
	}


	public BindingInfoImpl(String name,Object observedObj,String observedKeyPath,BindingOptions options){
		setObservedObject(observedObj);
		setObservedKeyPath(observedKeyPath);
		setOptions(options);
		setName(name);
	}

	@Override
	public String getObservedKeyPath() {
		return observedKeyPath;
	}

	public void setObservedKeyPath(String observedKeyPath) {
		this.observedKeyPath = observedKeyPath;
	}

	@Override
	public BindingOptions getOptions() {
		return options;
	}

	public void setOptions(BindingOptions options) {
		this.options = options;
	}

	@Override
	public Object getObservedObject() {
		return observedObject;
	}

	public void setObservedObject(Object observedObject) {
		this.observedObject = observedObject;
	}
	
//	public String toString()
//	{
//		StringBuffer buf = new StringBuffer();
//		buf.append("BindingInfoImpl[oberservedObject: " + observedObject + " keypath:" + observedKeyPath + "]" );
//		return buf.toString();
//	}
	
	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
