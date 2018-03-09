package com.ripware.apps.activedirectory.ldap;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import org.springframework.ldap.core.AttributesMapper;
import com.ripware.apps.activedirectory.data.Person;

/**
 * Maps a user's Common Name (cn) to a person object
 * For simplicity this mapper only maps a cn to a LogonID in a Person
 * object. 
 * @author Brad Rippe 
 * @see Person
 */
public class UserAttributesMapper implements AttributesMapper {

	/**
	 * Maps a CN to a person's logonID
	 * @return a person with the logonID set. If null, no Person exists
	 * 			in AD
	 */
	@Override
    public Person mapFromAttributes(Attributes attributes) throws NamingException {
        Person p = new Person(); 
        String commonName = (String)attributes.get("cn").get();
        p.setLogonID(commonName); // retrieve banner id for simplicity        
        return p;
    }

}
