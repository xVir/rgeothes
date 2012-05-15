package edu.ict.rgeothes.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;
import edu.ict.rgeothes.search.DateRange;

/**
 * Main class, representing record of thesaurus
 */
@Entity
@Table(name = "thesaurus_record")
public class Record implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7685385488885134162L;

	/**
	 * Record, which is the root of thesaurus (let's assume its "Earth")
	 */
	public static final Record ROOT_RECORD = new Record();

	@Id
	private UUID qualifier;

	@OneToMany
	private List<Name> names = new ArrayList<Name>();

	@OneToMany
	private List<Location> locations = new ArrayList<Location>();

	/**
	 * Objects, with current object contains
	 */
	@Transient
	private List<RecordReference> contains = new ArrayList<RecordReference>();

	/**
	 * Objects, which contains current object
	 */
	@Transient
	private List<RecordReference> belongTo = new ArrayList<RecordReference>();

	public Record() {
		qualifier = UUID.randomUUID();
	}

	public List<Name> getNames() {
		return names;
	}

	public void setNames(List<Name> names) {
		this.names = names;
	}

	public List<RecordReference> getContains() {
		return contains;
	}

	public void setContains(List<RecordReference> contains) {
		this.contains = contains;
	}

	public List<RecordReference> getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(List<RecordReference> belongTo) {
		this.belongTo = belongTo;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void addName(Name name) {
		names.add(name);
	}

	public void addBelongsTo(Record rec, Document doc) {
		RecordReference recordReference = new RecordReference();
		recordReference.setDocument(doc);
		recordReference.setRecordFrom(this);
		recordReference.setRecordTo(rec);
		belongTo.add(recordReference);
	}

	public void addContains(Record rec, Document doc) {
		RecordReference recordReference = new RecordReference(this, rec, doc);
		contains.add(recordReference);
	}

	public void addLocation(Location location) {
		locations.add(location);
	}

	public List<Location> getLocations() {
		return locations;
	}

	public Record getPrimaryParent() {

		if (this == ROOT_RECORD) {
			return null;
		}

		if (belongTo.size() > 0) {
			return belongTo.get(0).getRecordTo();
		} else {
			throw new IllegalStateException(
					"Only ROOT_RECORD could not have Parent");
		}
	}

	public void setPrimaryParent(Record rec, Document doc) {

		if (belongTo.size() == 0) {
			belongTo.add(0, new RecordReference(this, rec, doc));
		} else {
			belongTo.set(0, new RecordReference(this, rec, doc));
		}

	}

	public UUID getQualifier() {
		return qualifier;
	}

	public void setQualifier(UUID qualifier) {
		this.qualifier = qualifier;
	}

	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this,
				ApplicationContext.getInstance().getToStringStyle());
		stringBuilder.append("qualifier", getQualifier());
		stringBuilder.append("names", names, true);
		stringBuilder.append("locations", locations, true);
		return stringBuilder.toString();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();

		hashCodeBuilder.append(getQualifier());

		return hashCodeBuilder.toHashCode();
	}

	public boolean hasName(String name) {

		for (Name recordName : names) {
			if (recordName.getName().equalsIgnoreCase(name)) {
				return true;
			}
		}

		return false;
	}

	public boolean hasNamesValidIn(DateRange dateRange) {
		for (Name name : names) {
			
			DateRange validRange = name.getValidRange();
			
			if (validRange.intersects(dateRange)) {
				return true;
			}
			
			if (validRange.contains(dateRange))
			{
				
			}
			
		}
		return false;
	}
}
