package com.ripware.apps.activedirectory.data;

/**
 * Represent a relationship
 * @author Brad Rippe 
 *
 */
public class Relationship {
	private String relation; // 1 = parent
	private String relationshipID;
	private String label;	// College; Department
		
	/**
	 * Gets a relation 
	 * @return a relation 1 = parent
	 */
	public String getRelation() {
		return relation;
	}
	
	/**
	 * Sets the relation. 1 = parent
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	/**
	 * Gets the relationship id 
	 * @return the relationship id
	 */
	public String getRelationshipID() {
		return relationshipID;
	}
	
	/**
	 * Sets the relationship id 
	 * @param relationshipID the relation id
	 */
	public void setRelationshipID(String relationshipID) {
		this.relationshipID = relationshipID;
	}
	
	/**
	 * Gets the label. College; Department
	 * @return the label for the relationship
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets the label. College; Department
	 * @param label the label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
}
