package com.ripware.apps.activedirectory.data;

/**
 * Represent a College Group from the xml messages
 * @author Brad Rippe 
 *
 */
public class CollegeGroup implements Group {
	
	private String groupID;
	private String groupType; 		// could be "College", "Department", "Course", "Term",  "CourseSection", "CrossListedSection"
	private String scheme;
	private String shortDescription;
	private String longDescription;
	private String typeValueLevel;
	private String recStatus;
	
	/** 
	 * Creates a college group
	 */
	public CollegeGroup() {
		recStatus = "0"; // = 3 refers to a deleted college group; otherwise not specified		
	}
	
	/**
	 * Gets the group id
	 * @return the group id
	 */
	public String getGroupID() {
		return groupID;
	}
	
	/**
	 * Sets the group id
	 * @param groupID the group id
	 */
	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}
	
	/**
	 * Gets the group type
	 * Could be College, Department, Course, Term, CourseSection, or CrossListedSection
	 * @return the group type
	 */
	public String getGroupType() {
		return groupType;
	}
	
	/**
	 * Sets the group type
	 * @param groupType the group type
	 */
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	/**
	 * Gets the short description of the group
	 * @return the group the short description, ex. HS
	 */
	public String getShortDescription() {
		return shortDescription;
	}
	
	/**
	 * Sets the group short description
	 * @param shortDescription the group short description
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	
	/**
	 * Gets the long description of the group
	 * @return the group the long description, ex. Humanities and Social Sciences
	 */
	public String getLongDescription() {
		return longDescription;
	}
	
	/**
	 * Sets the group long description
	 * @param longDescription the group long description
	 */
	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}
	
	/**
	 * Gets the type value level of the group
	 * @return the group the value level. Should be "1" for college
	 */
	public String getTypeValueLevel() {
		return typeValueLevel;
	}
	
	/**
	 * Sets the group type value level
	 * @param typeValueLevel group type value level
	 */
	public void setTypeValueLevel(String typeValueLevel) {
		this.typeValueLevel = typeValueLevel;
	}
	
	/**
	 * Gets the scheme of the group
	 * @return the group the scheme. Should be "Luminis"
	 */
	public String getScheme() {
		return scheme;
	}
	
	/**
	 * Sets the scheme
	 * @param scheme should be "Luminis"
	 */
	public void setScheme(String scheme) {
		this.scheme = scheme;
	}
	
	/**
	 * Gets the record status of the group
	 * @return the group the record status. Create update not provided
	 * 			3 for delete
	 */
	public String getRecStatus() {
		return recStatus;
	}
	
	/**
	 * Sets the record status. Create or update not provided
	 * 3 for delete
	 * @param recStatus the record status
	 */
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}
}
