package edu.ict.rgeothes.entity;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;

/*
 * Class, representing name of the record
 */
public class Name {

	private String name;
	private String type;

	private String language;

	public Name() {
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

	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this,
				ApplicationContext.getInstance().getToStringStyle());
		stringBuilder.append("name", name);
		stringBuilder.append("type", type);
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

		hashCodeBuilder.append(name);
		hashCodeBuilder.append(type);

		return hashCodeBuilder.toHashCode();
	}

}
