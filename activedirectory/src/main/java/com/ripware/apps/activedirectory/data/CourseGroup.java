package com.ripware.apps.activedirectory.data;

import java.util.ArrayList;

/**
 * Course group
 * @author Brad Rippe 
 *
 */
public class CourseGroup extends CollegeGroup {

	private ArrayList<Relationship> relationships;
	
	/**
	 * Creates a course group with empty relationships
	 */
	public CourseGroup() {
		relationships = new ArrayList<Relationship>();
	}

	/**
	 * Gets relationships
	 * @return the course group relationships
	 */
	public ArrayList<Relationship> getRelationships() {
		return relationships;
	}

	/**
	 * Set the course group relationships
	 * @param relationships the relationships for the course group
	 */
	public void setRelationships(ArrayList<Relationship> relationships) {
		this.relationships = relationships;
	}
}
