package ua.nure.knt.coworking.dao.mysql;

import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.util.RoleBuilder;
import ua.nure.knt.coworking.util.UserBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoMySql implements UserDao {
	private final Connection connection;

	// READ
	private static final String GET_USER_BY_EMAIL = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id WHERE email = ?";
	private static final String GET_USER_BY_ID = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id WHERE id = ?";
	private static final String GET_ALL_USERS = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id WHERE role.name = 'ROLE_USER'";

	// CREATE
	private static final String INSERT_USER = "INSERT INTO user VALUES(null,?,?,?,?,?,?, (SELECT id FROM role WHERE name = ?))";

	// UPDATE
	private static final String UPDATE_USER_BY_ID = "SET email = ?, password = ?, last_name = ?, first_name = ?, passport_id = ?, phone_number = ? WHERE id = ?";

	public UserDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public User readUserByEmail(String email) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(GET_USER_BY_EMAIL);
		ps.setString(1, email);
		ResultSet rs = ps.executeQuery();
		User user = null;
		if (rs.next()) {
			user = extractUserFromResultSet(rs);
		}
		rs.close();
		ps.close();
		connection.close();
		return user;
	}

	@Override
	public User readUserById(Integer id) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(GET_USER_BY_ID);
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();
		User user = null;
		if (rs.next()) {
			user = extractUserFromResultSet(rs);
		}
		rs.close();
		ps.close();
		connection.close();
		return user;
	}

	@Override
	public List<User> readAllUsers() throws SQLException {
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(GET_ALL_USERS);
		ArrayList<User> users = new ArrayList<>();
		while (rs.next()) {
			users.add(extractUserFromResultSet(rs));
		}
		rs.close();
		connection.close();
		return users;
	}

	@Override
	public int createUser(User user) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(INSERT_USER);
		ps.setString(1, user.getEmail());
		ps.setString(2, user.getPassword());
		ps.setString(3, user.getLastName());
		ps.setString(4, user.getFirstName());
		ps.setString(5, user.getPassportId());
		ps.setString(6, user.getPhoneNumber());
		ps.setString(7, user.getRole()
				.getName());
		int updatedRows = ps.executeUpdate();
		connection.close();
		return updatedRows;
	}

	@Override
	public int updateUser(User user) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(UPDATE_USER_BY_ID);
		ps.setString(1, user.getEmail());
		ps.setString(2, user.getPassword());
		ps.setString(3, user.getLastName());
		ps.setString(4, user.getFirstName());
		ps.setString(5, user.getPassportId());
		ps.setString(6, user.getPhoneNumber());
		ps.setInt(7, user.getId());
		int updatedRows = ps.executeUpdate();
		connection.close();
		return updatedRows;
	}

	private User extractUserFromResultSet(ResultSet rs) throws SQLException {
		return new UserBuilder().setId(rs.getInt(1))
				.setEmail(rs.getString(2))
				.setPassword(rs.getString(3))
				.setLastName(rs.getString(4))
				.setFirstName(rs.getString(5))
				.setPassportId(rs.getString(6))
				.setPhoneNumber(rs.getString(7))
				.setRole(new RoleBuilder().setName(rs.getString(8))
						.build())
				.build();
	}

}
