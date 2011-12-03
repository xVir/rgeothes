package edu.ict.rgeothes.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
	
	private UUID qualifier;

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
	
	public List<Location> getLocations() {
		return locations;
	}
	
	
	public Name getPrimaryName(){
		return names.get(0);
	}
	
	public void setPrimaryName(Name name){
		if (names.size() == 0) {
			names.add(0, name);			
		}
		else{
			names.set(0, name);
		}
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
	
	public void setPrimaryParent(Record rec, Document doc){
		
		if (belongTo.size() == 0) {
			belongTo.add(0, new RecordReference(this, rec, doc));
		}
		else {
			belongTo.set(0, new RecordReference(this, rec, doc));	
		}
		
	}
	
	public UUID getQualifier() {
		return qualifier;
	}
	
	public void setQualifier(UUID qualifier) {
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
		stringBuilder.append("qualifier",getQualifier());
		stringBuilder.append("primaryName",getPrimaryName());
		stringBuilder.append("lifeteime",getPrimaryName().getLifetime());
		stringBuilder.append("endDoc",getPrimaryName().getEndDocument());
		stringBuilder.append("names", names, true);
		stringBuilder.append("locations",locations,true);
		return stringBuilder.toString();
	}
	
	@Override
	public int hashCode() {
		HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
		
		hashCodeBuilder.append(getQualifier());
		
		return hashCodeBuilder.toHashCode();
	}
}
