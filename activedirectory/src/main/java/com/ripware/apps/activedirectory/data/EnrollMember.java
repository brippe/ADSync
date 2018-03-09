package com.ripware.apps.activedirectory.data;

/**
 * Enrolled member of a course
 * @author Brad Rippe 
 *
 */
public class EnrollMember {
	
	private String srcId;		// event source id
	private int idType; 		// 1 indicates a person
	private String roleType; 	// 01 indicates a student has a learner role
	private int status;			// determines if the student is active or not 1 active 0 inactive
	private String recStatus;	// if = 3; delete student
	private String beginEnroll; //
	private String endEnroll;	//
	private int beginRestricted; //
	private int endRestricted;	 // 0 restricted by date; 1 not restricted	
	private String midTermGradingMode; 	// Letter Grade; Audit
	private String finalGradeMode;		// Letter Grade; Audit	
	private String subRole;
	private String bannerId;		// not in message must be translated from GOBSRID
	
	/**
	 * Create a Enroll Member
	 */
	public EnrollMember() {
		srcId = "";
		idType = 0; 			// 1 indicates a person
		roleType = ""; 			// 01 indicates a student has a learner role
		status = 0;				// determines if the student is active or not 1 active 0 inactive
		recStatus = "";			// if = 3; delete student
		beginEnroll = "";
		endEnroll = "";
		beginRestricted = 0;
		endRestricted = 0;	 	// 0 restricted by date; 1 not restricted
		
		midTermGradingMode = "";// Letter Grade; Audit
		finalGradeMode = "";	// Letter Grade; Audit
		subRole = "";
	}
	
	/**
	 * Gets the source id. Located in GOBSRID. 
	 * This id must be translated into a pidm
	 * @return the source id
	 */
	public String getSourceId() {
		return srcId;
	}
	
	/**
	 * Set the source id. Source ids are stored in GOBSRID
	 * @param srcId the source id 
	 */
	public void setSourceId(String srcId) {
		this.srcId = srcId;
	}
		
	/**
	 * Get the id type
	 * @return the id type, 1 indicates a person
	 */
	public int getIdType() {
		return idType;
	}
	
	/**
	 * Sets the id type, 1 indicates a person
	 * @param idType id type
	 */
	public void setIdType(int idType) {
		this.idType = idType;
	}
	
	/**
	 * Gets the role type
	 * @return role type 01 indicates a student has a learner role, 02 an instructor
	 */
	public String getRoleType() {
		return roleType;
	}
	
	/**
	 * Sets the role type 
	 * @param roleType role type 01 indicates a student has a learner role, 02 an instructor
	 */
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
	
	/**
	 * Gets the status
	 * Determines if the member is active or not 1 active 0 inactive
	 * @return thes status
	 */
	public int getStatus() {
		return status;
	}
	
	/**
	 * Sets the member's status 
	 * if the member is active or not 1 active 0 inactive
	 * @param status 1 active 0 inactive
	 */
	public void setStatus(int status) {
		this.status = status;
	}
	
	/**
	 * Gets the record status
	 * Record status determines if the member is
	 * being added or dropped
	 * if = 3; delete/remove record
	 * @return
	 */
	public String getRecStatus() {
		return recStatus;
	}
	
	/**
	 * Sets the record status 
	 * Determines if the member is added or dropped
	 * @param recStatus 3 = dropped
	 */
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}
	
	/**
	 * Gets the begin enroll
	 * @return begin enroll
	 */
	public String getBeginEnroll() {
		return beginEnroll;
	}
	
	/**
	 * Sets the begin enroll value 
	 * @param beginEnroll begin enroll
	 */
	public void setBeginEnroll(String beginEnroll) {
		this.beginEnroll = beginEnroll;
	}
	
	/**
	 * Gets the end enroll
	 * @return end enroll
	 */
	public String getEndEnroll() {
		return endEnroll;
	}
	
	/**
	 * Sets the end enroll
	 * @param endEnroll end enroll
	 */
	public void setEndEnroll(String endEnroll) {
		this.endEnroll = endEnroll;
	}
	
	/**
	 * Gets the begin restricted value
	 * @return 0 restricted by date; 1 not restricted	
	 */
	public int getBeginRestricted() {
		return beginRestricted;
	}
	
	/**
	 * Sets the begin restricted value
	 * @param beginRestricted 0 restricted by date; 1 not restricted	
	 */
	public void setBeginRestricted(int beginRestricted) {
		this.beginRestricted = beginRestricted;
	}
	
	/**
	 * Gets the end restricted value
	 * @return 0 restricted by date; 1 not restricted	
	 */
	public int getEndRestricted() {
		return endRestricted;
	}
	
	/**
	 * Sets the end of the restricted period
	 * @param endRestricted 0 restricted by date; 1 not restricted	 
	 */
	public void setEndRestricted(int endRestricted) {
		this.endRestricted = endRestricted;
	}
	
	/**
	 * Gets the mid-term grade mode
	 * @return Letter Grade; Audit
	 */
	public String getMidTermGradingMode() {
		return midTermGradingMode;
	}
	
	/**
	 * Sets the mid-term grade mode
	 * @param midTermGradingMode Letter Grade; Audit
	 */
	public void setMidTermGradingMode(String midTermGradingMode) {
		this.midTermGradingMode = midTermGradingMode;
	}
	
	/**
	 * Gets the final grade mode
	 * @return Letter Grade; Audit
	 */
	public String getFinalGradeMode() {
		return finalGradeMode;
	}
	
	/**
	 * Sets the final grade mode
	 * @param finalGradeMode the final grade mode Letter Grade; Audit
	 */
	public void setFinalGradeMode(String finalGradeMode) {
		this.finalGradeMode = finalGradeMode;
	}
	
	/**
	 * Gets the subrole
	 * @return the subrole
	 */
	public String getSubRole() {
		return subRole;
	}
	
	/**
	 * Sets the subrole
	 * @param subRole the sub role
	 */
	public void setSubRole(String subRole) {
		this.subRole = subRole;
	}

	/**
	 * Gets the banner id
	 * @return the banner id
	 */
	public String getBannerId() {
		return bannerId;
	}

	/**
	 * Sets the banner id
	 * @param bannerId the sis identifier
	 */
	public void setBannerId(String bannerId) {
		this.bannerId = bannerId;
	}
}
