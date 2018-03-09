package com.ripware.apps.activedirectory.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.ripware.apps.activedirectory.ConsumerConfig;
import com.ripware.apps.activedirectory.ldap.GroupUtils;
import org.apache.log4j.Logger;
import org.springframework.ldap.core.LdapTemplate;
import com.ripware.apps.activedirectory.LumListener;

/**
 * Server that communicates on the adsyncPort in beans.xml
 * with the preload process. This allows the preload process to
 * tell ADSync when it has loaded new terms. Since, ADSync keeps an
 * on going cache of terms loaded in AD, it will need to update those
 * terms when new terms are loaded. This is the service that allows that 
 * communication.
 * 
 * @author Brad Rippe 
 *
 */
public class ADSyncThread extends Thread {
	
	static Logger log = Logger.getLogger(ADSyncThread.class.getName());
	private LdapTemplate ldapTemplate;
	private ConsumerConfig cConfig;
	private LumListener listener;
	private ServerSocket server;
	
	/**
	 * Creates an object for communication with the 
	 * adsync preload process
	 * @param config adsync configuration bean
	 */
	public ADSyncThread(LdapTemplate ldapTemplate, ConsumerConfig cConfig, 
			LumListener listener) {
		this.ldapTemplate = ldapTemplate;
		this.cConfig = cConfig;
		this.listener = listener;		
		log.debug("ADSyncServer is created!");
	}
	
	@Override
	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		
		try {			
			server = new ServerSocket(Integer.parseInt(cConfig.getAdsyncPort()));
			
			// allows multiple executions of preload to communicate with ADSync
			while(true) {
				Socket serClient = server.accept(); 		// BLOCKING CALL
								
				log.debug("Socket accepted");
				in = new BufferedReader(new InputStreamReader(serClient.getInputStream()));
				out = new PrintWriter(serClient.getOutputStream(), true);
				log.debug("Got connected and in/out streams are ready");
				
				String line;
				// should send only 1 line of information
				if((line = in.readLine()) != null) {				
					log.debug("Got " + line + " from PreloadSecurityGroups");
					log.debug("Reloading security groups from Active Directory");
					if(line != null && line.equals("Update Terms"))
						GroupUtils.loadGroupsFromLDAP(ldapTemplate, cConfig, listener);

					// send back to preload that it was successful at reloading ad groups
					out.print("Successful reload of ADSync groups!");
					out.flush();				
					log.debug("Successful reload of ADSync groups!");
				}
			}
		} catch(NumberFormatException nfe) {			
			log.error("Invalid communication port number!", nfe);
		} catch(IOException ioe) {
			log.error("Socket Server couldn't listen on port " + cConfig.getAdsyncPort(), ioe);
		} finally {
			try {
			if(in != null)
				in.close();
			if(out != null)
				out.close();
			if(server != null)				
				server.close();
			} catch (IOException ioe2) {}
		}
	}
}
