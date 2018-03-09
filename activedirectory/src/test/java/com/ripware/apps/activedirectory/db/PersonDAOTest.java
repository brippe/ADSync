package com.ripware.apps.activedirectory.db;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.ripware.apps.activedirectory.data.Person;

/**
 * PersonDAO Test Case
 * @author Brad Rippe
 *
 */
@RunWith(JUnit4.class)
public class PersonDAOTest {

	private static PersonDAO personDAO;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		personDAO = (PersonDAO) context.getBean("personDAO");
	}

	/**
	 * Test method for {@link PersonDAO#getPerson(java.lang.String)}.
	 */
	@Test
	public void testGetPerson() {
		Person p = personDAO.getPerson("00001149");
		assertNotNull(p);
		assertEquals(p.getFamilyName(), "Rippe");
		assertEquals(p.getGivenName(), "Brad");
	}

	@Test
	public void testGetPrimaryInstructorBannerId() {
		String bannerId = personDAO.getPrimaryInstructorBannerId("201235", "11280");
		assertEquals(bannerId, "00003532");
	}
}

