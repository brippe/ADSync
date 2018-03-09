package com.ripware.apps.activedirectory.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.ripware.apps.activedirectory.data.CampusCodes;

/**
 * Course Data Access Object
 * @author Brad Rippe 
 *
 */
public class CourseDAO implements BannerDAO {

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
	 * Returns the Campus Code for the specified course
	 * @param term the term code for the course
	 * @param crn the course request number
	 * @return the campus code for the course
	 */
	public String getCampus(String term, String crn) {
		StringBuffer sql = new StringBuffer();
		sql.append("select ssbsect_camp_code from ssbsect ");
		sql.append("where ssbsect_term_code = '");
		sql.append(term);
		sql.append("' and ssbsect_crn = ");
		sql.append(crn);
		List<String> codes = jdbcTemplate.query(sql.toString(), new CampusCodeMapper());
		if(codes.size() > 0)
			return codes.get(0);
		
		return CampusCodes.BCollege.toString();	// return BCollege by default
	}
	
	private static final class CampusCodeMapper implements RowMapper {
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			return rs.getString("ssbsect_camp_code");
		}
	}	
}
