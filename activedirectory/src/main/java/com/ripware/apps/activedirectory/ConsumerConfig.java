package com.ripware.apps.activedirectory;

/**
 * Stores configuration information. Instantiated from <i>beans.xml</i>.
 * 
 * @author Brad Rippe 
 *
 */
public class ConsumerConfig {
	
	private String addCommand;
	private String addGroupCommand;
	private String addGroupMembersCommand;
	private boolean addInfo; // set to true to add user info, otherwise false
	private String path; 	// ad path where user accounts are created; Ex OU=People,DC=Ripware,dc=local
	private String AGroupPath;	// ad path where groups are created; ou=class rosters,dc=Ripware,dc=local
	private String BGroupPath;
	private String CGroupPath;
	private String domain; 	// ad domain where user account are created; Ex Ripware.local
	
	private String imqAddressList;		// address of the broker
	private String imqDefaultUsername;	// username to connect to the broker
	private String imqDefaultPassword;	// pass for the username
	private String imqDestination;		// broker topic to connection to
	private boolean messaging;			// determines if messages will be sent to notify campuses when
	private String adsyncPort;			// communication port so preloadSecurityGroups can communicate with ADSync
	private boolean createOrModifyUsers;	// if true, adsync will process user updates and adds

	// users are create, added to or dropped from a security group (course)
	/**
	 * Is application configured to add user information when
	 * creating Active Directory accounts
	 * @return true if configured to add user details when creating accounts, otherwise false
	 */
	public boolean isAddInfo() {
		return addInfo;
	}

	/**
	 * Determines if additional details are added to user information when 
	 * creating Active Directory accounts. If true, full details are added to 
	 * the user, otherwise false a minimum set of information is added to the 
	 * account when created
	 * @param addInfo true if full details added, otherwise false
	 */
	public void setAddInfo(boolean addInfo) {
		this.addInfo = addInfo;
	}

	/**
	 * Retrieves the basic powershell command for adding users to Active Directory.
	 * <i>Example: 'cmd /c powershell.exe -ExecutionPolicy Bypass -NoLogo -NonInteractive -NoProfile -WindowStyle Hidden -File c:\Temp\ADUser\AddADUser.ps1 -n '</i>
	 * @return the basic powershell command to create an account
	 * 
	 */
	public String getAddCommand() {
		return addCommand;
	}

	/**
	 * Sets the basic powershell command for adding users to Active Directory.
	 * @param addCommand the basic powershell add command
	 */
	public void setAddCommand(String addCommand) {
		this.addCommand = addCommand;
	}

	/**
	 * Retrieves the basic powershell command for creating a security group
	 * @return the basic powershell command for creating a security group
	 */
	public String getAddGroupCommand() {
		return addGroupCommand;
	}

	/**
	 * Sets the basic powershell command for creating a security group
	 * @param addGroupCommand the basic powershell command for creating a security group
	 */
	public void setAddGroupCommand(String addGroupCommand) {
		this.addGroupCommand = addGroupCommand;
	}

	/**
	 * Retrieves the basic powershell command for adding a member to a security group
	 * @return the basic powershell command for adding a member to a security group
	 */
	public String getAddGroupMembersCommand() {
		return addGroupMembersCommand;
	}

	/**
	 * Sets the basic powershell command for adding a member to a security group
	 * @param addGroupMembersCommand the basic powershell command for adding a member to a security group
	 */
	public void setAddGroupMembersCommand(String addGroupMembersCommand) {
		this.addGroupMembersCommand = addGroupMembersCommand;
	}

	/**
	 * Retrieves Active Directory/LDAP path to user accounts 
	 * @return the AD/LDAP path to user accounts
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets the Active Directory/LDAP path to user accounts
	 * <i>Example: OU=People,DC=Ripware,dc=local</i>
	 * @param path the location to create user accounts in Active Directory
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Retrieves the AD/LDAP path for where course security groups are located for B College
	 * @return the path to security groups
	 */
	public String getAGroupPath() {
		return AGroupPath;
	}

	/**
	 * Sets the path to security groups in Active Directory for A College
	 * @param groupsPath
	 */
	public void setAGroupPath(String groupsPath) {
		this.AGroupPath = groupsPath;
	}

	/**
	 * Retrieves the AD/LDAP path for where course security groups are located for BCollege
	 * @return the path to security groups
	 */
	public String getBGroupPath() {
		return BGroupPath;
	}

