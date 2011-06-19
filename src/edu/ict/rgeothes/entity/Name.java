package edu.ict.rgeothes.entity;

/*
 * Class, representing name of the record
 */
public class Name {

	private String name;
	private String type;
	
	private String language;

	public Name() {
		// TODO Auto-generated constructor stub
	}
	
	public Name(String name, String type, String language) {
		super();
		this.name = name;
		this.type = type;
		this.language = language;
	}	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}


	
	 
}
