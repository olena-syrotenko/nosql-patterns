package ua.nure.knt.coworking.dao;

import com.mongodb.client.MongoCursor;
import org.bson.Document;
import ua.nure.knt.coworking.entity.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public interface UserDao {
	User readUserByEmail(String email) throws SQLException;

	User readUserById(Integer id) throws SQLException;

	List<User> readAllUsers() throws SQLException;

	List<User> readUsersByFullName(String lastName, String firstName) throws SQLException;

	Integer createUser(User user) throws SQLException;

	Integer updateUser(User user) throws SQLException;

	void deleteUsers() throws SQLException;

	Integer migrate(List<User> users) throws SQLException;
}


