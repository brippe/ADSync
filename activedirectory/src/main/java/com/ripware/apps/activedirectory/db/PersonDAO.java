package com.ripware.apps.activedirectory.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ripware.apps.activedirectory.data.Person;

/**
 * Person Data Access Object
 * @author Brad Rippe 
 *
 */
public class PersonDAO implements BannerDAO {
	
	Logger log = Logger.getLogger(PersonDAO.class.getName());
	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;

	/* (non-Javadoc)
	 * @see BannerDAO#setDataSource(javax.sql.DataSource)
	 */
	@Override
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}
	
	/**
	 * Retrieves Person information from the backend service
	 * @param bannerID person's banner id
	 * @return a person
	 */
	public Person getPerson(String bannerID) {
		log.info("Getting person information from DB for "+ bannerID);
				
		StringBuffer sql = new StringBuffer();
		// Query occurs before the student email information is in Banner
		// thus, email could be null
		sql.append("select * from (");
		sql.append("select gorpaud_external_user, gorpaud_pin, ");
		sql.append("spriden_first_name || ' ' || spriden_last_name as displayName, "); 
		sql.append("spriden_first_name, spriden_last_name, goremal_email_address as email, ");
		sql.append("spraddr_street_line1, spraddr_city, spraddr_stat_code, spraddr_zip ");
		sql.append("from GORPAUD g ");
		sql.append("left join SPRIDEN s ");
		sql.append("on g.gorpaud_pidm = s.spriden_pidm ");
		sql.append("left join SPRADDR sa ");
		sql.append("on g.gorpaud_pidm = sa.spraddr_pidm ");
		sql.append("and (spraddr_status_ind is null and spraddr_to_date is null) ");
		sql.append("left join GOREMAL ge ");		
		sql.append("on spriden_pidm = ge.goremal_pidm ");
		sql.append("and (goremal_preferred_ind = 'Y' and goremal_status_ind = 'A') ");
		sql.append("WHERE (gorpaud_pin IS NOT NULL) ");
		// some cases gorpaud_pin is null and other cases gorpaud_external_user is null
		// this is a rare case but none the less occurs		
		sql.append("and spriden_id = '@");
		sql.append(bannerID);
		sql.append("' ORDER BY gorpaud_activity_date DESC) ");
		sql.append("WHERE rownum = 1");
		
		log.info("person query = " + sql.toString());
				
		List<Person> people = jdbcTemplate.query(sql.toString(), new PersonMapper());
		if(people.size() > 0) {
			if(people.get(0).getLogonID() == null)
				people.get(0).setLogonID(bannerID);
			return people.get(0);
		}
		return null;
	}
	
	/**
	 * Retrieves Person information from the backend service
	 * @param bannerID person's banner id
	 * @return a banner id based on the message source id
	 */
	public String getBannerIDFromSourceId(String srcID) {
		log.info("Getting bannerid from DB for "+ srcID);
				
		StringBuffer sql = new StringBuffer();
		// Query occurs before the student email information is in Banner
		// thus, email could be null
		sql.append("select substr(spriden_id, 2) as bannerId "); 
		sql.append("from spriden s ");
		sql.append("left join gobsrid g ");
		sql.append("on s.spriden_pidm = g.gobsrid_pidm ");
		sql.append("where g.gobsrid_sourced_id = ");
		sql.append(srcID);
		sql.append(" and length(s.spriden_id) = 9 ");
		sql.append("and s.spriden_change_ind is null and rownum = 1 ");
		String bannerId = (String) jdbcTemplate.queryForObject(sql.toString(), new PIDMMapper());
		return bannerId;
	}
	
	/**
	 * Retrieves Primary instructor's id
	 * @param term the course term code
	 * @param crn the course request number
	 * @return a banner id
	 */
	public String getPrimaryInstructorBannerId(String term, String crn) {
		log.info("Getting bannerid from DB for instructor in " + term + " crn: " + crn);
				
		StringBuffer sql = new StringBuffer();
		sql.append("select substr(spriden_id, 2) as bannerId ");
		sql.append("from sirasgn ");
		sql.append("left join spriden ");
		sql.append("on sirasgn_pidm = spriden_pidm ");
		sql.append("where sirasgn_term_code = '");
		sql.append(term);
		sql.append("' and sirasgn_crn = ");
		sql.append(crn);
		sql.append(" and sirasgn_primary_ind = 'Y' and spriden_change_ind is null");
				
		String bannerId = (String) jdbcTemplate.queryForObject(sql.toString(), new PIDMMapper());
		return bannerId;
	}
		
	private static final class PersonMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			Person p = new Person();
			p.setLogonID(rs.getString("gorpaud_external_user"));
			p.setPassword(rs.getString("gorpaud_pin"));
			p.setCity(rs.getString("spraddr_city"));
			p.setDisplayName(rs.getString("displayName"));
			p.setEmail(rs.getString("email"));
			p.setGivenName(rs.getString("spriden_first_name"));
			p.setZipCode(rs.getString("spraddr_zip"));
			p.setState(rs.getString("spraddr_stat_code"));
			p.setStreet(rs.getString("spraddr_street_line1"));
			p.setFamilyName(rs.getString("spriden_last_name"));
			return p;
		}
	}
	
	private static final class PIDMMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("bannerId");
		}
	}
}
