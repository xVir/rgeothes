package edu.ict.rgeothes.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;
import edu.ict.rgeothes.search.DateRange;

/*
 * Class, representing name of the record
 */
@Entity
@Table(name="thesaurus_name")
public class Name implements Serializable{

	private static final DateFormat lifetimeFormat = new SimpleDateFormat("yyyy.MM.dd");
	
	@Id
	private transient long id;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	private String name;
	private String type;

	private String language;

	/**
	 * Document, what makes name legal
	 */
	private Document beginDocument;
	
	/**
	 * Document, what cancel name
	 */
	private Document endDocument; 
	

	public Name() {
	}

	public Name(String name, String type, String language) {
		super();
		this.name = name;
		this.type = type;
		this.language = language;
		
		this.beginDocument = Document.FUTURE;
		this.endDocument = Document.FUTURE;
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

	
	
	public Document getBeginDocument() {
		return beginDocument;
	}

	public void setBeginDocument(Document beginDocument) {
		this.beginDocument = beginDocument;
	}

	public Document getEndDocument() {
		return endDocument;
	}

	public void setEndDocument(Document endDocument) {
		this.endDocument = endDocument;
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

	

	/**
	 * Returns true, if name is valid on specified date,
	 * otherwise false
	 * @param date
	 * @return
	 */
	public boolean isValidOnDate(Date date){
		if (date.after(beginDocument.getDate()) 
				&& date.before(endDocument.getDate())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getLifetime(){
		StringBuilder builder = new StringBuilder();
		
		if (beginDocument != null && beginDocument != Document.FUTURE) {
			builder.append(lifetimeFormat.format(beginDocument.getDate()));
		}
		else {
			builder.append("unknown");
		}
		
		builder.append("-");
		
		if (endDocument != null && endDocument != Document.FUTURE) {
			builder.append(lifetimeFormat.format(endDocument.getDate()));
		}
		else{
			builder.append("unknown");	
		}
		
		return builder.toString();
	}

	public DateRange getValidRange() {
		return new DateRange(beginDocument.getDate(), endDocument.getDate());
	}
	
}
