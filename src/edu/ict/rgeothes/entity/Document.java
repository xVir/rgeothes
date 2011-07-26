package edu.ict.rgeothes.entity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;

/**
 * Class representing document item
 */
public class Document {

	public static final Document UNKNOWN_DOCUMENT = new Document(
			"unknown document");



	public Document() {

	}

	private Document(String description) {
		this.description = description;
	}
	
	public Document(String description,Date date) {
		this.description = description;
		this.date = date;
	}

	private String uri;
	private String description;

	private Date date;

	/**
	 * Creation date of document
	 */
	private Date creationDate;
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date beginDate) {
		this.date = beginDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

		hashCodeBuilder.append(uri);
		// hashCodeBuilder.append(description);
		hashCodeBuilder.append(creationDate);
		hashCodeBuilder.append(date);

		return hashCodeBuilder.toHashCode();
	}

	@Override
	public String toString() {
		ToStringBuilder builder = new ToStringBuilder(this, ApplicationContext
				.getInstance().getToStringStyle());

		builder.append(description);
		builder.append("URI", uri);

		return builder.toString();
	}
	
	

	
}
