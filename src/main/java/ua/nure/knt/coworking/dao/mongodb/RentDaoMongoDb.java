package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.util.PlaceBuilder;
import ua.nure.knt.coworking.util.RentApplicationBuilder;
import ua.nure.knt.coworking.util.RentPlaceBuilder;
import ua.nure.knt.coworking.util.RoomBuilder;
import ua.nure.knt.coworking.util.StatusBuilder;
import ua.nure.knt.coworking.util.TariffBuilder;
import ua.nure.knt.coworking.util.UserBuilder;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;

public class RentDaoMongoDb implements RentDao {
	private static final String RENT_COLLECTION = "rent_application";
	private final MongoDatabase database;

	public RentDaoMongoDb(MongoClient mongoClient) {
		this.database = mongoClient.getDatabase("coworking");
	}

	@Override
	public List<RentApplication> readAllRentApplication() throws SQLException {
		MongoCursor<Document> rentApplicationsCursor = database.getCollection(RENT_COLLECTION)
				.find()
				.cursor();
		return extractRentApplicationListFromDocuments(rentApplicationsCursor);
	}

	@Override
	public List<RentApplication> readAllRentApplicationByStatus(StatusEnum statusEnum) throws SQLException {
		MongoCursor<Document> rentApplicationsCursor = database.getCollection(RENT_COLLECTION)
				.find(eq("status", statusEnum.getStatus()))
				.cursor();
		return extractRentApplicationListFromDocuments(rentApplicationsCursor);
	}

	@Override
	public List<RentApplication> readAllRentApplicationByUser(String userEmail) throws SQLException {
		MongoCursor<Document> rentApplicationsCursor = database.getCollection(RENT_COLLECTION)
				.find(eq("renter.email", userEmail))
				.cursor();
		return extractRentApplicationListFromDocuments(rentApplicationsCursor);
	}

	@Override
	public List<RentApplication> readAllRentApplicationByUserAndStatus(String userEmail, StatusEnum statusEnum) throws SQLException {
		MongoCursor<Document> rentApplicationsCursor = database.getCollection(RENT_COLLECTION)
				.find(and(eq("renter.email", userEmail), eq("status", statusEnum.getStatus())))
				.cursor();
		return extractRentApplicationListFromDocuments(rentApplicationsCursor);
	}

	@Override
	public RentApplication readRentApplicationById(Integer id) throws SQLException {
		Document document = database.getCollection(RENT_COLLECTION)
				.find(eq("number", id))
				.first();
		return document == null ? null : extractRentApplicationFromDocument(document);
	}

	@Override
	public Integer createRentApplication(RentApplication rentApplication) throws SQLException {
		Document lastId = database.getCollection(RENT_COLLECTION)
				.find()
				.sort(descending("id"))
				.limit(1)
				.first();
		Integer newId = lastId == null ? 1 : lastId.getInteger("number") + 1;
		rentApplication.setId(newId);
		database.getCollection(RENT_COLLECTION)
				.insertOne(extractDocumentFromRentApplication(rentApplication));
		return newId;
	}

	@Override
	public Integer updateRentApplicationStatus(RentApplication rentApplication) throws SQLException {
		UpdateResult updateResult = database.getCollection(RENT_COLLECTION)
				.updateMany(eq("number", rentApplication.getId()), Updates.combine(Updates.set("last_change", convertToDate(LocalDateTime.now())),
						Updates.set("status", rentApplication.getStatus()
								.getName())));
		return Math.toIntExact(updateResult.getModifiedCount());
	}

	@Override
	public Integer migrate(List<RentApplication> rentApplications) throws SQLException {
		database.getCollection(RENT_COLLECTION)
				.insertMany(rentApplications.stream()
						.map(this::extractDocumentFromRentApplication)
						.collect(Collectors.toList()));
		return rentApplications.size();
	}

	private List<RentApplication> extractRentApplicationListFromDocuments(MongoCursor<Document> documentsCursor) {
		ArrayList<RentApplication> rentApplications = new ArrayList<>();
		while (documentsCursor.hasNext()) {
			rentApplications.add(extractRentApplicationFromDocument(documentsCursor.next()));
		}
		return rentApplications;
	}

	private RentApplication extractRentApplicationFromDocument(Document document) {
		RentApplicationBuilder rentApplicationBuilder = new RentApplicationBuilder().setId(document.getInteger("number"))
				.setCreateDate(document.getDate("create_date")
						.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDateTime())
				.setLastChange(document.getDate("last_change")
						.toInstant()
						.atZone(ZoneId.systemDefault())
						.toLocalDateTime())
				.setLeaseAgreement(document.getString("lease_agreement"))
				.setRentAmount(document.getDouble("rent_amount"))
				.setStatus(new StatusBuilder().setName(document.getString("status"))
						.build())
				.setUser(new UserBuilder().setEmail(document.get("renter", Document.class)
								.getString("email"))
						.build());
		document.getList("places", Document.class)
				.forEach(rentPlaceDocument -> rentApplicationBuilder.setRentPlace(new RentPlaceBuilder().setPlace(
								new PlaceBuilder().setId(rentPlaceDocument.getInteger("place_number"))
										.setRoom(new RoomBuilder().setId(rentPlaceDocument.getInteger("room_number"))
												.build())
										.build())
						.setRentStart(rentPlaceDocument.getDate("rent_start")
								.toInstant()
								.atZone(ZoneId.systemDefault())
								.toLocalDate())
						.setRentEnd(rentPlaceDocument.getDate("rent_end")
								.toInstant()
								.atZone(ZoneId.systemDefault())
								.toLocalDate())
						.setTariff(new TariffBuilder().setName(rentPlaceDocument.getString("tariff"))
								.build())
						.build()));
		return rentApplicationBuilder.build();
	}

	private Document extractDocumentFromRentApplication(RentApplication rentApplication) {
		return new Document().append("number", rentApplication.getId())
				.append("create_date", convertToDate(LocalDateTime.now()))
				.append("last_change", convertToDate(LocalDateTime.now()))
				.append("lease_agreement", rentApplication.getLeaseAgreement())
				.append("status", rentApplication.getStatus()
						.getName())
				.append("rent_amount", rentApplication.getRentAmount())
				.append("renter", new Document().append("email", rentApplication.getUser()
								.getEmail())
						.append("passport_id", rentApplication.getUser()
								.getPassportId())
						.append("full_name", rentApplication.getUser()
								.getFirstName() + " " + rentApplication.getUser()
								.getLastName()))
				.append("places", rentApplication.getRentPlaces()
						.stream()
						.map(rentPlace -> new Document().append("place_number", rentPlace.getPlace()
										.getId())
								.append("room_number", rentPlace.getPlace()
										.getRoom()
										.getId())
								.append("rent_start", convertToDate(rentPlace.getRentStart()
										.atStartOfDay()))
								.append("rent_end", convertToDate(rentPlace.getRentEnd()
										.atStartOfDay()))
								.append("tariff", rentPlace.getTariff()
										.getName()))
						.collect(Collectors.toList()));
	}

	private Date convertToDate(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault())
				.toInstant());
	}
}
