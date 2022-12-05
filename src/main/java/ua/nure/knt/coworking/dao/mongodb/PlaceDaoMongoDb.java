package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.util.PlaceBuilder;
import ua.nure.knt.coworking.util.RoomBuilder;
import ua.nure.knt.coworking.util.RoomTypeBuilder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.ne;
import static com.mongodb.client.model.Filters.or;

public class PlaceDaoMongoDb implements PlaceDao {
	private static final String PLACE_COLLECTION = "place";
	private static final String RENT_COLLECTION = "rent_application";
	private final MongoDatabase database;

	public PlaceDaoMongoDb(MongoClient mongoClient) {
		this.database = mongoClient.getDatabase("coworking");
	}

	@Override
	public List<Place> readPlaceByRoomName(String roomName) throws SQLException {
		MongoCursor<Document> documentsCursor = database.getCollection(PLACE_COLLECTION)
				.find(eq("room.name", roomName))
				.cursor();
		return extractPlaceListFromDocuments(documentsCursor);
	}

	@Override
	public List<Place> readAvailablePlace(LocalDate dateFrom, LocalDate dateTo, Integer idRoomType) throws SQLException {
		List<Place> places = extractPlaceListFromDocuments(database.getCollection(PLACE_COLLECTION)
				.find()
				.cursor());
		List<Integer> rentedPlaces = extractPlaceNumberListFromDocuments(database.getCollection(RENT_COLLECTION)
				.find(and(or(ne("status", StatusEnum.CANCELLED), ne("status", StatusEnum.REJECTED)), lte("places.rent_start", Date.from(dateTo.atStartOfDay()
						.atZone(ZoneId.systemDefault())
						.toInstant())), gte("places.rent_end", Date.from(dateFrom.atStartOfDay()
						.atZone(ZoneId.systemDefault())
						.toInstant()))))
				.cursor());
		return places.stream()
				.filter(place -> !rentedPlaces.contains(place.getId()))
				.collect(Collectors.toList());
	}

	@Override
	public List<Place> readAllPlaces() throws SQLException {
		MongoCursor<Document> documentsCursor = database.getCollection(PLACE_COLLECTION)
				.find()
				.cursor();
		return extractPlaceListFromDocuments(documentsCursor);
	}

	@Override
	public Place readPlaceById(Integer id) throws SQLException {
		Document document = database.getCollection(PLACE_COLLECTION)
				.find(eq("number", id))
				.first();
		return document == null ? null : extractPlaceFromDocument(document);
	}

	@Override
	public Integer createPlace(Place place) throws SQLException {
		database.getCollection(PLACE_COLLECTION)
				.insertOne(extractDocumentFromPlace(place));
		return place.getId();
	}

	@Override
	public Integer updatePlace(Place place) throws SQLException {
		UpdateResult updateResult = database.getCollection(PLACE_COLLECTION)
				.updateMany(eq("number", place.getId()), extractUpdatesFromPlace(place));
		return Math.toIntExact(updateResult.getModifiedCount());
	}

	@Override
	public Integer deletePlaceById(Integer id) throws SQLException {
		DeleteResult deleteResult = database.getCollection(PLACE_COLLECTION)
				.deleteMany(eq("number", id));
		return Math.toIntExact(deleteResult.getDeletedCount());
	}

	private List<Place> extractPlaceListFromDocuments(MongoCursor<Document> documentsCursor) {
		ArrayList<Place> places = new ArrayList<>();
		while (documentsCursor.hasNext()) {
			places.add(extractPlaceFromDocument(documentsCursor.next()));
		}
		return places;
	}

	private List<Integer> extractPlaceNumberListFromDocuments(MongoCursor<Document> documentsCursor) {
		ArrayList<Integer> placeNumbers = new ArrayList<>();
		while (documentsCursor.hasNext()) {
			placeNumbers.addAll(documentsCursor.next()
					.getList("places", Document.class)
					.stream()
					.map(place -> place.getInteger("place_number"))
					.toList());
		}
		return placeNumbers;
	}

	private Place extractPlaceFromDocument(Document document) {
		Document roomDocument = document.get("room", Document.class);
		return new PlaceBuilder().setId(document.getInteger("number"))
				.setArea(document.getDouble("area"))
				.setRoom(new RoomBuilder().setId(roomDocument.getInteger("number"))
						.setName(roomDocument.getString("name"))
						.setRoomType(new RoomTypeBuilder().setName(roomDocument.getString("type"))
								.build())
						.build())
				.build();
	}

	private Document extractDocumentFromPlace(Place place) {
		return new Document().append("number", place.getId())
				.append("area", place.getArea())
				.append("room", new Document().append("number", place.getRoom()
								.getId())
						.append("name", place.getRoom()
								.getName())
						.append("type", place.getRoom()
								.getRoomType()
								.getName()));
	}

	private Bson extractUpdatesFromPlace(Place place) {
		return Updates.combine(Updates.set("area", place.getArea()), Updates.set("room.number", place.getRoom()
				.getId()), Updates.set("room.name", place.getRoom()
				.getName()), Updates.set("room.type", place.getRoom()
				.getRoomType()
				.getName()));
	}
}
