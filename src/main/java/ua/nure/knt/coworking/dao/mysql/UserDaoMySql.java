package ua.nure.knt.coworking.dao.mysql;

import org.bson.Document;
import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.Role;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.util.RoleBuilder;
import ua.nure.knt.coworking.util.UserBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class UserDaoMySql implements UserDao {
	private final Connection connection;

	// READ
	private static final String GET_USER_BY_EMAIL = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id WHERE email = ?";
	private static final String GET_USER_BY_ID = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id WHERE user.id = ?";
	private static final String GET_ALL_USERS = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id";
	private static final String GET_ALL_USERS_BY_FULL_NAME = "SELECT user.id, email, password, last_name, first_name, passport_id, phone_number, role.name FROM user JOIN role ON id_role = role.id WHERE last_name = ? AND first_name = ?";

	// CREATE
	private static final String INSERT_USER = "INSERT INTO user VALUES(?,?,?,?,?,?,?, (SELECT id FROM role WHERE name = ?))";

	// UPDATE
	private static final String UPDATE_USER_BY_ID = "UPDATE user SET email = ?, password = ?, last_name = ?, first_name = ?, passport_id = ?, phone_number = ? WHERE id = ?";

	// DELETE
	private static final String DELETE_ALL_USERS = "DELETE from user WHERE id > 0";

	// MIGRATION
	private static final String GET_ROLE_BY_NAME = "SELECT id, name FROM role WHERE name = ?";
	private static final String INSERT_ROLE = "INSERT INTO role(name) VALUES(?)";

	public UserDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public User readUserByEmail(String email) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_USER_BY_EMAIL);
			ps.setString(1, email);

			ResultSet rs = ps.executeQuery();

			User user = null;
			if (rs.next()) {
				user = extractUserFromResultSet(rs);
			}
			rs.close();
			ps.close();
			return user;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public User readUserById(Integer id) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_USER_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			User user = null;
			if (rs.next()) {
				user = extractUserFromResultSet(rs);
			}
			rs.close();
			ps.close();
			return user;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<User> readAllUsers() throws SQLException {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(GET_ALL_USERS);
			ArrayList<User> users = new ArrayList<>();
			while (rs.next()) {
				users.add(extractUserFromResultSet(rs));
			}
			rs.close();
			return users;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<User> readUsersByFullName(String lastName, String firstName) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_ALL_USERS_BY_FULL_NAME);
			ps.setString(1, lastName);
			ps.setString(2, firstName);
			ResultSet rs = ps.executeQuery();
			ArrayList<User> users = new ArrayList<>();
			while (rs.next()) {
				users.add(extractUserFromResultSet(rs));
			}
			rs.close();
			ps.close();
			return users;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer createUser(User user) throws SQLException {
		try {
			readOrInsertRole(user.getRole());
			PreparedStatement ps = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS);
			if (user.getId() == null) {
				ps.setNull(1, Types.NULL);
			} else {
				ps.setInt(1, user.getId());
			}
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getLastName());
			ps.setString(5, user.getFirstName());
			ps.setString(6, user.getPassportId());
			ps.setString(7, user.getPhoneNumber());
			ps.setString(8, user.getRole()
					.getName());
			ps.executeUpdate();

			ResultSet keys = ps.getGeneratedKeys();
			if (keys.next()) {
				return keys.getInt(1);
			}
			keys.close();
			ps.close();
			return 0;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer updateUser(User user) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(UPDATE_USER_BY_ID);
			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getLastName());
			ps.setString(4, user.getFirstName());
			ps.setString(5, user.getPassportId());
			ps.setString(6, user.getPhoneNumber());
			ps.setInt(7, user.getId());

			int updatedRows = ps.executeUpdate();
			ps.close();
			return updatedRows;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public void deleteUsers() throws SQLException {
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(DELETE_ALL_USERS);
		} catch (SQLException ignored) {

		} finally {
			connection.close();
		}
	}

	@Override
	public void createUserFromDocument(Document document) {

	}

	@Override
	public List<Document> readUsersDocumentsByFullName(String lastName, String firstName) {
		return null;
	}

	private Integer readOrInsertRole(Role role) throws SQLException {
		PreparedStatement readPs = connection.prepareStatement(GET_ROLE_BY_NAME);
		readPs.setString(1, role.getName());
		ResultSet readRs = readPs.executeQuery();
		Integer roleId = null;
		if (readRs.next()) {
			roleId = readRs.getInt("id");
		} else {
			PreparedStatement insertPs = connection.prepareStatement(INSERT_ROLE, Statement.RETURN_GENERATED_KEYS);
			insertPs.setString(1, role.getName());
			insertPs.executeUpdate();
			ResultSet keys = insertPs.getGeneratedKeys();
			if (keys.next()) {
				roleId = keys.getInt(1);
			}
			keys.close();
			insertPs.close();
		}
		readRs.close();
		readPs.close();
		return roleId;
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
