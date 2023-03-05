package org.sanat.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.sanat.model.Circle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

@Component
public class JDBCMDaoImp {

	private DataSource dataSource;
	private JdbcTemplate jdbcTemplate;
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	//private SimpleJdbcTemplate simpleJdbcTemplate;
	
	public Circle getCirclebyID(int id) {
		Connection connection = null;
		try {
			// DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
			// connection = DriverManager.getConnection("jdbc:derby://localhost:1527/db");
			connection = dataSource.getConnection();
			PreparedStatement ps = connection.prepareStatement("SELECT * FROM circle where id= ?");
			ps.setInt(1, id);
			Circle circle = null;
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				circle = new Circle(id, rs.getString("name"));
				return circle;
			}
			rs.close();
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public int getCircleCount() {
		String sql = "SELECT COUNT(*) FROM CIRCLE";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public String getCircleName(int circleId) {
		String sql = "SELECT name FROM circle where id= ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { circleId }, String.class);
	}

	public Circle getCircle(int circleId) {
		String sql = "SELECT * FROM circle where id= ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { circleId }, new CircleMapper());
	}

	public List<Circle> getAllCircle() {
		String sql = "SELECT * FROM circle";
		return jdbcTemplate.query(sql, new CircleMapper());
	}

	public DataSource getDataSource() {
		return dataSource;
	}

//	public void insertCircle(Circle circle) {
//		String sql = "Insert into circle (id, name) values (?,?)";
//		jdbcTemplate.update(sql, new Object [] {circle.getId(), circle.getName()});
//	}

	public void insertCircle(Circle circle) {
		String sql = "Insert into circle (id, name) values (:id,:name)";
		SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", circle.getId()).addValue("name",
				circle.getName());
		namedParameterJdbcTemplate.update(sql, sqlParameterSource);
	}

	public void createTriangleTable() {
		String sql = "create table triangle (id INTEGER, name VARCHAR(50))";
		jdbcTemplate.execute(sql);
	}

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	private static final class CircleMapper implements RowMapper<Circle> {

		@Override
		public Circle mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
			Circle circle = new Circle();
			circle.setId(resultSet.getInt("id"));
			circle.setName(resultSet.getString("name"));
			return circle;
		}

	}
}
