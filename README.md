# ADSync - Active Directory Synchronization with Luminis Broker 4 and 5
======

This is the synchronization process from Banner to Active Directory. This application was written before BEIS (Banner Enterprise Identity Service) was created and has been running in production for years. This process is dependent upon Luminis Message Broker (LMB) which is part the Luminis Platform and works with both LP4 and LP5. Banner places changes in a table (GOBEQRC/GOREQRC), the Luminis Message Gateway (LMG) reads the tables list of changes, formats the data into xml messages and sends the new messages to the LMB. Luminis subscribes to a JMS topic in the LMB, from those messages luminis creates users, courses, and members of courses based on those messages.

ADSync works similarly, it will subscribe to a broker Topic, com_sct_ldi_sis_Sync, in the LMB and from messages, ADSync will create/update users in Active Directory. In addition, it will add/drop and create security groups based on terms in active directory. The creation/update of users is a two part process:

A Java application will read xml messages and generate the Powershell command to create/update users in Active Directory.
The Java application will spawn the appropriate Powershell process to create/update the user in Active Directory (Note: the Powershell command is actually creating or updating the user information in AD). The Java application is run as a Windows service which can be stopped or started just like any service on Windows. This allows the process to start when the system is started or started manually as needed.

The creation of security groups, the adding and deletion of members to a security group, works similarly to the process mentioned above. The Java application reads messages from the LMB (those messages could be a add or drop to a section, or the creation of a section) and based on the message, spawns a powershell process to either add a member to a security group, delete a member from a security group, or create a security group for a course section.

Note: ADSync only handles creation of section security groups. Terms, Division, Department, Subject, Term, and Campus security groups must be handled by the Preload_AD_Security_Groups process. This process handles initial creation of groups and enrollments so there is less stress on ADSync to create all groups and members. This is similar to the 'cptool imsimport' process that you must do for Luminis to create sections and enrollments at the beginning of each semester.

Security groups are defined in the "Banner Data Stored in Active Directory" document.

An advantage to this application, receiving messages from the LMB, is that it's fully self-recovering. If the broker is temporarily unavailable, ADSync will rediscover the broker upon it becoming available without need of restarting ADSync. It also allows for runtime changes to its configuration which can be modified at runtime.

For more information on using Powershell Cmdlets for Active Directory, see http://technet.microsoft.com/en-us/library/ee617195.aspx.

## Building the Java Program
Getting the source code

```git clone https://github.com/brippe/ADSync.git```

or you can fork the repo to make your own changes.

Building the project with Maven

```mvn package```

To skip the unit tests in project you can build with the following command:

```mvn package -DskipTests```

This command will create a `activedirectory-<version>-with-dependencies.jar`. This jar file contains ALL the Java dependencies for the Java application to work. More on building the project in the project's README.txt

=======

# ADSync
Ellucian Banner to Active Directory synchronization
