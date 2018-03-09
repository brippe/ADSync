package com.ripware.apps.activedirectory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.ripware.apps.activedirectory.cache.XMLMessageCache;
import com.ripware.apps.activedirectory.db.PersonDAO;
import com.ripware.apps.activedirectory.ldap.PersonSearch;
import com.ripware.apps.activedirectory.xml.XMLReader;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.ripware.apps.activedirectory.cache.EhcacheWrapper;
import com.ripware.apps.activedirectory.data.Assignment;
import com.ripware.apps.activedirectory.data.CampusAbbr;
import com.ripware.apps.activedirectory.data.CampusCodes;
import com.ripware.apps.activedirectory.data.CourseSectionGroup;
import com.ripware.apps.activedirectory.data.Enroll;
import com.ripware.apps.activedirectory.data.EnrollMember;
import com.ripware.apps.activedirectory.data.Group;
import com.ripware.apps.activedirectory.data.Person;
import com.ripware.apps.activedirectory.data.Roster;
import com.ripware.apps.activedirectory.db.CourseDAO;

/**
 * Responsible for listening to a luminis message queue. Luminis Message Broker contains
 * messages from Banner in regards to user changes; new accounts; adds and drops from courses.
 * This course subscribes to the com_sct_ldi_sis_Sync topic on the LMB and translates messages
 * into Active Directory accounts, creation of security groups, add/remove from those Active 
 * Directory groups.
 * 
 * <i>AD Tools for Win7 - <a href="http://www.microsoft.com/en-us/download/details.aspx?id=7887">AD Tools</a></i>
 * 
 * @author Brad Rippe 
 * @see ConsumerConfig
 */
public class LumListener implements MessageListener {
	
	static Logger log = Logger.getLogger(LumListener.class.getName());
	
	private ConsumerConfig config;
	private XMLMessageCache cache;
	private CourseDAO courseDAO;
	private PersonDAO personDAO;
	private PersonSearch personSearch;
	private EhcacheWrapper<String, Person> personCache;
	private List<String> adGroups;
	
	/**
	 * Creates a Luminis Message Queue listener
	 * @param config the configuration for this message listener
	 */
	public LumListener(ConsumerConfig config) {
		this.config = config;
		this.cache = null;
		this.courseDAO = null;
		this.personSearch = null;
		this.personCache = null;
		this.adGroups = null;
	}	
	
