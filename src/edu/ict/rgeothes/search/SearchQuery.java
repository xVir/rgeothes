package edu.ict.rgeothes.search;

public class SearchQuery {
	private String name;

	private DateRange dateRange;

	private SearchCoordinates searchCoordinates;

	public SearchQuery() {
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}

	public SearchCoordinates getSearchCoordinates() {
		return searchCoordinates;
	}

	public void setSearchCoordinates(SearchCoordinates searchCoordinates) {
		this.searchCoordinates = searchCoordinates;
	}

	public SearchQuery(String name, DateRange dateRange,
			SearchCoordinates searchCoordinates) {
		super();
		this.name = name;
		this.dateRange = dateRange;
		this.searchCoordinates = searchCoordinates;
	}
	
	
	
}
