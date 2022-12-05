package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.util.RoomTypeBuilder;
import ua.nure.knt.coworking.util.ServiceBuilder;
import ua.nure.knt.coworking.util.TariffBuilder;
import ua.nure.knt.coworking.util.TimeUnitBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Sorts.descending;

public class TariffDaoMongoDb implements TariffDao {
	private static final String TARIFF_COLLECTION = "tariff";
	private final MongoDatabase database;

	public TariffDaoMongoDb(MongoClient mongoClient) {
		this.database = mongoClient.getDatabase("coworking");
	}

	@Override
	public List<Tariff> readAllTariffs() throws SQLException {
		MongoCursor<Document> tariffsCursor = database.getCollection(TARIFF_COLLECTION)
				.find()
				.cursor();
		return extractTariffListFromDocuments(tariffsCursor);
	}

	@Override
	public List<Tariff> readTariffByRoomType(String roomTypeName) throws SQLException {
		MongoCursor<Document> tariffsCursor = database.getCollection(TARIFF_COLLECTION)
				.find(eq("room_type", roomTypeName))
				.cursor();
		return extractTariffListFromDocuments(tariffsCursor);
	}

	@Override
	public List<Tariff> readTariffByTimeUnit(String timeUnit) throws SQLException {
		MongoCursor<Document> tariffsCursor = database.getCollection(TARIFF_COLLECTION)
				.find(eq("time_unit", timeUnit))
				.cursor();
		return extractTariffListFromDocuments(tariffsCursor);
	}

	public List<Tariff> readTariffByTimeUnitLowerPrice(String timeUnit, Double maxPrice) {
		MongoCursor<Document> tariffsCursor = database.getCollection(TARIFF_COLLECTION)
				.find(and(eq("time_unit", timeUnit), lte("price", maxPrice)))
				.cursor();
		return extractTariffListFromDocuments(tariffsCursor);
	}

	@Override
	public Tariff readTariffById(Integer id) throws SQLException {
		Document document = database.getCollection(TARIFF_COLLECTION)
				.find(eq("id", id))
				.first();
		return document == null ? null : extractTariffFromDocument(document);
	}

	@Override
	public Integer createTariff(Tariff tariff) throws SQLException {
		if(tariff.getId() == null) {
			Document lastId = database.getCollection(TARIFF_COLLECTION)
					.find()
					.sort(descending("id"))
					.limit(1)
					.first();
			Integer newId = lastId == null ? 1 : lastId.getInteger("id") + 1;
			tariff.setId(newId);
		}
		database.getCollection(TARIFF_COLLECTION)
				.insertOne(extractDocumentFromTariff(tariff));
		return tariff.getId();
	}

	@Override
	public Integer updateTariff(Tariff tariff) throws SQLException {
		UpdateResult updateResult = database.getCollection(TARIFF_COLLECTION)
				.updateOne(eq("name", tariff.getName()), extractUpdatesFromTariff(tariff));
		return Math.toIntExact(updateResult.getModifiedCount());
	}

	@Override
	public Integer deleteTariffById(Integer id) throws SQLException {
		DeleteResult deleteResult = database.getCollection(TARIFF_COLLECTION)
				.deleteMany(eq("id", id));
		return Math.toIntExact(deleteResult.getDeletedCount());
	}

	private List<Tariff> extractTariffListFromDocuments(MongoCursor<Document> documentsCursor) {
		ArrayList<Tariff> tariffs = new ArrayList<>();
		while (documentsCursor.hasNext()) {
			tariffs.add(extractTariffFromDocument(documentsCursor.next()));
		}
		return tariffs;
	}

	private Tariff extractTariffFromDocument(Document document) {
		TariffBuilder tariffBuilder = new TariffBuilder().setId(document.getInteger("id"))
				.setName(document.getString("name"))
				.setPrice(document.getDouble("price"))
				.setRoomType(new RoomTypeBuilder().setName(document.getString("room_type"))
						.build())
				.setTimeUnit(new TimeUnitBuilder().setName(document.getString("time_unit"))
						.build());
		document.getList("services", String.class)
				.forEach(service -> tariffBuilder.setService(new ServiceBuilder().setName(service)
						.build()));
		return tariffBuilder.build();
	}

	private Document extractDocumentFromTariff(Tariff tariff) {
		return new Document().append("id", tariff.getId())
				.append("name", tariff.getName())
				.append("price", tariff.getPrice())
				.append("room_type", tariff.getRoomType()
						.getName())
				.append("time_unit", tariff.getTimeUnit()
						.getName())
				.append("services", tariff.getServices()
						.stream()
						.map(Service::getName)
						.collect(Collectors.toList()));
	}

	private Bson extractUpdatesFromTariff(Tariff tariff) {
		return Updates.combine(Updates.set("price", tariff.getPrice()), Updates.set("services", tariff.getServices()
				.stream()
				.map(Service::getName)
				.collect(Collectors.toList())));
	}
}
