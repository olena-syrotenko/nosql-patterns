package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.util.PlaceBuilder;
import ua.nure.knt.coworking.util.RoomBuilder;
import ua.nure.knt.coworking.util.RoomTypeBuilder;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class PlaceDaoMongoDb implements PlaceDao {
	private final MongoCollection<Document> placeCollection;

	public PlaceDaoMongoDb(MongoClient mongoClient) {
		this.placeCollection = mongoClient.getDatabase("coworking")
				.getCollection("place");
	}

	@Override
	public List<Place> readPlaceByRoomName(String roomName) throws SQLException {
		MongoCursor<Document> documentsCursor = placeCollection.find(eq("room.name", roomName))
				.cursor();
		return extractPlaceListFromDocuments(documentsCursor);
	}

	@Override
	public List<Place> readAvailablePlace(LocalDate dateFrom, LocalDate dateTo, Integer idRoomType) throws SQLException {
		return null;
	}

	@Override
	public Place readPlaceById(Integer id) throws SQLException {
		Document document = placeCollection.find(eq("number", id))
				.first();
		return document == null ? null : extractPlaceFromDocument(document);
	}

	@Override
	public Integer createPlace(Place place) throws SQLException {
		if (place == null) {
			return null;
		}
		placeCollection.insertOne(extractDocumentFromPlace(place));
		return place.getId();
	}

	@Override
	public Integer updatePlace(Place place) throws SQLException {
		if (place == null) {
			return null;
		}
		UpdateResult updateResult = placeCollection.updateMany(eq("number", place.getId()), extractUpdatesFromPlace(place));
		return Math.toIntExact(updateResult.getModifiedCount());
	}

	@Override
	public Integer deletePlaceById(Integer id) throws SQLException {
		DeleteResult deleteResult = placeCollection.deleteMany(eq("number", id));
		return Math.toIntExact(deleteResult.getDeletedCount());
	}

	private List<Place> extractPlaceListFromDocuments(MongoCursor<Document> documentsCursor) throws SQLException {
		ArrayList<Place> places = new ArrayList<>();
		while (documentsCursor.hasNext()) {
			places.add(extractPlaceFromDocument(documentsCursor.next()));
		}
		return places;
	}

	private Place extractPlaceFromDocument(Document document) throws SQLException {
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
