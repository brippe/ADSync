package com.ripware.apps.activedirectory.cache;

/**
 * Luminis Broker repeats multiple messages. XMLMessageCache is used to store
 * an xml message for comparing it to the next xml message. If the message is
 * the same as the previous message, it should not be processed. If the next message
 * is a new message, then it should be stored in XMLMessageCache and processed.
 *
 * @author Brad Rippe
 *
 */
public class XMLMessageCache {
	private String previousXmlMessage;
	private boolean useCache;			// determines if message cache is used

	/**
	 * Creates an XMLMessageCache for storing the previous
	 * xml message
	 */
	public XMLMessageCache() {
		previousXmlMessage = "";
		useCache = false;
	}

	/**
	 * Compares the current xml message in cache to the specified
	 * xml message.
	 * @param xml the message to be compared to the cached message
	 * @return true if both messages are the same, otherwise false
	 */
	public boolean isSameAsPreviousMessage(String xml) {
		return previousXmlMessage.equals(xml);
	}

	/**
	 * Retrieves the cached message which should be the previous message
	 * that LumListener received.
	 * @return the cached xml message
	 */
	public String getPreviousXmlMessage() {
		return previousXmlMessage;
	}

	/**
	 * Sets the previous xml message; Caches the xml message.
	 * @param xmlMessage the message to be placed in cache
	 */
	public void setPreviousXmlMessage(String xmlMessage) {
		if(!previousXmlMessage.equals(xmlMessage))
			this.previousXmlMessage = xmlMessage;
	}

	/**
	 * Retrieve whether the XMLMessageCache is configured for use.
	 * @return true if XMLMessageCache is configured for use, otherwise false
	 */
	public boolean isUseCache() {
		return useCache;
	}

	/**
	 * Sets whether the XMLMessageCache is in use or not.
	 * @param useCache true to use the message cache, otherwise false
	 */
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
}

