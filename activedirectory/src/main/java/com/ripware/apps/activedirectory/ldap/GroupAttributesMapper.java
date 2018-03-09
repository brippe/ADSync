package com.ripware.apps.activedirectory.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import com.ripware.apps.activedirectory.data.Person;
import org.springframework.ldap.core.AttributesMapper;

/**
 * Maps a group's Common Name (cn) to an attribute
 * For simplicity this mapper only maps a cn 
 * @author Brad Rippe 
 * @see Person
 */
public class GroupAttributesMapper implements AttributesMapper {

	/**
	 * Maps a CN to a person's logonID
	 * @return a person with the logonID set. If null, no Person exists
	 * 			in AD
	 */
	@Override
    public String mapFromAttributes(Attributes attributes) throws NamingException {         
        return (String) attributes.get("cn").get();        
    }
}
