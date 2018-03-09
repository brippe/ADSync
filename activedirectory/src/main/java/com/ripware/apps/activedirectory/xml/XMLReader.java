package com.ripware.apps.activedirectory.xml;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.ripware.apps.activedirectory.data.Assignment;
import com.ripware.apps.activedirectory.data.CollegeGroup;
import com.ripware.apps.activedirectory.data.CourseGroup;
import com.ripware.apps.activedirectory.data.CourseSectionGroup;
import com.ripware.apps.activedirectory.data.CrossListSectionMembership;
import com.ripware.apps.activedirectory.data.Enroll;
import com.ripware.apps.activedirectory.data.EnrollMember;
import com.ripware.apps.activedirectory.data.GroupFactory;
import com.ripware.apps.activedirectory.data.Member;
import com.ripware.apps.activedirectory.data.Person;
import com.ripware.apps.activedirectory.data.Relationship;
import com.ripware.apps.activedirectory.data.Role;
import com.ripware.apps.activedirectory.data.Group;
import com.ripware.apps.activedirectory.data.TermGroup;

/**
 * Reader for xml messages in the Luminis Broker. This reader handles all messages described in
 * the <i>Banner Integration for eLearning 8.0 Message Reference Guide</i>.
 * @author Brad Rippe 
 */
public class XMLReader extends XMLUtils {
	
	Logger log = Logger.getLogger(XMLReader.class.getClass());
	