	/**
	 * Sets the path to security groups in Active Directory for A College
	 * @param groupsPath
	 */
	public void setBGroupPath(String groupsPath) {
		this.BGroupPath = groupsPath;
	}

	/**
	 * Retrieves the AD/LDAP path for where course security groups are located for CCollege
	 * @return the path to security groups
	 */
	public String getCGroupPath() {
		return CGroupPath;
	}

	/**
	 * Sets the path to security groups in Active Directory for CCollege
	 * @param groupsPath
	 */
	public void setCGroupPath(String groupsPath) {
		this.CGroupPath = groupsPath;
	}

	
	/**
	 * Retrieves the domain that the application is working with
	 * @return the Active Directory domain
	 */
	public String getDomain() {
		return domain;
	}

	/**
	 * Sets the Active Directory domain the application is interacting with
	 * <i>Example: Ripware.local</i>
	 * @param domain the active directory domain
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * Retrieves message broker address for iPlanet Message Queue (LMB) 
	 * @return the address for LMB
	 */
	public String getImqAddressList() {
		return imqAddressList;
	}

	/**
	 * Sets the address for iPlanet Message Queue (LMB) 
	 * <i>Example: mq://mygateway:7676/ssljms</i>
	 * @param imqAddressList the address for LMB 
	 */
	public void setImqAddressList(String imqAddressList) {
		this.imqAddressList = imqAddressList;
	}

	/**
	 * Retrieves the iPlanet Message Queue (LMB) username
	 * @return the iPlanet message queue username
	 */
	public String getImqDefaultUsername() {
		return imqDefaultUsername;
	}

	/**
	 * Sets the iPlanet Message Queue (LMB) username
	 * @param imqDefaultUsername the imq username
	 */
	public void setImqDefaultUsername(String imqDefaultUsername) {
		this.imqDefaultUsername = imqDefaultUsername;
	}

	/**
	 * Retrieves the iPlanet Message Queue (LMB) password
	 * @return the imq password
	 */
	public String getImqDefaultPassword() {
		return imqDefaultPassword;
	}

	/**
	 * Sets the iPlanet Message Queue (LMB) password
	 * @param imqDefaultPassword the imq password
	 */
	public void setImqDefaultPassword(String imqDefaultPassword) {
		this.imqDefaultPassword = imqDefaultPassword;
	}

	/**
	 * Retrieves the iPlanet Message Queue (LMB) topic
	 * @return the imq password
	 */
	public String getImqDestination() {
		return imqDestination;
	}

	/**
	 * Sets the iPlanet Message Queue (LMB) topic
	 * Banner uses com_sct_ldi_sis_Sync to send messages to about changes in Banner
	 * @param imqDestination the imq topic to subscribe to, ex com_sct_ldi_sis_Sync
	 */
	public void setImqDestination(String imqDestination) {
		this.imqDestination = imqDestination;
	}
	
	/**
	 * Is messaging on or off for notifying campuses
	 * @return the messaging
	 */
	public boolean isMessaging() {
		return messaging;
	}

	/**
	 * Set messaging on or off for notifying campuses
	 * @param messaging the messaging to set
	 */
	public void setMessaging(boolean messaging) {
		this.messaging = messaging;
	}

	/**
	 * Retrieves the adsync port
	 * This is the communication port adsync opens for 
	 * the preload process
	 * @return the adsyncPort
	 */
	public String getAdsyncPort() {
		return adsyncPort;
	}

	/**
	 * Sets the adsync communication port for the preload process
	 * @param adsyncPort the adsyncPort to set
	 */
	public void setAdsyncPort(String adsyncPort) {
		this.adsyncPort = adsyncPort;
	}

	/**
	 * Determines if users should be modified or created
	 * @return the createOrModifyUsers true if users will be modified and created
	 * otherwise, false
	 */
	public boolean isCreateOrModifyUsers() {
		return createOrModifyUsers;
	}

	/**
	 * Sets whether users should be created or modified
	 * @param createOrModifyUsers the createOrModifyUsers to set
	 * 			true to create and modify users, otherwise false
	 */
	public void setCreateOrModifyUsers(boolean createOrModifyUsers) {
		this.createOrModifyUsers = createOrModifyUsers;
	}
}
