package com.ripware.apps.activedirectory.db;

import javax.sql.DataSource;

/**
 * Banner Data Access Object
 * @author Brad Rippe 
 *
 */
public interface BannerDAO {
	
	/**
	 * Sets the datasource for the DAO
	 * @param dataSource the datasource to use 
	 */
	public void setDataSource(DataSource dataSource);

}