	/**
	 * Reads a Person data message
	 * @param doc the person data message 
	 * @return the person information contained in the message
	 */
	public Person readPersonXMLMessage(Document doc) {
		try {		    
		    Node enterprise = getEnterpriseNodeFromDoc(doc);		    
		    Person pers = new Person();
		    
		    Node person = getNode("person", enterprise.getChildNodes());
		    pers.setRecStatus(getNodeAttr("recstatus", person)); // 3 = delete person = AD disable account
		    NodeList personData = person.getChildNodes();
		    pers.setEmail(getNodeValue("email", personData));
		    
		    Node tel = getNode("tel", personData);
		    if(tel != null) {
		    	String teleType = getNodeAttr("teltype", tel);
		    	if(teleType != null) {
		    		if(teleType.equals("1")) {
		    			pers.setVoiceTele(getNodeValue(tel));		    			
		    		} else if(teleType.equals("2")) {
		    			pers.setFaxTele(getNodeValue(tel));
		    		} else if(teleType.equals("3")) {
		    			pers.setMobileTele(getNodeValue(tel));
		    		} else if(teleType.equals("4")) {
		    			pers.setPagerTele(getNodeValue(tel));
		    		}
		    	}
		    }
		    		    
		    for(int x = 0; x < personData.getLength(); x++) {
		    	Node node = personData.item(x);
		    	if(node.getNodeName().equalsIgnoreCase("userid")){		    		
		    		String type = getNodeAttr("useridtype", node);
		    		
		    		//log.info("Type userid " + getNodeAttr("useridtype", node));
		    		//log.info("userid " + getNodeValue(node));
		    		
		    		if(type.equals("Logon ID")){
		    			pers.setLogonID(getNodeValue(node));
		    			pers.setPassword(getNodeAttr("password", node));
		    			pers.setLogonEncryptType(getNodeAttr("pwencryptiontype", node)); // returns "" if none found
		    		} else if(type.equals("SCTID")) {
		    			pers.setSctID(getNodeValue(node));
		    			pers.setPassword(getNodeAttr("password", node));
		    			pers.setSctEncryptType(getNodeAttr("pwencryptiontype", node)); // returns "" if none found
		    		} else if(type.equals("Email ID")) {
		    			pers.setEmailID(getNodeValue(node));
		    		} else if (type.equals("UDCIdentifier")) {
		    			pers.setUdcIdentifier(getNodeValue(node));
		    		}
		    	}
		    }
		    
		    Node name = getNode("name", personData);
		    Node fn = getNode("fn", name.getChildNodes());
		    pers.setDisplayName(getNodeValue(fn));
		    
		    Node preferredName = getNode("nickname", name.getChildNodes());
		    if(preferredName != null)
		    	pers.setPreferredName(getNodeValue(preferredName));
		    Node n = getNode("n", name.getChildNodes());
		    Node family = getNode("family", n.getChildNodes());
		    pers.setFamilyName(getNodeValue(family));
		    Node given = getNode("given", n.getChildNodes());
		    pers.setGivenName(getNodeValue(given));
		    
		    Node prefix = getNode("prefix", n.getChildNodes());
		    if(prefix != null)
		    	pers.setPrefix(getNodeValue(prefix));
		    
		    Node suffix = getNode("suffix", n.getChildNodes());
		    if(suffix != null)
		    	pers.setSuffix(getNodeValue(suffix));
		    
		    Node partname = getNode("partname", n.getChildNodes());
		    if(partname != null && getNodeAttr("partnametype", partname) != null &&
		    		getNodeAttr("partnametype", partname).equals("MiddleName")) {		    	
		    	pers.setMiddleName(getNodeValue(partname));
		    }
		    
		    Node demographics = getNode("demographics", personData);
		    Node gender = getNode("gender", demographics.getChildNodes());
		    pers.setGender(getNodeValue(gender));
		    
		    Node adr = getNode("adr", personData);
		    if(adr != null) {
			    Node street = getNode("street", adr.getChildNodes());
			    pers.setStreet(getNodeValue(street));
			    Node locality = getNode("locality", adr.getChildNodes());
			    pers.setCity(getNodeValue(locality));
			    Node region = getNode("region", adr.getChildNodes());
			    pers.setState(getNodeValue(region));
			    Node pcode = getNode("pcode", adr.getChildNodes());
			    pers.setZipCode(getNodeValue(pcode));
		    } else {
		    	log.debug("No address for " + pers.getDisplayName());
		    }
		    		    
		    for(int x = 0; x < personData.getLength(); x++) {
		    	Node node = personData.item(x);
		    	if(node.getNodeName().equalsIgnoreCase("institutionrole")){
		    		Role role = new Role();
		    		role.setPrimaryrole(getNodeAttr("primaryrole", node));
		    		role.setInstitutionroletype(getNodeAttr("institutionroletype", node));
		    		log.info("Person is a " + role.getInstitutionroletype());
		    		pers.getInstitutionRoles().add(role);
		    	}
		    }
		    		    
		    Node extension = getNode("extension", personData);		    
		    Node luminisperson = getNode("luminisperson", extension.getChildNodes());
		    NodeList rols = luminisperson.getChildNodes();		    
		    for(int x = 0; x < rols.getLength(); x++) {
		    	Node node = rols.item(x);
		    	// maybe more than one major
		    	if(node.getNodeName().equalsIgnoreCase("academicmajor")) {
		    		pers.setMajor(getNodeValue(node));
		    	} else if(node.getNodeName().equalsIgnoreCase("academictitle")) {	// faculty member
		    		pers.setAcademicTitle(getNodeValue(node));
		    	} else if(node.getNodeName().equalsIgnoreCase("academicdegree")) {	// faculty member
		    		pers.setAcademicDegree(getNodeValue(node));
		    	} else if(node.getNodeName().equalsIgnoreCase("customrole")) {
		    		pers.getLuminisRoles().add(getNodeValue(node));
		    	} 
		    }
		    log.info("Processing "+pers.getDisplayName());
		    return pers;
		    
		} catch ( Exception e ) {
			log.error(e);
		    e.printStackTrace();
		    return null;
		}
	}
	
	/**
	 * Reads a group message. Group messages are sent for College, Department,
	 * Course, and Term data. 
	 * @param doc the group data message 
	 * @return a Group based on the type of message where it's for College, Department, Course,
	 * 			or Term data
	 */
	public Group readGroupXMLMessage(Document doc) {
		try {		    
		    Node enterprise = getEnterpriseNodeFromDoc(doc);		    
		    log.info("Group Message");
		    return parseGroupMessage(enterprise);
		} catch ( Exception e ) {
			log.error(e);
			e.printStackTrace();
		    return null;
		}		
	}
	
	/**
	 * Reads a cross list section membership message. 
	 * @param doc the cross list section membership
	 * @return the cross list section membership message content.
	 */
	public CrossListSectionMembership readCrossListUpdateXMLMessage(Document doc) {
		try {		    
		    Node enterprise = getEnterpriseNodeFromDoc(doc);
		    log.info("Cross List Section Update Message");
		    return parseCrossListUpdateMessage(enterprise);		// still returns a group
		} catch ( Exception e ) {
			log.error(e);
			e.printStackTrace();
		    return null;
		}	
	}
	
