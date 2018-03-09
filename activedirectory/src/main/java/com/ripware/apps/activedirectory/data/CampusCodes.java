package com.ripware.apps.activedirectory.data;

/**
 * Campus Codes for A College, B College, and the C College
 * @author Brad Rippe 
 *
 */
public enum CampusCodes {
	ACollege(1), BCollege(2), CCollege(3);
		
	private int value;
	private CampusCodes(int value) { this.value = value; }
	
	/**
	 * Returns the String representation of the Campus Code
	 */
	public String toString() {
		return Integer.toString(value);
	}
	
	/*
	 * public boolean equals declared final in Enum
	 */
};
