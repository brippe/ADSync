package com.ripware.apps.activedirectory.ldap;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.LdapTemplate;

import com.ripware.apps.activedirectory.data.Person;

/**
 * Person Search via Active Directory test
 * @author Brad Rippe 
 *
 */
@RunWith(JUnit4.class)
public class PersonSearchTest {

	private static PersonSearch personSearch;

	/**
	 * Sets up the class to communicate with Active Directory
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		LdapTemplate ldapTemplate = (LdapTemplate) context.getBean("ldapTemplate");
		personSearch = new PersonSearch(ldapTemplate);
	}

	/**
	 * Test method for {@link PersonSearch#getPerson(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetPerson() {
		List<Person> people = personSearch.getPerson("OU=People,DC=Ripware,DC=local", "00001040");
		assertEquals(people.size(), 1);
		assertEquals(people.get(0).getLogonID(), "00001040");	// person exists?

		people = personSearch.getPerson("OU=People,DC=Ripware,DC=local", "00001878");
		assertEquals(people.size(), 1);
		assertEquals(people.get(0).getLogonID(), "00001878");	// person exists?

		people = personSearch.getPerson("OU=People,DC=Ripware,DC=local", "99999999");
		assertEquals(people.size(), 0);
	}
}