	/**
	 * Reads a student enrollment message
	 * @param doc the student enrollment message 
	 * @return the student enrollment message content.
	 */
	public Enroll readEnrollXMLMessage(Document doc) {
		try {		    
		    Node enterprise = getEnterpriseNodeFromDoc(doc);
		    log.info("Student Enrollment Message");
		    return parseStudentEnrollmentMessage(enterprise);
		} catch ( Exception e ) {
			log.error(e);
			e.printStackTrace();
		    return null;
		}
	}
	
	/**
	 * Reads a faculty assignment message
	 * @param doc the faculty assignment message
	 * @return the faculty assignment message content
	 */
	public Assignment readAssignmentXMLMessage(Document doc) {
		try {		    
		    Node enterprise = getEnterpriseNodeFromDoc(doc);
		    log.info("Faculty Assignment Message");
		    return parseFacultyAssignmentMessage(enterprise);
		} catch ( Exception e ) {
			log.error(e);
			e.printStackTrace();
		    return null;
		}
	}
	
	/**
	 * Reads a message to delete a group. Group messages are sent for College, Department,
	 * Course, and Term data. 
	 * @param doc the group delete message
	 * @return group information for the group to be deleted
	 */
	public Group readDeleteGroupXMLMessage(Document doc) {
		try {
		    Node enterprise = getEnterpriseNodeFromDoc(doc); // change to message	    
		    
		    CollegeGroup cg = new CollegeGroup();
		    Node group = getNode("group", enterprise.getChildNodes());
		    cg.setRecStatus(getNodeAttr("recstatus", group));
		    NodeList groupData = group.getChildNodes();
		    Node sourcedid = getNode("sourcedid", groupData);
		    Node id = getNode("id", sourcedid.getChildNodes());
		    cg.setGroupID(getNodeValue(id));
		    		    
		    Node description = getNode("description", groupData);		        
		    Node shortDesc = getNode("short", description.getChildNodes());		    
		    cg.setShortDescription(getNodeValue(shortDesc));
		 
		    return cg;

		} catch ( Exception e ) {
			log.error(e);
		    e.printStackTrace();
		    return null;
		}		
	}
	
	/**
	 * Reads a message to delete student enrollment 
	 * @param doc the student enrollment deletion message
	 * @return the enrollment information of the student to be deleted
	 */
	public Enroll readDeleteEnrollXMLMessage(Document doc) {
		try {
			Node enterprise = getEnterpriseNodeFromDoc(doc); // change to message
			
			Node membership = getNode("membership", enterprise.getChildNodes());	// adds to cross list sections don't have a group tag
			NodeList membershipData = membership.getChildNodes();
			Node sourcedid = getNode("sourcedid", membershipData);
			Node id = getNode("id", sourcedid.getChildNodes());		    
			
			Enroll enroll = new Enroll(); 
			enroll.setCourseId(getNodeValue(id));
			log.info("Delete enrollment for Course ID: " + enroll.getCourseId());
			
			for(int x = 0; x < membershipData.getLength(); x++) {
		    	Node node = membershipData.item(x);
		    	if(node.getNodeName().equalsIgnoreCase("member")){
		    		EnrollMember member = new EnrollMember();
		    		member.setSourceId(getNodeValue(getNode("id", getNode("sourcedid", node.getChildNodes()).getChildNodes())));
		    		member.setIdType(Integer.parseInt(getNodeValue(getNode("idtype", node.getChildNodes()))));
		    		member.setStatus(Integer.parseInt(getNodeValue(getNode("status", getNode("role", node.getChildNodes()).getChildNodes()))));
		    		Node role = getNode("role", node.getChildNodes());
		    		member.setRecStatus(getNodeAttr("recstatus", role));
		    		
		    		log.info("Source Id " + member.getSourceId());
		    		log.info("IdType " + member.getIdType());
		    		log.info("Status " + member.getStatus());
		    		log.info("RecStatus " + member.getRecStatus());
		    		
		    		enroll.getMembers().add(member);
		    	}
		    }		
			return enroll;

		} catch ( Exception e ) {
			log.error(e);
		    e.printStackTrace();
		    return null;
		}		
	}
	
