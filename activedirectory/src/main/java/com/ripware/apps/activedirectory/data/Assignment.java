package com.ripware.apps.activedirectory.data;

import java.util.ArrayList;

/**
 * Instructor assignment to a course
 * @author Brad Rippe 
 *
 */
public class Assignment implements Roster {
	private String courseId;
	private ArrayList<EnrollMember> members;

	/**
	 * Creates an assignment
	 */
	public Assignment() {
		members = new ArrayList<EnrollMember>();
	}

	/**
	 * Retrieves the course id in the format 30072.201220
	 * @return the course id
	 */
	public String getCourseId() {
		return courseId;
	}

	/**
	 * Sets the course id.
	 * @param courseId the course id
	 */
	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	/**
	 * Retrieves the list of instructors assigned to the course
	 * @return the instructors assigned to the course
	 */
	public ArrayList<EnrollMember> getMembers() {
		return members;
	}

	/**
	 * Sets the list of instructors assigned to the course
	 * @param members the instructors
	 */
	public void setMembers(ArrayList<EnrollMember> members) {
		this.members = members;
	}
}

