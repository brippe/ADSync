package com.ripware.apps.activedirectory;

import javax.jms.JMSException;

import com.ripware.apps.activedirectory.cache.XMLMessageCache;
import com.ripware.apps.activedirectory.db.PersonDAO;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.ldap.core.LdapTemplate;

import com.sun.messaging.ConnectionConfiguration;
import com.sun.messaging.ConnectionFactory;

import com.ripware.apps.activedirectory.cache.EhcacheWrapper;
import com.ripware.apps.activedirectory.data.Person;
import com.ripware.apps.activedirectory.db.CourseDAO;
import com.ripware.apps.activedirectory.ldap.GroupUtils;
import com.ripware.apps.activedirectory.ldap.PersonSearch;
import com.ripware.apps.activedirectory.server.ADSyncThread;

/**
 * Consumer of Luminis integration messages. Subscribes to Luminis LMB
 * read messages then takes appropriate action to add Active Directory
 * Accounts, add security groups, and/or add/remove members of those 
 * security groups based on class enrollment.
 * 
 * @author Brad Rippe 
 * @see Banner Integration for eLearning - Message Reference Guide 8.0 for
 * 		more information on what messages this consumer handles
 */
public class LumLMBConsumer {
	
	static Logger log = Logger.getLogger(LumLMBConsumer.class.getName());
	
	/**
	 * Runs the LumLMBConsumer, otherwise known as "ADSync"
	 * @param args application arguments; none expected
	 */
    public static void main( String[] args ) {
        
    	ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        ConnectionFactory cf = (ConnectionFactory) context.getBean("connectionFactory");
        ConsumerConfig cConfig = (ConsumerConfig) context.getBean("config");
        XMLMessageCache cache = (XMLMessageCache) context.getBean("xmlMessageCache");
        CourseDAO courseDAO = (CourseDAO) context.getBean("courseDAO");
        PersonDAO personDAO = (PersonDAO) context.getBean("personDAO");
        LdapTemplate ldapTemplate = (LdapTemplate) context.getBean("ldapTemplate");
        @SuppressWarnings("unchecked")
		EhcacheWrapper<String, Person> personCache = (EhcacheWrapper<String, Person>) context.getBean("personCache");
        
        try {
	        //old connectionfactory doesn't support injection of properties
	        cf.setProperty(ConnectionConfiguration.imqAddressList, cConfig.getImqAddressList());
	        cf.setProperty(ConnectionConfiguration.imqDefaultUsername, cConfig.getImqDefaultUsername());
	        cf.setProperty(ConnectionConfiguration.imqDefaultPassword, cConfig.getImqDefaultPassword());
	        if(cConfig.getImqAddressList().contains("ssljms"))
	        	cf.setProperty("imqConnectionType", "TLS");
        } catch(JMSException jmsEx) {
        	log.error(jmsEx);
        }
        
        LumListener listener = new LumListener(cConfig);
        listener.setCache(cache);
        listener.setCourseDAO(courseDAO);
        listener.setPersonDAO(personDAO);
        listener.setPersonSearch(new PersonSearch(ldapTemplate));        
        listener.setPersonCache(personCache);
        
        // Get Security Groups for each institution
        // Security groups are now in 2 different locations
        // OU=Groups (or a root OU)
        // and OU=(term code) contains crn groups
        // groupSearch.getGroupNames gets groups recursively from the root
        // thus, crn groups can be located in a term OU and still be located
        GroupUtils.loadGroupsFromLDAP(ldapTemplate, cConfig, listener);
        
        log.info("Starting communication server socket for the preload process!");
        ADSyncThread preloadCommunicationServer = new ADSyncThread(ldapTemplate, cConfig, listener);
        preloadCommunicationServer.start();
        
        DefaultMessageListenerContainer dmContainer = new DefaultMessageListenerContainer();
        dmContainer.setConnectionFactory(cf);
	    dmContainer.setDestinationName(cConfig.getImqDestination());
	    dmContainer.setMessageListener(listener);
	    dmContainer.setPubSubDomain(true);
	    dmContainer.initialize();
	                            
        log.info("Broker Address: " + cConfig.getImqAddressList());
        log.info("Queue/Topic: " + cConfig.getImqDestination());
        log.info("Cache XML messages: " + cache.isUseCache());
        log.info("isActive: " + dmContainer.isActive());
        log.info("Running: " + dmContainer.isRunning());
        log.info("Durable: " + dmContainer.isSubscriptionDurable());        
        log.info("Active Consumers: " + dmContainer.getActiveConsumerCount());
        log.info("Max Messages: " + dmContainer.getMaxMessagesPerTask());        
    }
}
