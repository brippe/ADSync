package com.ripware.apps.activedirectory.data;

import java.util.ArrayList;

/**
 * Represents a cross list section members
 * This could be a student or staff member
 * @author Brad Rippe 
 * @see Member
 */
public class CrossListSectionMembership {
	private String id;		// Cross List ID	
	private ArrayList<Member> members;
	
	/**
	 * Creates a cross listed membership
	 */
	public CrossListSectionMembership() {
		members = new ArrayList<Member>();
	}

	/**
	 * Gets the crosslist id
	 * @return the crosslist id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the crosslist id
	 * @param id the crosslist id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the crosslist membership 
	 * @return the crosslist membership
	 */
	public ArrayList<Member> getMembers() {
		return members;
	}

	/**
	 * Sets the member list for the cross list
	 * @param members the members
	 */
	public void setMembers(ArrayList<Member> members) {
		this.members = members;
	}
}
