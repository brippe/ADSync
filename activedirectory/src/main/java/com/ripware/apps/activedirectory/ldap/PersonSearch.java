package com.ripware.apps.activedirectory.ldap;

import java.util.List;

import org.springframework.ldap.core.LdapTemplate;

import com.ripware.apps.activedirectory.data.Person;

/**
 * Person Ldap search. Used for querying Active Directory 
 * to determine if a user exists in the directory already
 * @author Brad Rippe 
 * @see org.springframework.ldap.core.LdapTemplate
 */
public class PersonSearch {

	private LdapTemplate ldapTemplate;
	
	/**
	 * Create a person search based on the ldap template
	 * @param ldapTemplate the ldap template
	 */
	public PersonSearch(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	
	/**
	 * Searches the directory service for a person
	 * @param dnBase the base domain name for the search
	 * @param bannerID the banner id of the person to search for
	 * @return list of people matching the banner id or an empty list 
	 * 			if the person doesn't exist
	 */
	@SuppressWarnings("unchecked")
	public List<Person> getPerson(String dnBase, String bannerID) {
		StringBuffer query = new StringBuffer();
		UserAttributesMapper mapper = new UserAttributesMapper();
		query.append("(&(objectClass=person)(cn=");
		query.append(bannerID);
		query.append("))");
		return ldapTemplate.search(dnBase, query.toString(), mapper);
	}
	
	/**
	 * Sets the ldapTemplate
	 * @param ldapTemplate the ldap template to use for accessing the data store
	 */
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
}
