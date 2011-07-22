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

	/**
	 * Date, representing unknown date in future
	 */
	public static final Date UNKNOWN_HIGH_DATE = new GregorianCalendar(9999,
			11, 31).getTime();

	/**
	 * Date, representing unknown date in past
	 */
	public static final Date UNKNOWN_LOW_DATE = new GregorianCalendar(0, 0, 1)
			.getTime();

	private static final DateFormat lifetimeFormat = new SimpleDateFormat("yyyy.MM.dd");

	public Document() {

	}

	private Document(String description) {
		this.description = description;
	}

	private String uri;
	private String description;

	private Date beginDate;
	private Date endDate;

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

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	private Date creationDate;

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

		hashCodeBuilder.append(uri);
		// hashCodeBuilder.append(description);
		hashCodeBuilder.append(creationDate);
		hashCodeBuilder.append(beginDate);
		hashCodeBuilder.append(endDate);

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
	
	/**
	 * Returns interval where document has power
	 */
	public String getLifetime(){
		return String.format("%s-%s", lifetimeFormat.format(beginDate),
				lifetimeFormat.format(endDate));
	}
	
	
	/**
	 * Returns true, if document is valid on specified date,
	 * otherwise false
	 * @param date
	 * @return
	 */
	public boolean isValidOnDate(Date date){
		if (date.after(beginDate) && date.before(endDate)) {
			return true;
		}
		
		return false;
	}

	
}
