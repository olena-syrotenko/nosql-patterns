package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.util.RoleBuilder;
import ua.nure.knt.coworking.util.UserBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Sorts.descending;

public class UserDaoMongoDb implements UserDao {
	private static final String USER_COLLECTION = "user";
	private final MongoDatabase database;

	public UserDaoMongoDb(MongoClient mongoClient) {
		this.database = mongoClient.getDatabase("coworking");
	}

	@Override
	public User readUserByEmail(String email) throws SQLException {
		Document document = database.getCollection(USER_COLLECTION)
				.find(eq("email", email))
				.first();
		return document == null ? null : extractUserFromDocument(document);
	}

	@Override
	public User readUserById(Integer id) throws SQLException {
		Document document = database.getCollection(USER_COLLECTION)
				.find(eq("id", id))
				.first();
		return document == null ? null : extractUserFromDocument(document);
	}

	@Override
	public List<User> readAllUsers() throws SQLException {
		MongoCursor<Document> usersCursor = database.getCollection(USER_COLLECTION)
				.find()
				.cursor();
		return extractUserListFromDocuments(usersCursor);
	}

	@Override
	public List<User> readUsersByFullName(String lastName, String firstName) throws SQLException {
		MongoCursor<Document> usersCursor = database.getCollection(USER_COLLECTION)
				.find(and(eq("last_name", lastName), eq("first_name", firstName)))
				.cursor();
		return extractUserListFromDocuments(usersCursor);
	}

	@Override
	public Integer createUser(User user) throws SQLException {
		Document lastId = database.getCollection(USER_COLLECTION)
				.find()
				.sort(descending("id"))
				.limit(1)
				.first();
		Integer newId = lastId == null ? 1 : lastId.getInteger("id") + 1;
		user.setId(newId);
		database.getCollection(USER_COLLECTION)
				.insertOne(extractDocumentFromUser(user));
		return newId;
	}

	@Override
	public Integer updateUser(User user) throws SQLException {
		UpdateResult updateResult = database.getCollection(USER_COLLECTION)
				.updateOne(eq("email", user.getEmail()), extractUpdatesFromUser(user));
		return Math.toIntExact(updateResult.getModifiedCount());
	}

	@Override
	public void deleteUsers() throws SQLException {
		database.getCollection(USER_COLLECTION)
				.deleteMany(gte("id", 0));
	}

	@Override
	public Integer migrate(List<User> users) throws SQLException {
		database.getCollection(USER_COLLECTION)
				.insertMany(users.stream()
						.map(this::extractDocumentFromUser)
						.collect(Collectors.toList()));
		return users.size();
	}

	private List<User> extractUserListFromDocuments(MongoCursor<Document> documentsCursor) {
		ArrayList<User> users = new ArrayList<>();
		while (documentsCursor.hasNext()) {
			users.add(extractUserFromDocument(documentsCursor.next()));
		}
		return users;
	}

	private User extractUserFromDocument(Document document) {
		return new UserBuilder().setId(document.getInteger("id"))
				.setEmail(document.getString("email"))
				.setPassword(document.getString("password"))
				.setLastName(document.getString("last_name"))
				.setFirstName(document.getString("first_name"))
				.setPassportId(document.getString("passport_id"))
				.setPhoneNumber(document.getString("phone_number"))
				.setRole(new RoleBuilder().setName(document.getString("role"))
						.build())
				.build();
	}

	private Document extractDocumentFromUser(User user) {
		return new Document().append("id", user.getId())
				.append("email", user.getEmail())
				.append("password", user.getPassword())
				.append("last_name", user.getLastName())
				.append("first_name", user.getFirstName())
				.append("passport_id", user.getPassportId())
				.append("phone_number", user.getPhoneNumber())
				.append("role", user.getRole()
						.getName());
	}

	private Bson extractUpdatesFromUser(User user) {
		return Updates.combine(Updates.set("password", user.getPassword()), Updates.set("last_name", user.getLastName()),
				Updates.set("first_name", user.getFirstName()), Updates.set("passport_id", user.getPassportId()),
				Updates.set("phone_number", user.getPhoneNumber()));
	}
}
