package edu.ict.rgeothes.entity;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang.NotImplementedException;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;
import edu.ict.rgeothes.utils.DateUtils;

/**
 * Class representing document item
 */
@Entity
@Table(name = "thesaurus_document")
public class Document implements Serializable {

	public static final Document FUTURE = new Document(
			"future");

	public static final Document PAST = new Document("past");
	
	static {
		FUTURE.setId(1);

		FUTURE.setDate( DateUtils.CreateDate(3000, Calendar.JANUARY, 1));
		FUTURE.setCreationDate(DateUtils.CreateDate(3000, Calendar.JANUARY, 1));
		
		PAST.setId(0);
		PAST.setDate(DateUtils.CreateDate(1, 1, 1));
		PAST.setCreationDate(DateUtils.CreateDate(1, 1, 1));
	}

	public Document() {

	}

	private Document(String description) {
		this.description = description;
	}

	public Document(String description, Date date) {
		this.description = description;
		this.date = date;
	}

	@Id
	private transient long id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private String uri;
	private String description;

	@Temporal(TemporalType.DATE)
	private Date date;

	/**
	 * Creation date of document
	 */
	@Temporal(TemporalType.DATE)
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
		if (creationDate == null) {
			return date;
		}
		
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
