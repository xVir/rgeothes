package edu.ict.rgeothes.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import edu.ict.rgeothes.ApplicationContext;

/*
 * Main class, representing record of thesaurus
 */
public class Record {

	private String qualifier;

	private RecordReference previous;
	
	private List<Name> names = new ArrayList<Name>();

	private List<Location> locations = new ArrayList<Location>();
	
	/*
	 * Objects, with current object contains
	 */
	private List<RecordReference> contains = new ArrayList<RecordReference>();
	
	/*
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
		recordReference.setRecord(rec);
		belongTo.add(recordReference);
	}
	
	
	public Name getPrimaryName(){
		return names.get(0);
	}
	
	public String getQualifier() {
		return qualifier;
	}
	
	public void setQualifier(String qualifier) {
		this.qualifier = qualifier;
	}
	
	@Override
	public String toString() {
		ToStringBuilder stringBuilder = new ToStringBuilder(this,
				ApplicationContext.getInstance().getToStringStyle());
		stringBuilder.append("qualifier",getQualifier());
		stringBuilder.append("names", names, true);
		return stringBuilder.toString();
	}
}