	/**
	 * This is the work that is done when a message is received.
	 * @param message the message received.
	 * @see javax.jms.MessageListener
	 */
	public void onMessage(Message message) {
		if (message instanceof TextMessage) {
            try {
            	String xml = ((TextMessage) message).getText();
            	log.info("********************************************");            	
            	
            	if(cache.isUseCache() && cache.isSameAsPreviousMessage(xml)) {
            		log.info("Same xml message as the last message. Skipping message");
            		return;
            	}            
            	
            	log.info(xml);
            	if(cache.isUseCache())
            		cache.setPreviousXmlMessage(xml); // set cached message            	
            	            	
            	XMLReader reader = new XMLReader();
            	Document doc = reader.getXMLDocument(xml);
            	processMessage(xml, reader, doc);            	
            	
            } catch (Exception ex) {
            	log.error(ex);
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalArgumentException("Message must be of type TextMessage");
        }		
	}

	/**
	 * Process the message received by LMB and executes the associated commands to 
	 * create users, add security groups, or add/remove members to those groups based
	 * on the message.
	 *  
	 * @param xml the message from Luminis Message Broker; this is an xml doc
	 * @param reader the xml reader
	 * @param doc the org.w3c.dom.Document representation of the xml document
	 * @see XMLReader
	 */
	private void processMessage(String xml, XMLReader reader, Document doc) {
		String cmd;
			
		if(xml.contains("<person>")) {
			
			log.info("Person Message");
			if(!config.isCreateOrModifyUsers())	{	// if configured not to create or modify users, do nothing requested by MC
				log.info("ADSync configured NOT to process user messages!");
				return;
			}
			Person person = reader.readPersonXMLMessage(doc);
			createUserWithPowershell(person);		// create or update user			
			
		} else if(xml.contains("<group>")) { // could be College, Department, Course, Term, Course Section, 
			
			log.info("Group Message");
			Group group = reader.readGroupXMLMessage(doc);
			
			if(group.getGroupType().equals("College")) {
				log.info("\tCollege Message");
			} else if(group.getGroupType().equals("Term")) {
				log.info("\tTerm Message");
			} else if(group.getGroupType().equals("Department")) {
				log.info("\tDepartment Message");
			} else if(group.getGroupType().equals("Course")) {
				log.info("\tCourse Message");
			// a security group needs to be created in Active Directory based on the course
			// section. Only create a group if the term is already in the college groups
			} else if(group.getGroupType().equals("CourseSection")) {
				log.info("\tCourse Section Message");				
				cmd = generateCourseSectionGroupCommand((CourseSectionGroup)group);
				if(cmd != null) {
					log.info("***************** Executing Powershell = " + cmd.toString());				
					execPowerShellCommand(cmd);				
				}
			}
			
		} else if(xml.contains("<group recstatus=\"3\">")) { // could be College, Department, Course, Term, Course Section
			
			log.info("Group Deletion Message");
			//Group group = reader.readDeleteGroupXMLMessage(doc);
			
		} else if(xml.contains("<membership>")) { 	// could be student enrollment or faculty
			
			log.info("Membership Message");
			/*if(reader.isNonGroupMessage(doc) && reader.isCrossListUpdateMessage(doc)) {	// cross list updates aren't group messages; format is different		    	
		 	   	log.info("Cross List Section Group Message");
		 	   //CrossListSectionMembership slsm = reader.readCrossListUpdateXMLMessage(doc);
		     } else*/
			
			// Check to see if the user is in AD, if not add user			
			boolean rosterAdd = true;			
			if (reader.isStudentEnrollmentMessage(doc)) {
		    	 log.info("Student Enrollment Message");
		    	 rosterAdd = !reader.isStudentDropMessage(doc);
		    	 
		    	 Enroll enroll;
		    	 if(rosterAdd)
		    		 enroll = reader.readEnrollXMLMessage(doc);
		    	 else
		    		 enroll = reader.readDeleteEnrollXMLMessage(doc);
		    	 		    	 
		    	 cmd = generateAddGroupMembersCommand(enroll, rosterAdd);
		    	 if(cmd != null) {
		    		 log.info("***************** Executing Powershell = " + cmd.toString());
		    		 execPowerShellCommand(cmd);
		    	 } else {
		    		 log.info("************ Inactive Student Enrollment record");
		    	 }
		    	 
		     } else if(reader.isFacultyAssignmentMessage(doc)) {
		    	 log.info("Faculty Assignment Message");
		    	 
		    	 rosterAdd = !reader.isFacultyDropMessage(doc);
		    	 
		    	 Assignment assign;
		    	 if(rosterAdd)
		    		 assign = reader.readAssignmentXMLMessage(doc);
		    	 else
		    		 assign = reader.readDeleteAssignXMLMessage(doc);
		    	 
		    	 cmd = generateAddGroupMembersCommand(assign, rosterAdd);
		    	 if(cmd != null) {
		    		 log.info("***************** Executing Powershell = " + cmd.toString());
		    		 execPowerShellCommand(cmd);
		    	 }
		    	 
		     } else {
		    	 log.info("Unknown message");
		     }			
		}
	}

	/**
	 * Returns the add user command to execute powershell script
	 * AddADUser.ps1 which is spawned into another process to create
	 * the AD account with powershell.
	 * @param person the user to create the account
	 * @return the command to execute the powershell script that creates an AD account
	 */
	private String generateAddUserCommand(Person person) {
		
		StringBuffer cmd = new StringBuffer(config.getAddCommand());
		cmd.append(person.getLogonID());
		cmd.append(" -p \"");
		cmd.append(person.getPassword());
		cmd.append("\" -c \"");
		cmd.append(person.getCity());
		cmd.append("\" -comp ");
		cmd.append("\"\"");
		cmd.append(" -dn \"");            	
		cmd.append(person.getDisplayName());
		cmd.append("\"");
		cmd.append(" -e \"");
		if(person.getEmail() != null && !person.getEmail().equals(""))
		   	cmd.append(person.getEmail());
		else
			cmd.append("noemailprovided@ripware.com");
		cmd.append("\" -bid \"");
		cmd.append(person.getLogonID());
		cmd.append("\" -dept ");
		cmd.append("\"\"");
		cmd.append(" -gn \"");
		cmd.append(person.getGivenName());
		cmd.append("\" -pc \"");
		cmd.append(person.getZipCode());
		cmd.append("\" -s \"");
		cmd.append(person.getState());
		cmd.append("\" -st \"");
		cmd.append(person.getStreet());
		cmd.append("\" -ln \"");
		
		// Some last names contain spaces. Only add the last name
		if(person.getFamilyName().contains(" ")) {
			cmd.append(person.getFamilyName().substring(person.getFamilyName().indexOf(' ')+1));
		} else {
			cmd.append(person.getFamilyName());
		}
		
		cmd.append("\" -r ");
		cmd.append(joinRoles(person.getLuminisRoles(), ","));
		
		// add path and domain
		cmd.append(" -pat \"");
		cmd.append(config.getPath());
		cmd.append("\" -dom \"");
		cmd.append(config.getDomain());
		cmd.append("\"");
		
		// determines if the user information will be added into AD or not
		cmd.append(" -ai ");
		if(config.isAddInfo())
			cmd.append("yes");
		else 
			cmd.append("no");
		cmd.append(" -m ");
		if(config.isMessaging())
			cmd.append("yes");
		else
			cmd.append("no");
				
		return cmd.toString();
	}
	
	/**
	 * Returns the add security group command to execute powershell script AddADGroup.ps1 
	 * which is spawned into another process to create the AD Security group associated with
	 * the course crn.
	 * @param group the group information for creating a security group in Active Directory
	 * @return the command to execute powershell script to create a security group
	 */
	private String generateCourseSectionGroupCommand(CourseSectionGroup group) {		
		//.\AddADGroup.ps1 -g FC-201220-020922 -p "OU=Groups,OU=BCollege,DC=Ripware,DC=local" -mb "CN=00001610,OU=People,DC=Ripware,DC=local"
		StringBuffer cmd = new StringBuffer(config.getAddGroupCommand());
		String term = group.getGroupID().substring(6);
		String crn = group.getGroupID().substring(0, 5);
		
		String campPre = determineCoursePrefix(term, crn);
		// can determine the course or the term isn't loaded into AD
		if(campPre == null)		// Term OU not in AD, do nothing
			return null;
		
		String groupPath = determineGroupPath(campPre);
		// CC-30025-201220
		String newGroup = campPre + term + "-" + crn;		
		cmd.append(newGroup);
		
		// add to cached list of groups; modification have to be synchronized due to the preload process
		synchronized (this) {
			adGroups.add(newGroup);
		}
		
		cmd.append(" -p \"");
		
		// As of 3/12/2014 CRN groups reside in a term OU ex. OU=201320		
		cmd.append("OU=");
		cmd.append(term);
		cmd.append(",");
		
		cmd.append(groupPath);
		// get managed by instructor		
		cmd.append("\" -mb \"CN=");
		String bannerId = personDAO.getPrimaryInstructorBannerId(term, crn);
		if(bannerId != null) {
			cmd.append(bannerId);
			cmd.append(",");
			cmd.append(config.getPath());
			cmd.append("\"");
		} else {
			cmd.append("\"");
		}
		// add description later
		// cmd.append(group.getLongDescription() + " " + group.getFullDesc());
		//cmd.append("\"");
		return cmd.toString();
	}
	
	/**
	 * Returns the add member to a security group command to execute powershell script AddADGroupMembers.ps1 
	 * which is spawned into another process to add members to an AD Security group.
	 * @param enroll the enrollment information of a course
	 * @param add true to generate an add command, false to generate a drop command
	 * @return the command to execute powershell script to add members to a security group
	 */
	private String generateAddGroupMembersCommand(Roster roster, boolean add) {
		StringBuffer cmd = new StringBuffer(config.getAddGroupMembersCommand());		
		// need to know what campus a crn is in		
		String cn = "CN=";
		String term = roster.getCourseId().substring(6);
		String crn = roster.getCourseId().substring(0, 5);
		String campPre = determineCoursePrefix(term, crn);
		if(campPre == null)		// Term OU not in AD, do nothing
			return null;
		
		String groupPath = determineGroupPath(campPre); 
		
		cmd.append("\"");
		cmd.append(cn);
		cmd.append(campPre);
		//30025.201220
		cmd.append(term);
		cmd.append("-");
		cmd.append(crn);
		cmd.append(",");
		// As of 3/12/2014 CRN groups reside in a term OU ex. OU=201320		
		cmd.append("OU=");
		cmd.append(term);
		cmd.append(",");
		cmd.append(groupPath);		
		cmd.append("\" -t \"");
		cmd.append(cn);
		cmd.append(campPre);
		cmd.append(term);
		cmd.append(",OU=");
		cmd.append(term);
		cmd.append(",");
		cmd.append(groupPath);
		cmd.append("\" -bid ");
		StringBuffer comma = new StringBuffer();
		
		for(EnrollMember em : roster.getMembers()) {
			//if(em.getStatus() == 0) // record is inactive
			//	return null;
			
			// if user isn't in LDAP/AD we need to create them
			// get the bannerid from the source id
			// let's assume that if the user is in the cache
			// they are in AD
			Person p = personCache.get(em.getSourceId());
			if(p != null) {
				log.info("Person found in cache " + p.getLogonID());
				em.setBannerId(p.getLogonID());
			} else {
				// if person in cache, we assume they are in AD
				em.setBannerId(personDAO.getBannerIDFromSourceId(em.getSourceId()));
				if(!userExists(em.getBannerId()) && config.isCreateOrModifyUsers()) {
					p = createUser(em.getBannerId());
					personCache.put(em.getSourceId(), p);
					log.info("PersonCache = " + personCache.size());
				} else if(!config.isCreateOrModifyUsers() && roster.getMembers().size() != 1) {
					continue;		// don't create a user and move to the next user
				}
			}
			
			cmd.append(comma);
			cmd.append("\"");
			cmd.append(em.getBannerId());		// what if more than 1 member
			cmd.append("\"");
			if(comma.length() == 0)
				comma.append(",");
		}
		
		if(add)
			cmd.append(" -a add");
		else
			cmd.append(" -a remove");
		
		cmd.append(" -m ");
		if(config.isMessaging())
			cmd.append("yes");
		else
			cmd.append("no");
		
		return cmd.toString();
	}
	
	private String determineGroupPath(String prefixPlusTerm) {
		if(prefixPlusTerm.startsWith(CampusAbbr.CCollege.toString()))
			return config.getCGroupPath();
		else if(prefixPlusTerm.startsWith(CampusAbbr.ACollege.toString()))
			return config.getAGroupPath();
		else if(prefixPlusTerm.startsWith(CampusAbbr.BCollege.toString()))
			return config.getBGroupPath();
		else
			return "";
	}
	
	private String determineCoursePrefix(String term, String crn) {
		String prefix;
		if(term.endsWith("5")) {			
			
			prefix = (new StringBuffer(CampusAbbr.CCollege.toString()).append("-")).toString();
			if(!getAdGroups().contains(prefix + term)) {		// checks if term OU is in AD this prevents terms
				log.info("CCollege " + term + " not loaded in AD");	// that aren't loaded from being processed
				return null;
			}
			return prefix;
			
		} else {
			
			if(!getAdGroups().contains(CampusAbbr.ACollege + "-" + term) &&
					!getAdGroups().contains(CampusAbbr.BCollege + "-" + term)) {
				log.info("A/B College " + term + " not loaded in AD");
				return null;
			}
			
			String campCode = courseDAO.getCampus(term, crn);
			log.debug("Registration for Campus Code = " + campCode);
			
			// determine if the course is a BCollege or CCollege course
			if(campCode.equals(CampusCodes.BCollege.toString())) {
				return (new StringBuffer(CampusAbbr.BCollege.toString()).append("-")).toString();
				//groupPath = config.getBGroupPath();
			} else if(campCode.equals(CampusCodes.CCollege.toString())) {
				return (new StringBuffer(CampusAbbr.CCollege.toString()).append("-")).toString();
				//groupPath = config.getAGroupPath();
			} else {
				return null;
			}
		}
	}

	/**
	 * Creates a separate process and executes the specified command
	 * @param cmd the command to be executed
	 */
	private void execPowerShellCommand(String cmd) {
		try {
			Runtime runtime = Runtime.getRuntime();
	    	Process proc = runtime.exec(cmd);
	    	proc.getOutputStream().close();
	    	InputStream inStream = proc.getInputStream();
	    	InputStreamReader isr = new InputStreamReader(inStream);
	    	BufferedReader br = new BufferedReader(isr);
	    	
	    	String line;
	    	// std out from powershell
	    	while((line = br.readLine()) != null) {
	    		log.info(line);
	    	}
	    	br.close();
	    	
	    	inStream = proc.getErrorStream();
	    	isr = new InputStreamReader(inStream);
	    	br = new BufferedReader(isr);
	    	
	    	// output error messages
	    	while((line = br.readLine()) != null) {
	    		log.info(line);
	    	}	    	
	    	br.close();
	    	
		} catch(Exception ex) {
			log.error(ex);
			ex.printStackTrace();
		}
	}

	/**
	 * Retrieves the xml message cache
	 * @return the xml message cache
	 */
	public XMLMessageCache getCache() {
		return cache;
	}

	/**
	 * Sets the xml message cache
	 * @param cache the cache to be used
	 */
	public void setCache(XMLMessageCache cache) {
		this.cache = cache;
	}

	/**
	 * Retrieves the course data access object
	 * @return the course data access object
	 */
	public CourseDAO getCourseDAO() {
		return courseDAO;
	}

	/**
	 * Sets the course data access object
	 * @param cDAO the course data access object
	 */
	public void setCourseDAO(CourseDAO cDAO) {
		courseDAO = cDAO;
	}
	
	/**
	 * Retrieves the person data access object
	 * @return the person data access object
	 */
	public PersonDAO getPersonDAO() {
		return personDAO;
	}

	/**
	 * Sets the person data access object
	 * @param cDAO the person data access object
	 */
	public void setPersonDAO(PersonDAO pDAO) {
		personDAO = pDAO;
	}
	
	/**
	 * Sets the person search
	 * @param personSearch the person search
	 */
	public void setPersonSearch(PersonSearch personSearch) {
		this.personSearch = personSearch;
	}
	
	/**
	 * Creates a delimiter separated string from a list of roles
	 * Roles are specified like: Faculty, Student@BCollege, FinAid
	 * @param roles list of roles 
	 * @param delim the delimiter
	 * @return a string of all the roles delimited by the specified delimiter
	 */
	public String joinRoles(List<String> roles, String delim) {
		StringBuilder sb = new StringBuilder();
		String loopDelim = "";
		
		for(String r : roles) {
			if(r.equalsIgnoreCase("addisable") ||
					r.equalsIgnoreCase("adpopulate"))
				continue;
			sb.append(loopDelim);
			sb.append(r);
			
			loopDelim = delim;
		}
		if(sb.toString().equals(""))
			return "\"\"";
		
		return sb.toString();
	}
		
	/**
	 * Returns true if the person associated with the banner id
	 * is in the LDAP server, otherwise false
	 * @param bannerID the banner id of the person
	 * @return true if in LDAP, otherwise false
	 */
	public boolean userExists(String bannerID) {
		log.info("Checking if user exists " + bannerID + " in AD");
		List<Person> people = personSearch.getPerson(config.getPath(), bannerID);
		log.info("Found " + people.size());
		if(people.size() == 0)
			return false;
		return true;
	}
	
	/**
	 * Queries the backend database for person information of the specified
	 * person and creates that person in AD. 
	 * @param bannerID the banner id of the person needed in LDAP
	 */
	public Person createUser(String bannerID) {
		Person p = personDAO.getPerson(bannerID);
		if(p != null) {
			ArrayList<String> roles = new ArrayList<String>();
			roles.add("Student");
			p.setluminisRoles(roles);
			createUserWithPowershell(p);
		} else
			log.info("Couldn't find person = " + bannerID + " in db!!!");
		return p;
	}
	
	/**
	 * Executes the powershell script to create a user in AD
	 * @param person 
	 */
	private void createUserWithPowershell(Person person) {		
		String cmd;
		cmd = generateAddUserCommand(person);
		log.info("***************** Executing Powershell = " + cmd.toString());
		execPowerShellCommand(cmd);
	}

	/**
	 * Returns the person cache, this avoids going to ldap every time to see if a person exists in AD
	 * @return the personCache
	 */
	public EhcacheWrapper<String, Person> getPersonCache() {
		return personCache;
	}

	/**
	 * Sets the person cached to avoid going to AD to check if persons exist
	 * @param personCache the personCache to set
	 */
	public void setPersonCache(EhcacheWrapper<String, Person> personCache) {
		this.personCache = personCache;
	}

	/**
	 * Gets the groups that are in Active Directory for all three institutions
	 * @return the adGroups the names of the groups in AD
	 */
	public synchronized List<String> getAdGroups() {
		return adGroups;
	}

	/**
	 * Sets the active directory group names that exist
	 * @param groups the adGroup names
	 */
	public synchronized void setAdGroups(List<String> groups) {
		this.adGroups = groups;
	}
}
