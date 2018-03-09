package com.ripware.apps.activedirectory.data;


/**
 * Represents a term.
 * @author Brad Rippe 
 * @see CollegeGroup
 */
public class TermGroup extends CollegeGroup {
	
	private String begin;
	private String end;
	private int enrollAccept;  // 0 = no or 1 = yes for enrollment accepted	
	private int enrollAllowed; // 0 always target system can't enroll students
	
	/**
	 * Gets the beginning date of the term
	 * @return the beginning 
	 */
	public String getBegin() {
		return begin;
	}
	
	/**
	 * Sets the beginning date
	 * @param begin the beginning date
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}
	
	/**
	 * Gets the ending date of the term
	 * @return the ending 
	 */
	public String getEnd() {
		return end;
	}
	
	/**
	 * Sets the ending date for the term
	 * @param end the ending date
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	
	/**
	 * Gets enroll accept. indicates whether a term accepts enrollment
	 * @return 0 or 1 indicating whether enrollment is accepted
	 */
	public int getEnrollAccept() {
		return enrollAccept;
	}
	
	/**
	 * Sets enroll accept
	 * @param enrollAccept either 0 or 1 
	 */
	public void setEnrollAccept(int enrollAccept) {
		this.enrollAccept = enrollAccept;
	}
	
	/**
	 * Indicates whether the target system can enroll users
	 * @return 0 always; target can't enroll users
	 */
	public int getEnrollAllowed() {
		return enrollAllowed;
	}
	
	/**
	 * Sets the enroll allowed by the target system
	 * @param enrollAllowed should be 0 in all cases
	 * 			target systems aren't allowed to enroll users
	 */
	public void setEnrollAllowed(int enrollAllowed) {
		this.enrollAllowed = enrollAllowed;
	}		
}
