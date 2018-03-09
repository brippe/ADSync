package com.ripware.apps.activedirectory.data;

/**
 * Group factory for creating groups
 * @author Brad Rippe 
 * @see Group
 */
public class GroupFactory {
	
	/**
	 * Creates a group by name
	 * @param groupName "Course", "CourseSection", "CrossListSection", "Term", or "College"
	 * @return a group
	 */
	public static Group instanceOfGroup(String groupName) {
		if(groupName.equals("Course")) {
			return new CourseGroup();
		} else if(groupName.equals("CourseSection")) {
			return new CourseSectionGroup();
		} else if(groupName.equals("CrossListSection")) {
			return new CrossListSectionGroup();
		} else if(groupName.equals("Term")){
			return new TermGroup();
		} else {
			return new CollegeGroup();
		}
	}
}
