/**
 * 
 */
package com.ripware.apps.activedirectory.data;

/**
 * Represents an entity's role
 * @author Brad Rippe 
 */
public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	// institutionrole
	private String primaryrole; // primaryrole
	private String institutionroletype; // institutionroletype //"Student"; "Faculty"; "Staff"; "Alumni"; "ProspectiveStudent"
	
	/**
	 * Gets the primary role
	 * @return the primary role which doesn't exist in the SIS: ""
	 */
	public String getPrimaryrole() {
		return primaryrole;
	}
	
	/**
	 * Sets the primary role
	 * @param primaryrole this should be an empty string
	 */
	public void setPrimaryrole(String primaryrole) {
		this.primaryrole = primaryrole;
	}
	
	/**
	 * Gets the institution role type
	 * @return "Student"; "Faculty"; "Staff"; "Alumni"; "ProspectiveStudent"; etc
	 */
	public String getInstitutionroletype() {
		return institutionroletype;
	}
	
	/**
	 * Sets the institution role type
	 * @param institutionroletype the role type. Ex. "Student"; "Faculty"; "Staff"; "Alumni"; "ProspectiveStudent"
	 */
	public void setInstitutionroletype(String institutionroletype) {
		this.institutionroletype = institutionroletype;
	}	
}
