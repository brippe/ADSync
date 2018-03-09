package com.ripware.apps.activedirectory.data;
import java.util.ArrayList;

/**
 * Represents a person; student or faculty
 * @author Brad Rippe 
 *
 */
public class Person implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String logonID;
	private String logonEncryptType;
	private String sctID;
	private String sctEncryptType;
	private String emailID;
	private String password;
	private String preferredName;
	private String displayName; 	// fn
	private String givenName; 		// given
	private String prefix;			// mr. mrs. ms.
	private String suffix;			// Jr. Sr. III
	private String middleName;  	// middleName in partname element
	private String familyName; 		// family
	private String udcIdentifier; 
	private String gender; 			// gender
	private String email; 			// email
	private String street; 			// street
	private String city; 			// locality
	private String state; 			// region
	private String zipCode; 		// pcode
	private String voiceTele;
	private String faxTele;
	private String mobileTele;
	private String pagerTele;	
	private String major; 			// academicmajor
	private String academicTitle; 	// faculty's major???
	private String academicDegree;	// faculty's earned degree
	private String recStatus;		// recStatus = 3 for delete otherwise not added
	
	private ArrayList<Role> institutionalRoles;
	private ArrayList<String> luminisRoles; // customrole
	
	/**
	 * Creates a person
	 */
	public Person() {
		institutionalRoles = new ArrayList<Role>();
		luminisRoles = new ArrayList<String>();
		preferredName = "";
		prefix = "";
		suffix = "";
		voiceTele = "";
		faxTele = "";
		mobileTele = "";
		pagerTele = "";
		academicTitle = "";
		academicDegree = "";
		recStatus = "";
	}
	
	/**
	 * Gets the logon id; Banner id 
	 * @return the logon id
	 */
	public String getLogonID() {
		return logonID;
	}
	
	/** 
	 * Sets the logon id for a user
	 * @param logonID banner id without the '@'
	 */
	public void setLogonID(String logonID) {
		this.logonID = logonID;		
	}
	
	/**
	 * Gets the SCT id. This is the banner id with the '@'
	 * @return the SCT ID
	 */
	public String getSctID() {
		return sctID;
	}
	
	/**
	 * Sets the SCT ID. This will be the banner id with the '@'
	 * @param sctID the banner id with the '@'
	 */
	public void setSctID(String sctID) {
		this.sctID = sctID;
	}
	
	/**
	 * Gets the person's email
	 * @return person's email. More than likely default to banner id
	 */
	public String getEmailID() {
		return emailID;
	}
	
	/**
	 * Sets the person's email
	 * @param emailID the person's email. More than likely default to the banner id
	 */
	public void setEmailID(String emailID) {
		this.emailID = emailID;
	}
	
	/**
	 * Gets the person's password
	 * @return the person's password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * Sets the person's password
	 * @param password the person's password
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * Gets the person's preferred name
	 * @return the preferred name
	 */
	public String getPreferredName() {
		return preferredName;
	}

	
	/**
	 * Sets the person's preferred name
	 * @param preferredName the preferred name
	 */
	public void setPreferredName(String preferredName) {
		this.preferredName = preferredName;
	}

	/**
	 * Gets the person's display name
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}
	
	/**
	 * Sets the person's display name 
	 * @param displayName the display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	/**
	 * Gets the person's given name (First Name)
	 * @return the person's first name
	 */
	public String getGivenName() {
		return givenName;
	}
	
	/**
	 * Sets the person's give name (first name)
	 * @param givenName the given name
	 */
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	
	/**
	 * Gets the person's prefix. Mr, Mrs., Ms., etc
	 * @return the person't prefix
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the person's prefix. Mr., Dr., Mrs. etc
	 * @param prefix the prefix
	 */
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * Gets the person's suffix. Jr., III, Sr., etc
	 * @return the person's suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the person's suffix. Jr., III, Sr., etc
	 * @param suffix the person's suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	/**
	 * Gets the person's middle name
	 * @return the person's middle name 
	 */
	public String getMiddleName() {
		return middleName;
	}

	/**
	 * Sets the person's middle name 
	 * @param middleName the middle name
	 */
	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	/**
	 * Gets the person's family name (last name)
	 * @return the person's family name (last name)
	 */
	public String getFamilyName() {
		return familyName;
	}
	
	/**
	 * Sets the person's family name (last name)
	 * @param familyName the person's family name (last name)
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	
	/**
	 * Gets the person's gender 
	 * @return 1 = female, 2 = male, 0 = unknown
	 */
	public String getGender() {
		return gender;
	}
	
	/**
	 * Sets the person's gender
	 * @param gender 1 = female, 2 = male, 0 = unknown
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	/**
	 * Gets the person's email
	 * @return the person's email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the person's email
	 * @param email the person's email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the person's street
	 * @return the person's street
	 */
	public String getStreet() {
		return street;
	}
	
	/**
	 * Sets the person's street
	 * @param street the person's street
	 */
	public void setStreet(String street) {
		this.street = street;
	}
	
	/**
	 * Gets the person's city
	 * @return the person's city
	 */
	public String getCity() {
		return city;
	}
	
	/**
	 * Sets the person's city
	 * @param city the person's city
	 */
	public void setCity(String city) {
		this.city = city;
	}
	
	/**
	 * Gets the person's state
	 * @return the person's state
	 */
	public String getState() {
		return state;
	}
	
	/**
	 * Sets the person's state
	 * @param state the person's state
	 */
	public void setState(String state) {
		this.state = state;
	}
	
	/**
	 * Gets the person's zip code
	 * @return the person's zip code
	 */
	public String getZipCode() {
		return zipCode;
	}
	
	/**
	 * Sets the person's zip code
	 * @param zipCode the person's zip code
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	/**
	 * Gets the person's voice telephone
	 * @return the person's voice telephone
	 */
	public String getVoiceTele() {
		return voiceTele;
	}

	/**
	 * Sets the person's voice telephone
	 * @param voiceTele the person's voice telephone
	 */
	public void setVoiceTele(String voiceTele) {
		this.voiceTele = voiceTele;
	}

	/**
	 * Gets the person's fax number
	 * @return the person's fax number
	 */
	public String getFaxTele() {
		return faxTele;
	}

	/**
	 * Sets the person's fax number
	 * @param faxTele the person's fax number
	 */
	public void setFaxTele(String faxTele) {
		this.faxTele = faxTele;
	}

	/**
	 * Gets the person's mobile number
	 * @return the person's mobile number
	 */
	public String getMobileTele() {
		return mobileTele;
	}

	/**
	 * Sets the person's mobile number
	 * @param mobileTele the person's mobile number
	 */
	public void setMobileTele(String mobileTele) {
		this.mobileTele = mobileTele;
	}

	/**
	 * Gets the person's pager number
	 * @return the person's pager number
	 */
	public String getPagerTele() {
		return pagerTele;
	}

	/**
	 * Sets the person's pager number
	 * @param pagerTele the person's pager number
	 */
	public void setPagerTele(String pagerTele) {
		this.pagerTele = pagerTele;
	}

	/**
	 * Gets the person's luminis roles
	 * @return the person's luminis roles
	 */
	public ArrayList<String> getLuminisRoles() {
		return luminisRoles;
	}
	
	/**
	 * Sets the person's luminis roles
	 * @param luminisRoles the person's luminis roles
	 */
	public void setluminisRoles(ArrayList<String> luminisRoles) {
		this.luminisRoles = luminisRoles;
	}
	
	/**
	 * Gets the person's institution roles
	 * @return the person's institution roles
	 */
	public ArrayList<Role> getInstitutionRoles() {
		return institutionalRoles;
	}
	
	/**
	 * Sets the person's institution roles
	 * @param institutionalRoles the person's institution roles
	 */
	public void setInstitutionRoles(ArrayList<Role> institutionalRoles) {
		this.institutionalRoles = institutionalRoles;
	}
	
	/**
	 * Gets the person's major
	 * @return person's major
	 */
	public String getMajor() {
		return major;
	}
	
	/**
	 * Sets the person's major
	 * @param major person's major
	 */
	public void setMajor(String major) {
		this.major = major;
	}

	/**
	 * Gets the person's academic title
	 * @return faculty member title
	 */
	public String getAcademicTitle() {
		return academicTitle;
	}

	/**
	 * Sets the person's academic title
	 * @param academicTitle faculty member title
	 */
	public void setAcademicTitle(String academicTitle) {
		this.academicTitle = academicTitle;
	}

	/**
	 * Gets a faculty members earned degree
	 * @return faculty members earned degree
	 */
	public String getAcademicDegree() {
		return academicDegree;
	}

	/**
	 * Sets faculty members earned degree
	 * @param academicDegree faculty members earned degree
	 */
	public void setAcademicDegree(String academicDegree) {
		this.academicDegree = academicDegree;
	}

	/**
	 * Gets the banner generated UDC identifier
	 * @return the banner generated UDC identifier
	 */
	public String getUdcIdentifier() {
		return udcIdentifier;
	}

	/**
	 * Sets the banner generated UDC identifier
	 * @param udcIdentifier the banner generated UDC identifier
	 */
	public void setUdcIdentifier(String udcIdentifier) {
		this.udcIdentifier = udcIdentifier;
	}

	/**
	 * Gets the logon encrypt type. 
	 * Explains how the password was encrypted.
	 * @return the logon encrypt type
	 */
	public String getLogonEncryptType() {
		return logonEncryptType;
	}

	/**
	 * Sets the logon encrypt type
	 * @param logonEncryptType the logon encrypt type
	 */
	public void setLogonEncryptType(String logonEncryptType) {
		this.logonEncryptType = logonEncryptType;
	}

	/**
	 * Gets the SCT encrypt type
	 * Explains how the password was encrypted
	 * @return the SCT encrypt type
	 */
	public String getSctEncryptType() {
		return sctEncryptType;
	}

	/**
	 * Sets the SCT encrypt type
	 * @param sctEncryptType the SCT encrypt type
	 */
	public void setSctEncryptType(String sctEncryptType) {
		this.sctEncryptType = sctEncryptType;
	}

	/**
	 * Gets the record status. Create, update = "", 3 = delete
	 * @return determines if the message was an add or a drop person from system
	 */
	public String getRecStatus() {
		return recStatus;
	}

	/**
	 * Sets the record status
	 * @param recStatus the record status. 3 = delete
	 */
	public void setRecStatus(String recStatus) {
		this.recStatus = recStatus;
	}
	
	/**
	 * Pads a bannerID with additional zeros if 
	 * the bannerID is < 8 digits
	 * @param bannerId the bannerId
	 * @return a bannerID with 8 digits if < 8 digits; otherwise, returns the bannerId
	 */
	public static  String padBannerID(String bannerId) {
		if(bannerId.length() < 8) {
			StringBuffer s = new StringBuffer();
			for(int i = bannerId.length(); i < 8; i++)
				s.append("0");
			s.append(bannerId);
			return s.toString();
		} else {
			return bannerId;
		}		
	}
}

