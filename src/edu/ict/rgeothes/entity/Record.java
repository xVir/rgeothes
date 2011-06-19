package edu.ict.rgeothes.entity;

import java.util.ArrayList;
import java.util.List;

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
	
	
	
	public Name getPrimaryName(){
		return names.get(0);
	}
	
	public String getQualifier() {
		return qualifier;
	}
}
