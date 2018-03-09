package com.ripware.apps.activedirectory.ldap;

import static org.junit.Assert.assertTrue;

import java.util.List;

import com.ripware.apps.activedirectory.ConsumerConfig;
import com.ripware.apps.activedirectory.data.CampusAbbr;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.ldap.core.LdapTemplate;

/**
 * Group Search via Active Directory test
 * @author Brad Rippe 
 *
 */
@RunWith(JUnit4.class)
public class GroupSearchTest {

	private static GroupSearch groupSearch;
	private static ConsumerConfig cConfig;

	/**
	 * Sets up the class to communicate with Active Directory
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
		LdapTemplate ldapTemplate = (LdapTemplate) context.getBean("ldapTemplate");
		groupSearch = new GroupSearch(ldapTemplate);
		cConfig = (ConsumerConfig) context.getBean("config");
	}

	/**
	 * Test method for {@link GroupSearch#getGroupNames(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testGetGroupNames() {
		List<String> groups = groupSearch.getGroupNames(cConfig.getAGroupPath(), CampusAbbr.ACollege.toString());
        int size = groups.size();
        assertTrue("ACollege groups aren't greater than 0", size > 0);

        groups.addAll(groupSearch.getGroupNames(cConfig.getBGroupPath(), CampusAbbr.BCollege.toString()));
        assertTrue("BCollege groups aren't greater than 0", groups.size() > size); // should add to size

        size = groups.size();
        groups.addAll(groupSearch.getGroupNames(cConfig.getCGroupPath(), CampusAbbr.CCollege.toString()));
        assertTrue("CCollege groups aren't greater than 0", groups.size() > size);
	}
}

