package edu.ict.rgeothes.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;

/**
 * Main class, representing record of thesaurus
 */
public class Record {

	/**
	 * Record, which is the root of thesaurus (let's assume its "Earth")
	 */
	public static final Record ROOT_RECORD = new Record();
	{
		qualifier = "Earth";
	};
	
	private String qualifier;

	private RecordReference previous;
	
	private List<Name> names = new ArrayList<Name>();

	private List<Location> locations = new ArrayList<Location>();
	
	/**
	 * Objects, with current object contains
	 */
	private List<RecordReference> contains = new ArrayList<RecordReference>();
	
	/**
	 * Objects, which contains current object
	 */
	private List<RecordReference> belongTo = new ArrayList<RecordReference>();

	public Record() {
	}
	
	public void addName(Name name){
		names.add(name);
	}
	
	public void addBelongsTo(Record rec, Document doc){
		RecordReference recordReference = new RecordReference();
		recordReference.setDocument(doc);
		recordReference.setRecordFrom(this);
		recordReference.setRecordTo(rec);
		belongTo.add(recordReference);
	}
	
	public void addContains(Record rec, Document doc){
		RecordReference recordReference = new RecordReference(this, rec, doc);
		contains.add(recordReference);
	}
	
	public void addLocation(Location location){
		locations.add(location);
	}
	
	
	public Name getPrimaryName(){
		return names.get(0);
	}
	
	public Record getPrimaryParent(){
		
		if (this == ROOT_RECORD){
			return null;
		}
		
		if (belongTo.size() > 0){
			return belongTo.get(0).getRecordTo();
		}
		else{
			throw new IllegalStateException("Only ROOT_RECORD could not have Parent");
		}
	}
	
	/*
	 * Unique qualifier for record
	 */
	public String getFullQualifier() {
		
		if (getPrimaryParent() == null) {
			return qualifier;
		}
		
		if (getPrimaryParent() == ROOT_RECORD) {
			//we can ignore root record qualifier
			return qualifier;
		}

		return String.format("%s; %s", getPrimaryParent().getFullQualifier(),qualifier);
	}
	
	public String getQualifier() {
		return qualifier;
	}
	
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	
	public RecordReference getPrevious() {
		return previous;
	}
	
	public void setPrevious(RecordReference previous) {
		this.previous = previous;
	}
	
	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this,
				ApplicationContext.getInstance().getToStringStyle());
		stringBuilder.append("qualifier",getFullQualifier());
		stringBuilder.append("names", names, true);
		stringBuilder.append("locations",locations,true);
		return stringBuilder.toString();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		
		hashCodeBuilder.append(getFullQualifier());
		
		return hashCodeBuilder.toHashCode();
	}
}
