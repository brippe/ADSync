package com.ripware.apps.activedirectory.data;

/**
 * Member of a course or section
 * @author Brad Rippe 
 *
 */
public class Member {
	private String id;
	private int idType;	// 2 = member is a group; 1 is a person
	private int status; // 1 active; 0 inactive
	private String recStatus;
	
	/**
	 * Gets the member's id; banner id
	 * @return the member's id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Sets the id. If the id is < 8 digits the 
	 * id is padded with zeros at the beginning of the id
	 * @param id the banner id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Gets the id type
	 * @return 2 = member is a group; 1 is a person
	 */
	public int getIdType() {
		return idType;
	}
	
	/**
	 * Sets the id type 2 = member is a group; 1 is a person
	 * @param idType id type
	 */
	public void setIdType(int idType) {
		this.idType = idType;
	}
	
	/**
	 * Gets the status
	 * @return status 1 is active 0 is nonactive
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Sets the status 
	 * @param status 1 is active 0 is nonactive
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Gets the record status
	 * @return record status 3 = removed as member
	 */
	public String getRecStatus() {
		return recStatus;
	}
	
	/**
	 * Sets the record status
	 * @param status 3 = remove member
	 */
	public void setRecStatus(String status) {
		this.recStatus = status;
	}
}
