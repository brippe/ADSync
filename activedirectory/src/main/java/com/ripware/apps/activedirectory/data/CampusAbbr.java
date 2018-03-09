package com.ripware.apps.activedirectory.data;

/**
 * Campus Abbreviations for A College, B College, and C College
 * @author Brad Rippe 
 *
 */
public enum CampusAbbr {
	ACollege("A"), BCollege("B"), CCollege("C");
		
	private String value;
	private CampusAbbr(String value) { this.value = value; }
	
	/**
	 * Returns the String representation of the Campus Abbreviation
	 */
	public String toString() {
		return value;
	}
};