	/**
	 * Reads a message to delete faculty assignment 
	 * @param doc the faculty assignment deletion message
	 * @return the assignment information of the faculty member to be deleted
	 */	
	public Assignment readDeleteAssignXMLMessage(Document doc) {
		try {
			Node enterprise = getEnterpriseNodeFromDoc(doc); // change to message
			
			Node membership = getNode("membership", enterprise.getChildNodes());	// adds to cross list sections don't have a group tag
			NodeList membershipData = membership.getChildNodes();
			Node sourcedid = getNode("sourcedid", membershipData);
			Node id = getNode("id", sourcedid.getChildNodes());		    
			
			Assignment assign = new Assignment(); 
			assign.setCourseId(getNodeValue(id));
			log.info("Delete assignment for Course ID: " + assign.getCourseId());
			
			for(int x = 0; x < membershipData.getLength(); x++) {
		    	Node node = membershipData.item(x);
		    	if(node.getNodeName().equalsIgnoreCase("member")){
		    		EnrollMember member = new EnrollMember();
		    		member.setSourceId(getNodeValue(getNode("id", getNode("sourcedid", node.getChildNodes()).getChildNodes())));
		    		member.setIdType(Integer.parseInt(getNodeValue(getNode("idtype", node.getChildNodes()))));
		    		Node role = getNode("role", node.getChildNodes());
		    		member.setStatus(Integer.parseInt(getNodeValue(getNode("status", role.getChildNodes()))));		    		
		    		member.setRecStatus(getNodeAttr("recstatus", role));
		    		
		    		log.info("Faculty id " + member.getSourceId());
		    		log.info("IdType " + member.getIdType());
		    		log.info("Status " + member.getStatus());
		    		log.info("RecStatus " + member.getRecStatus());
		    		
		    		assign.getMembers().add(member);
		    	}
		    }		
			return assign;

		} catch ( Exception e ) {
			log.error(e);
		    e.printStackTrace();
		    return null;
		}		
	}
	
