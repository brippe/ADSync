package com.ripware.apps.activedirectory.data;

/**
 * Represents a group, Course, College, Course Section, Term, or Department
 * @author Brad Rippe 
 *
 */
public interface Group {

	/**
	 * Gets the group id
	 * @return the group id
	 */
	public String getGroupID();
	
	/**
	 * Sets the group id
	 * @param groupID the group id
	 */
	public void setGroupID(String groupID);
	
	/**
	 * Gets the group type
	 * Could be College, Department, Course, Term, CourseSection, or CrossListedSection
	 * @return the group type
	 */
	public String getGroupType();
	
	/**
	 * Sets the group type
	 * @param groupType the group type
	 */
	public void setGroupType(String groupType);
	
	/**
	 * Gets the short description of the group
	 * @return the group the short description, ex. HS
	 */
	public String getShortDescription();
	
	/**
	 * Sets the group short description
	 * @param shortDescription the group short description
	 */
	public void setShortDescription(String shortDescription);
	
	/**
	 * Gets the long description of the group
	 * @return the group the long description, ex. Humanities and Social Sciences
	 */
	public String getLongDescription();
	
	/**
	 * Sets the group long description
	 * @param longDescription the group long description
	 */
	public void setLongDescription(String longDescription);
	
	/**
	 * Gets the type value level of the group
	 * @return the group the value level. Should be "1" for college
	 */
	public String getTypeValueLevel();
	
	/**
	 * Sets the group type value level
	 * @param typeValueLevel group type value level
	 */
	public void setTypeValueLevel(String typeValueLevel);
	
	/**
	 * Gets the scheme of the group
	 * @return the group the scheme. Should be "Luminis"
	 */
	public String getScheme();
	
	/**
	 * Sets the scheme
	 * @param scheme should be "Luminis"
	 */
	public void setScheme(String scheme);
	
	/**
	 * Gets the record status of the group
	 * @return the group the record status. Create update not provided
	 * 			3 for delete
	 */
	public String getRecStatus();
	
	/**
	 * Sets the record status. Create or update not provided
	 * 3 for delete
	 * @param recStatus the record status
	 */
	public void setRecStatus(String recStatus);
}
