package com.ripware.apps.activedirectory.db;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * CourseDAO test case
 * @author Brad Rippe 
 *
 */
@RunWith(JUnit4.class)
public class CourseDAOTest {

	private static CourseDAO courseDAO;

	/**
	 * Setup for the test
	 */
	@BeforeClass
	public static void runBeforeClass() {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		courseDAO = (CourseDAO) context.getBean("courseDAO");
	}

	/**
	 * Test method for {@link CourseDAO#getCampus(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetCampus() {
		assertEquals(courseDAO.getCampus("201230", "30001"), "2");
		assertEquals(courseDAO.getCampus("201230", "30157"), "1");
	}
}