	/**
	 * Determines if the message is a group message
	 * @param doc the message
	 * @return true if a group message, otherwise false
	 */
	public boolean isNonGroupMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
	    for(int i = 0; i < children.getLength(); i++) {
	    	Node n = children.item(i);
	    	if(n.getNodeType() !=  1)
	    		continue;
	    	if(n.getNodeName() == "membership")		// this could also be student enrollment; or faculty assignment
	    		return true;	    	
	    }
	    return false;
	}
	
	/**
	 * Determines if the message is an enrollment message
	 * @param doc the message
	 * @return true if an enrollment message, otherwise false
	 */
	public boolean isStudentEnrollmentMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
	    for(int i = 0; i < children.getLength(); i++) {
	    	Node n = children.item(i);
	    	if(n.getNodeType() !=  1)
	    		continue;
	    	if(n.getNodeName().equals("membership")) {		// this could also be student enrollment; or faculty assignment
	    		Node role = getNode("role", getNode("member", n.getChildNodes()).getChildNodes());
	    		//log.info("Role type = " + getNodeAttr("roletype", role));
	    		if(role != null && getNodeAttr("roletype", role).equals("01"))
	    			return true;
	    	}
	    }
	    return false;
	}
	
	/**
	 * Determines if the message is an student drop message
	 * @param doc the message
	 * @return true if an drop enrollment message, otherwise false
	 */
	public boolean isStudentDropMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
	    for(int i = 0; i < children.getLength(); i++) {
	    	Node n = children.item(i);
	    	if(n.getNodeType() !=  1)
	    		continue;
	    	if(n.getNodeName().equals("membership")) {		// this could also be student enrollment; or faculty assignment
	    		Node role = getNode("role", getNode("member", n.getChildNodes()).getChildNodes());
	    		String status = getNodeValue(getNode("status", role.getChildNodes()));
	    		//log.info("Role type = " + getNodeAttr("roletype", role));
	    		if(role != null && getNodeAttr("roletype", role).equals("01") &&
	    				(getNodeAttr("recstatus", role).equals("3") || status.equals("0")))
	    			// drop by deletion of enrollment record = 3
	    			// drop by status of the enrollment record being inactive
	    			return true;
	    	}
	    }
	    return false;
	}
	
	/**
	 * Determines if the message is a faculty assignment message
	 * @param doc the message
	 * @return true if a faculty assignment message, otherwise false
	 */
	public boolean isFacultyAssignmentMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
	    for(int i = 0; i < children.getLength(); i++) {
	    	Node n = children.item(i);
	    	if(n.getNodeType() !=  1)
	    		continue;
	    	if(n.getNodeName().equals("membership")) {		// this could also be student enrollment; or faculty assignment
	    		Node role = getNode("role", getNode("member", n.getChildNodes()).getChildNodes());
	    		//log.info("Role type = " + getNodeAttr("roletype", role));
	    		if(role != null && getNodeAttr("roletype", role).equals("02"))
	    			return true;
	    	}
	    }
	    return false;
	}
	
	/**
	 * Determines if the message is a drop faculty assignment message
	 * @param doc the message
	 * @return true if a drop faculty assignment message, otherwise false
	 */
	public boolean isFacultyDropMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
	    for(int i = 0; i < children.getLength(); i++) {
	    	Node n = children.item(i);
	    	if(n.getNodeType() !=  1)
	    		continue;
	    	if(n.getNodeName().equals("membership")) {		// this could also be student enrollment; or faculty assignment
	    		Node role = getNode("role", getNode("member", n.getChildNodes()).getChildNodes());
	    		//log.info("Role type = " + getNodeAttr("roletype", role));
	    		if(role != null && getNodeAttr("roletype", role).equals("02") &&
	    				getNodeAttr("recstatus", role).equals("3"))			// drop = 3
	    			return true;
	    	}
	    }
	    return false;
	}
	
	/**
	 * Determines if the message is a cross list section message
	 * @param doc the message
	 * @return true if a cross list section message, otherwise false
	 */
	public boolean isCrossListUpdateMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
	    for(int i = 0; i < children.getLength(); i++) {
	    	Node n = children.item(i);
	    	if(n.getNodeType() !=  1)
	    		continue;
	    	if(n.getNodeName().equals("membership")) {		// this could also be student enrollment; or faculty assignment
	    		Node role = getNode("role", getNode("member", n.getChildNodes()).getChildNodes());
	    		//log.info("Role type = " + getNodeAttr("roletype", role));
	    		if(role != null && getNodeAttr("roletype", role) == null)
	    			return true;	    		
	    	}
	    }
	    return false;
	}
	
	/**
	 * Determines if the message is a delete message for a group
	 * @param doc the message
	 * @return true if a group delete message, otherwise false
	 */
	public boolean isGroupDeleteMessage(Document doc) {
		Node enterprise = getEnterpriseNodeFromDoc(doc);
		NodeList children = enterprise.getChildNodes();
		Node group = getNode("group", children);
		String recStatus = getNodeAttr("recstatus", group);
		if(recStatus != null && recStatus.equals("3"))
			return true;
		return false;
	}
	
	/**
	 * Creates a Document from an xml message
	 * @param xmlMessage the xml to create the document from
	 * @return the document of the xml
	 * @throws ParserConfigurationException if a configuration error occurred
	 * @throws SAXException if a sax error has occurred
	 * @throws IOException if an i/o error has occurred
	 */
	public Document getXMLDocument(String xmlMessage)  throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    DocumentBuilder docBuilder = dbf.newDocumentBuilder();
	    InputSource is = new InputSource();
	    is.setCharacterStream(new StringReader(xmlMessage));
	    
	    //Document doc = docBuilder.parse(new File(fileName));
	    Document doc = docBuilder.parse(is);
	    doc.getDocumentElement().normalize();
	    return doc;
	}
	
	/**
	 * Returns the enterprise node from a document. The enterprise element is the root element for all
	 * ldisp 2.0 messages
	 * @param doc the document to get the enterprise element from
	 * @return the enterprise element from an xml document
	 */
	private Node getEnterpriseNodeFromDoc(Document doc) {				    
	    NodeList nodeLst = doc.getElementsByTagName("enterprise"); 	// 1st child
	    return nodeLst.item(0);
	}	
	
	/**
	 * Parses a group message
	 * @param enterprise the root node of the xml message
	 * @return the group content from the message
	 */
	private Group parseGroupMessage(Node enterprise) {		
		Node group = getNode("group", enterprise.getChildNodes());	// adds to cross list sections don't have a group tag
		NodeList groupData = group.getChildNodes();
		Node sourcedid = getNode("sourcedid", groupData);
		Node id = getNode("id", sourcedid.getChildNodes());		    
		
		Node grouptype = getNode("grouptype", groupData);		    
		Node scheme = getNode("scheme", grouptype.getChildNodes());
		Node typevalue = getNode("typevalue", grouptype.getChildNodes());
		String typeOfGroup = getNodeValue(typevalue);
				    
		Node description = getNode("description", groupData);		        
		Node shortDesc = getNode("short", description.getChildNodes());
		Node longDesc = getNode("long", description.getChildNodes());
		
		log.info("Message Type = " + typeOfGroup);
		Group cg = GroupFactory.instanceOfGroup(typeOfGroup);	// returns the proper group
		
		cg.setGroupID(getNodeValue(id));
		cg.setScheme(getNodeValue(scheme));
		cg.setGroupType(getNodeValue(typevalue));
		cg.setTypeValueLevel(getNodeAttr("level", typevalue));
		cg.setShortDescription(getNodeValue(shortDesc));
		    		    
		if(typeOfGroup.equals("Term")) {		
			Node timeframe = getNode("timeframe", groupData);
			Node enrollcontrol = getNode("enrollcontrol", groupData);
			Node begin = getNode("begin", timeframe.getChildNodes()); // there's a restrict attr that if set 0 states that the student can't participate before the beginning date
		    Node end = getNode("end", timeframe.getChildNodes());	// again restrict attr here
		    Node enrollaccept = getNode("enrollaccept", enrollcontrol.getChildNodes());
		    TermGroup tg = (TermGroup) cg;
		    tg.setBegin(getNodeValue(begin));
		    tg.setEnd(getNodeValue(end));
		    try {
		    	tg.setEnrollAccept(Integer.parseInt(getNodeValue(enrollaccept)));
		    } catch(NumberFormatException nfe) {
		    	tg.setEnrollAccept(0);
		    }			    
		}
		
		if(typeOfGroup.equals("CourseSection")) {
			
			CourseSectionGroup csg = null;
			try {					
				Node fullDesc = getNode("full", description.getChildNodes());
				Node org = getNode("org", groupData);
				Node orgUnit = getNode("orgunit", org.getChildNodes());
				
				csg = (CourseSectionGroup) cg;
				csg.setFullDesc(getNodeValue(fullDesc));
				csg.setOrg(getNodeValue(orgUnit));
			
			
		    	// these tags determine if the message is targeted for the LMS
		    	// updates to a section targeted for the LMS require a deletion of the
		    	// old section and creation of a new section; these tags may or may not be present
		    	Node extension = getNode("extension", groupData);
		    	Node luminisgroup = getNode("luminisgroup", extension.getChildNodes());
		    	Node deliverysystem = getNode("deliverysystem", luminisgroup.getChildNodes());
		    	if(deliverysystem != null)
		    		csg.setDeliverySystem(getNodeValue(deliverysystem));
		    	Node events = getNode("events", luminisgroup.getChildNodes());
		    	if(events != null) {
		    		Node recurringevent = getNode("recurringevent", events.getChildNodes());
		    		csg.setEventDescription(getNodeValue(getNode("eventdescription", recurringevent.getChildNodes())));
		    		csg.setBeginDate(getNodeValue(getNode("begindate", recurringevent.getChildNodes())));
		    		csg.setEndDate(getNodeValue(getNode("enddate", recurringevent.getChildNodes())));
		    		csg.setDaysOfWeek(getNodeValue(getNode("daysofweek", recurringevent.getChildNodes())));
		    		csg.setBeginTime(getNodeValue(getNode("begintime", recurringevent.getChildNodes())));
		    		csg.setEndTime(getNodeValue(getNode("endtime", recurringevent.getChildNodes())));
		    		csg.setLocation(getNodeValue(getNode("location", recurringevent.getChildNodes())));
		    	}
		    	
			} catch(Exception e) {
				log.info("CourseSection additional LMS tags not present. This is ok!");
				// not targeted for the LMS				
				csg.setDeliverySystem(null);
			}
		}
		
		if(typeOfGroup.equals("Course") || typeOfGroup.equals("CourseSection")) {			
			CourseGroup cg2 = (CourseGroup) cg;
			for(int x = 0; x < groupData.getLength(); x++) {
		    	Node node = groupData.item(x);
		    	if(node.getNodeName().equalsIgnoreCase("relationship")){
		    		Relationship relationship = new Relationship();
		    		relationship.setRelation(getNodeAttr("relation", node));
		    		relationship.setRelationshipID(getNodeValue(getNode("id", getNode("sourcedid", node.getChildNodes()).getChildNodes())));
		    		relationship.setLabel(getNodeValue(getNode("label", node.getChildNodes())));
		    		cg2.getRelationships().add(relationship);
		    	}
		    }
		}
		
		if(!typeOfGroup.equals("CrossListedSection")) {
			cg.setLongDescription(getNodeValue(longDesc));			// not long desc in cross list section group
		}
		return cg;
	}
	
	/**
	 * Parses a cross list section membership message
	 * @param enterprise the root node of the xml message
	 * @return the cross list section content from the message
	 */
	private CrossListSectionMembership parseCrossListUpdateMessage(Node enterprise) {
		Node membership = getNode("membership", enterprise.getChildNodes());	// adds to cross list sections don't have a group tag
		NodeList membershipData = membership.getChildNodes();
		Node sourcedid = getNode("sourcedid", membershipData);
		Node id = getNode("id", sourcedid.getChildNodes());		    
		
		CrossListSectionMembership clsm = new CrossListSectionMembership(); 
		clsm.setId(getNodeValue(id));
		
		for(int x = 0; x < membershipData.getLength(); x++) {
	    	Node node = membershipData.item(x);
	    	if(node.getNodeName().equalsIgnoreCase("member")){
	    		Member member = new Member();
	    		member.setId(getNodeValue(getNode("id", getNode("sourcedid", node.getChildNodes()).getChildNodes())));
	    		member.setIdType(Integer.parseInt(getNodeValue(getNode("idtype", node.getChildNodes()))));
	    		member.setStatus(Integer.parseInt(getNodeValue(getNode("status", getNode("role", node.getChildNodes()).getChildNodes()))));
	    		member.setRecStatus(getNodeAttr("recstatus", getNode("role", node.getChildNodes())));
	    		clsm.getMembers().add(member);
	    	}
	    }
		
		return clsm;
	}

	/**
	 * Parses a student enrollment message
	 * @param enterprise the root node of the xml message
	 * @return the student enrollment content from the message
	 */
	private Enroll parseStudentEnrollmentMessage(Node enterprise) {
		Node membership = getNode("membership", enterprise.getChildNodes());	// adds to cross list sections don't have a group tag
		NodeList membershipData = membership.getChildNodes();
		Node sourcedid = getNode("sourcedid", membershipData);
		Node id = getNode("id", sourcedid.getChildNodes());		    
		
		Enroll enroll = new Enroll(); 
		enroll.setCourseId(getNodeValue(id));
		log.info("Enrollment for Course ID: " + enroll.getCourseId());
		
		for(int x = 0; x < membershipData.getLength(); x++) {
	    	Node node = membershipData.item(x);
	    	if(node.getNodeName().equalsIgnoreCase("member")){
	    		EnrollMember member = new EnrollMember();
	    		member.setSourceId(getNodeValue(getNode("id", getNode("sourcedid", node.getChildNodes()).getChildNodes())));
	    		member.setIdType(Integer.parseInt(getNodeValue(getNode("idtype", node.getChildNodes()))));
	    		Node role = getNode("role", node.getChildNodes());
	    		member.setStatus(Integer.parseInt(getNodeValue(getNode("status", role.getChildNodes()))));
	    		
	    		//member.setRecStatus(getNodeAttr("recstatus", role)); only used for delete message
	    		member.setRoleType(getNodeAttr("roletype", role)); // roletype = 01 ; student enrollment
	    		
	    		Node timeframe = getNode("timeframe", role.getChildNodes());
	    		Node begin = getNode("begin", timeframe.getChildNodes());
	    		Node end = getNode("end", timeframe.getChildNodes());
	    		member.setBeginEnroll(getNodeValue(begin));
	    		member.setBeginRestricted(Integer.parseInt(getNodeAttr("restrict", end)));
	    		member.setEndEnroll(getNodeValue(end));
	    		member.setEndRestricted(Integer.parseInt(getNodeAttr("restrict", end)));
	    		
	    		NodeList roleChilds = role.getChildNodes();
	    		for(int y = 0; y < roleChilds.getLength(); y++) {
	    			Node n = roleChilds.item(y);
	    			if(n.getNodeName().equals("intermresult")) {	    				
	    	    		Node midMode = getNode("mode", n.getChildNodes());
	    	    		if(midMode != null)
	    	    			member.setMidTermGradingMode(getNodeValue(midMode));
	    			}
	    		}
	    		
	    		Node finalresult = getNode("finalresult", role.getChildNodes());
	    		if(finalresult != null) {
	    			Node finalMode = getNode("mode", finalresult.getChildNodes());	    			
	    			if(finalMode != null)
	    				member.setFinalGradeMode(getNodeValue(finalMode));
	    		}
	    		
	    		log.info("Event Source ID " + member.getSourceId());
	    		log.info("IdType " + member.getIdType());
	    		log.info("Status " + member.getStatus());
	    		log.info("RecStatus " + member.getRecStatus());
	    		log.info("Role Type " + member.getRoleType());
	    		log.info("Begin Time " + member.getBeginEnroll());
	    		log.info("Begin Restrict " + member.getBeginRestricted());
	    		log.info("End Time " + member.getEndEnroll());
	    		log.info("End Restrict " + member.getEndRestricted());
	    		log.info("Mid Grade Mode " + member.getMidTermGradingMode());
	    		log.info("Final Grade Mode " + member.getFinalGradeMode());
	    		
	    		enroll.getMembers().add(member);
	    	}
	    }		
		return enroll;
	}
	
	/**
	 * Parses a faculty assignment membership message
	 * @param enterprise the root node of the xml message
	 * @return the faculty assignment content from the message
	 */
	private Assignment parseFacultyAssignmentMessage(Node enterprise) {
		Node membership = getNode("membership", enterprise.getChildNodes());	// adds to cross list sections don't have a group tag
		NodeList membershipData = membership.getChildNodes();
		Node sourcedid = getNode("sourcedid", membershipData);
		Node id = getNode("id", sourcedid.getChildNodes());		    
		
		Assignment assign = new Assignment(); 
		assign.setCourseId(getNodeValue(id));
		
		log.info("Assignment for Course ID: " + assign.getCourseId());
		
		for(int x = 0; x < membershipData.getLength(); x++) {
	    	Node node = membershipData.item(x);
	    	if(node.getNodeName().equalsIgnoreCase("member")){
	    		EnrollMember member = new EnrollMember();		// this is an instructor assignment
	    		member.setSourceId(getNodeValue(getNode("id", getNode("sourcedid", node.getChildNodes()).getChildNodes())));
	    		member.setIdType(Integer.parseInt(getNodeValue(getNode("idtype", node.getChildNodes()))));
	    		Node role = getNode("role", node.getChildNodes());
	    		member.setStatus(Integer.parseInt(getNodeValue(getNode("status", role.getChildNodes()))));	    		
	    		//member.setRecStatus(getNodeAttr("recstatus", role)); only for a delete message	    		
	    		member.setRoleType(getNodeAttr("roletype", role)); // roletype = 02; faculty assignment
	    		member.setSubRole(getNodeValue(getNode("subrole", role.getChildNodes()))); // Primary or Subordinate
	    		
	    		log.info("Instructor Source ID " + member.getSourceId());
	    		log.info("IdType " + member.getIdType());
	    		log.info("Status " + member.getStatus());
	    		log.info("RecStatus " + member.getRecStatus());
	    		log.info("Role Type " + member.getRoleType());
	    		log.info("Sub Role " + member.getSubRole());
	    		
	    		assign.getMembers().add(member);
	    	}
	    }		
		return assign;
	}
}

/*
 * XMLReader.java
 * 
 * Copyright (c) Sep 24, 2012 Ripware, LLC.
 * All rights reserved.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE, ARE EXPRESSLY DISCLAIMED. IN NO EVENT SHALL
 * Ripware, LLC OR ITS EMPLOYEES BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED, THE COSTS OF PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED IN ADVANCE OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Redistribution and use of this software in source or binary forms, with or
 * without modification, are permitted, provided that the following conditions
 * are met.
 * 
 * 1. Any redistribution must include the above copyright notice and disclaimer
 *    and this list of conditions in any related documentation and, if feasible, in
 *    the redistributed software.
 * 
 * 2. Any redistribution must include the acknowledgment, "This product includes
 *    software developed by Ripware, LLC, in any
 *    related documentation and, if feasible, in the redistributed software."
 * 
 * 3. The names "Ripware" and "Ripware, LLC" must
 *    not be used to endorse or promote products derived from this software.
 */