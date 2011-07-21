package edu.ict.rgeothes;

import org.apache.commons.lang.builder.StandardToStringStyle;

public class ApplicationContext {

	private static ApplicationContext appContext;
	
	private StandardToStringStyle toStringStyle;
	
	private ApplicationContext(){
		
		
		toStringStyle = new StandardToStringStyle();
		toStringStyle.setUseShortClassName(true);
		toStringStyle.setUseIdentityHashCode(false);
		toStringStyle.setUseClassName(false);
	}
	
	public static ApplicationContext getInstance(){
		if (appContext == null) {
			appContext=new ApplicationContext();
		}
		return appContext;
	}
	
	public StandardToStringStyle getToStringStyle() {
		return toStringStyle;
	}
	
}
