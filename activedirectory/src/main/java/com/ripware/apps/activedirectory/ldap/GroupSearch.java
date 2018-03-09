package com.ripware.apps.activedirectory.ldap;

import java.util.List;

import javax.naming.directory.SearchControls;

import org.apache.log4j.Logger;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.AttributesMapperCallbackHandler;
import org.springframework.ldap.core.CollectingNameClassPairCallbackHandler;
import org.springframework.ldap.core.LdapTemplate;

/**
 * Group Ldap search. Used for querying Active Directory 
 * to retrieve group names
 * @author Brad Rippe 
 * @see org.springframework.ldap.core.LdapTemplate
 */
public class GroupSearch {
	
	static Logger log = Logger.getLogger(GroupSearch.class.getName());
	private LdapTemplate ldapTemplate;
	
	/**
	 * Create a group search based on the ldap template
	 * @param ldapTemplate the ldap template
	 */
	public GroupSearch(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
	
	/**
	 * Searches the directory service for a group names. There are the created security 
	 * groups for depts, divs, crns etc. This search is a recursive search from the dnBase
	 * If security groups are in a child Organizational Unit from the dnBase, this search
	 * will find the child security groups.
	 * @param dnBase the base domain name for the search, should contain
	 * 			OU information for OU=BCollege, ACollege, or CCollege
	 * 			Example: OU=Groups,OU=ACollege,DC=Ripware,DC=local
	 * @param prefix the college prefix to get the list groups from: CC, FC, or CCollege
	 * @return list of security groups in the dnBase; remember AD puts a limit of 1000 on
	 * 			result sizes by default; This method pages results to return all the groups
	 */
	@SuppressWarnings("unchecked")
	public List<String> getGroupNames(String dnBase, String prefix) {				
		StringBuffer filter = new StringBuffer();
		filter.append("(|(CN=");
		filter.append(prefix);
		filter.append("-*)(objectcategory=group))");
				
		SearchControls searchControls = new SearchControls();
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		GroupAttributesMapper mapper = new GroupAttributesMapper();
		CollectingNameClassPairCallbackHandler handler = new AttributesMapperCallbackHandler(mapper);
		PagedResultsDirContextProcessor pagedProcessor = new PagedResultsDirContextProcessor(999);
		ldapTemplate.search(dnBase, filter.toString(), searchControls, handler, pagedProcessor);
		
		PagedResultsCookie cookie = pagedProcessor.getCookie();
		while(cookie != null) {
			byte[] cookieBytes = cookie.getCookie();
			if(cookieBytes == null)
				break;
			pagedProcessor = new PagedResultsDirContextProcessor(999, cookie);
			ldapTemplate.search(dnBase, filter.toString(), searchControls, handler, pagedProcessor);
			cookie = pagedProcessor.getCookie();
		}
		return handler.getList();
	}
	
	/**
	 * Sets the ldapTemplate
	 * @param ldapTemplate the ldap template to use for accessing the data store
	 */
	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}
}
