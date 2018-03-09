package com.ripware.apps.activedirectory.data;

import java.util.ArrayList;

/**
 * Contains in course enrollment information.
 * @author Brad Rippe 
 */
public class Enroll implements Roster {
	private String courseId;
	private ArrayList<EnrollMember> members;
	
	/**
	 * Creates an Enroll with no enrolled members
	 */
	public Enroll() {
		members = new ArrayList<EnrollMember>();
	}

	/**
	 * Retrieves the course id. In the format 63331.200520
	 * @return the course id crn.termcode
	 */
	public String getCourseId() {
		return courseId;
	}

	/**
	 * Sets the course id. Should be in the format 63331.200520
	 * @param courseId the course id 
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	/**
	 * Retrieves the members of this course 
	 * @return
	 */
	public ArrayList<EnrollMember> getMembers() {
		return members;
	}

	/**
	 * Sets the members for the course
	 * @param members the members of the course
	 */
	public void setMembers(ArrayList<EnrollMember> members) {
		this.members = members;
	}
}
