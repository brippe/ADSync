package com.ripware.apps.activedirectory.ldap;

import java.util.List;

import com.ripware.apps.activedirectory.ConsumerConfig;
import com.ripware.apps.activedirectory.data.CampusAbbr;
import org.apache.log4j.Logger;
import org.springframework.ldap.core.LdapTemplate;

import com.ripware.apps.activedirectory.LumListener;

/**
 * 
 * @author Brad Rippe 
 *
 */
public class GroupUtils {
	
	static Logger log = Logger.getLogger(GroupUtils.class.getName());

	public static void loadGroupsFromLDAP(LdapTemplate ldapTemplate,
                                          ConsumerConfig cConfig, LumListener listener) {
		GroupSearch groupSearch = new GroupSearch(ldapTemplate);
        log.info("Getting all group information from Active Directory");
        List<String> groups = groupSearch.getGroupNames(cConfig.getAGroupPath(), CampusAbbr.ACollege.toString());
        int size = groups.size();
        log.info(size + " groups from A College");
        groups.addAll(groupSearch.getGroupNames(cConfig.getBGroupPath(), CampusAbbr.BCollege.toString()));
        log.info(groups.size()-size + " groups from B College");
        size = groups.size();
        groups.addAll(groupSearch.getGroupNames(cConfig.getCGroupPath(), CampusAbbr.CCollege.toString()));
        log.info(groups.size()-size + " groups from CCollege");
        log.info("Retrieved " + groups.size() + " security groups");
        listener.setAdGroups(groups);
	}
}
