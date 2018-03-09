package com.ripware.apps.activedirectory.data;

/**
 * Course Section Group represents and course section
 * @author Brad Rippe 
 * @see CourseGroup
 *
 */
public class CourseSectionGroup extends CourseGroup {

	private String fullDesc;
	private String org;
	private String begin;
	private String end;
	private String deliverySystem;	// determines if the message is targeted for the LMS
									// there are other implications to this target see 6-6 in Banner Integration for eLearning guide	
	private String eventDescription;
	private String beginDate;
	private String endDate;
	private String daysOfWeek;
	private String beginTime;
	private String endTime;
	private String location;
	
	
	/**
	 * Gets the section full description
	 * @return the full description
	 */
	public String getFullDesc() {
		return fullDesc;
	}
	
	/**
	 * Sets the full description
	 * @param fullDesc the full description
	 */
	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}
	
	/**
	 * Gets the organization. Name of the department
	 * @return the organization
	 */
	public String getOrg() {
		return org;
	}
	
	/**
	 * Sets the organization. Name of the department
	 * @param org the organization
	 */
	public void setOrg(String org) {
		this.org = org;
	}
	
	/**
	 * Gets the beginning date of the section
	 * @return the beginning 
	 */
	public String getBegin() {
		return begin;
	}
	
	/**
	 * Sets the beginning date
	 * @param begin the beginning date
	 */
	public void setBegin(String begin) {
		this.begin = begin;
	}
	
	/**
	 * Gets the ending date of the section
	 * @return the ending 
	 */
	public String getEnd() {
		return end;
	}
	
	/**
	 * Sets the ending date
	 * @param end the ending date
	 */
	public void setEnd(String end) {
		this.end = end;
	}
	
	/**
	 * Gets the delivery system responsible for content.
	 * @return the delivery system. Ex. WEBCT
	 */
	public String getDeliverySystem() {
		return deliverySystem;
	}
	
	/**
	 * Sets the delivery system responsible for content.
	 * @param deliverySystem the delivery system, WEBCT, LUMINIS
	 */
	public void setDeliverySystem(String deliverySystem) {
		this.deliverySystem = deliverySystem;
	}
	
	/**
	 * Gets the event description
	 * @return the event description 
	 */
	public String getEventDescription() {
		return eventDescription;
	}
	
	/**
	 * Sets the event description
	 * @param eventDescription the event description
	 */
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	
	/**
	 * Gets the beginning date for the section
	 * @return the beginning date
	 */
	public String getBeginDate() {
		return beginDate;
	}
	
	/**
	 * Sets the beginning date for the section 
	 * @param beginDate the beginning date for the section 
	 */
	public void setBeginDate(String beginDate) {
		this.beginDate = beginDate;
	}
	
	/**
	 * Gets the ending date for the section 
	 * @return ending date for the section
	 */
	public String getEndDate() {
		return endDate;
	}
	
	/**
	 * Sets the ending date for the section 
	 * @param endDate the ending date for the section 
	 */	
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * Gets the days of the week
	 * @return days of the week
	 */
	public String getDaysOfWeek() {
		return daysOfWeek;
	}
	
	/**
	 * Sets the days of the week
	 * @param daysOfWeek the days of the week
	 */
	public void setDaysOfWeek(String daysOfWeek) {
		this.daysOfWeek = daysOfWeek;
	}
	
	/**
	 * Gets the beginning time of the section
	 * @return beginning time of the section
	 */
	public String getBeginTime() {
		return beginTime;
	}
	
	/**
	 * Sets the beginning time of the section 
	 * @param beginTime the beginning time
	 */
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	
	/** 
	 * Sets the ending time of the section 
	 * @return ending time of the section 
	 */
	public String getEndTime() {
		return endTime;
	}
	
	/**
	 * Sets the ending time of the section 
	 * @param endTime ending time of the section
	 */
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * Gets the location, building with room number or TBA
	 * @return the location of the section
	 */
	public String getLocation() {
		return location;
	}
	
	/**
	 * Sets the location, building with the room number of TBA
	 * @param location the location
	 */
	public void setLocation(String location) {
		this.location = location;
	}
}
