package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.bson.Document;
import org.bson.conversions.Bson;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.observers.Observer;
import ua.nure.knt.coworking.util.RoomTypeBuilder;
import ua.nure.knt.coworking.util.ServiceBuilder;
import ua.nure.knt.coworking.util.TariffBuilder;
import ua.nure.knt.coworking.util.TimeUnitBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Accumulators.avg;
import static com.mongodb.client.model.Accumulators.max;
import static com.mongodb.client.model.Accumulators.sum;
import static com.mongodb.client.model.Aggregates.group;
import static com.mongodb.client.model.Aggregates.limit;
import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Aggregates.project;
import static com.mongodb.client.model.Aggregates.sort;
import static com.mongodb.client.model.Aggregates.unwind;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lt;
import static com.mongodb.client.model.Filters.lte;
import static com.mongodb.client.model.Filters.size;
import static com.mongodb.client.model.Projections.computed;
import static com.mongodb.client.model.Projections.excludeId;
import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;
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

	@Override
	public List<Document> readRoomTypeCountByPriceRange(Double maxPrice) {
		Bson match = match(lt("price", maxPrice));
		Bson group = group("$room_type", sum("tariffCount", 1));
		Bson project = project(fields(excludeId(), computed("room_type", "$_id"), include("tariffCount")));
		return database.getCollection(TARIFF_COLLECTION)
				.aggregate(Arrays.asList(match, group, project))
				.into(new ArrayList<>());
	}

	@Override
	public Map<String, Long> readRoomTypeCountByPriceRangeWithoutAggregation(Double maxPrice) {
		return database.getCollection(TARIFF_COLLECTION)
				.find(lt("price", maxPrice))
				.projection(fields(excludeId(), include("room_type")))
				.into(new ArrayList<>())
				.stream()
				.collect(Collectors.groupingBy(document -> document.getString("room_type"), Collectors.counting()));
	}

	@Override
	public List<Document> readRoomTypeSumByTimeUnit(String roomType) {
		Bson match = match(eq("room_type", roomType));
		Bson group = group("$time_unit", sum("tariffSum", "$price"));
		Bson project = project(fields(excludeId(), computed("time_unit", "$_id"), include("tariffSum")));
		return database.getCollection(TARIFF_COLLECTION)
				.aggregate(Arrays.asList(match, group, project))
				.into(new ArrayList<>());
	}

	@Override
	public Map<String, Double> readRoomTypeSumByTimeUnitWithoutAggregation(String roomType) {
		return database.getCollection(TARIFF_COLLECTION)
				.find(eq("room_type", roomType))
				.projection(fields(excludeId(), include("time_unit"), include("price")))
				.into(new ArrayList<>())
				.stream()
				.collect(Collectors.groupingBy(document -> document.getString("time_unit"), Collectors.summingDouble(document -> document.getDouble("price"))));
	}

	@Override
	public List<Document> readServiceCountByRange(Integer minServiceUsage) {
		Bson unwind = unwind("$services");
		Bson group = group("$services", sum("serviceCount", 1));
		Bson match = match(gte("serviceCount", minServiceUsage));
		Bson project = project(fields(excludeId(), computed("service", "$_id"), include("serviceCount")));
		return database.getCollection(TARIFF_COLLECTION)
				.aggregate(Arrays.asList(unwind, group, match, project))
				.into(new ArrayList<>());
	}

	@Override
	public Map<String, Long> readServiceCountByRangeWithoutAggregation(Integer minServiceUsage) {
		return database.getCollection(TARIFF_COLLECTION)
				.find()
				.projection(fields(excludeId(), include("services")))
				.into(new ArrayList<>())
				.stream()
				.map(document -> document.getList("services", String.class))
				.flatMap(Collection::stream)
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
				.entrySet()
				.stream()
				.filter(e -> e.getValue() >= minServiceUsage)
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@Override
	public Document readTimeUnitWithMaxAvgPrice() {
		Bson group = group("$time_unit", avg("timeUnitAvg", "$price"));
		Bson project = project(fields(excludeId(), computed("time_unit", "$_id"), include("timeUnitAvg")));
		Bson sort = sort(descending("timeUnitAvg"));
		Bson limit = limit(1);
		return database.getCollection(TARIFF_COLLECTION)
				.aggregate(Arrays.asList(group, project, sort, limit))
				.first();
	}

	@Override
	public Map.Entry<String, Double> readTimeUnitWithMaxAvgPriceWithoutAggregation() {
		return database.getCollection(TARIFF_COLLECTION)
				.find()
				.projection(fields(excludeId(), include("time_unit"), include("price")))
				.into(new ArrayList<>())
				.stream()
				.collect(
						Collectors.groupingBy(document -> document.getString("time_unit"), Collectors.averagingDouble(document -> document.getDouble("price"))))
				.entrySet()
				.stream()
				.max(Map.Entry.comparingByValue())
				.orElse(null);
	}

	@Override
	public List<Document> readMaxPriceByRoomTypeTimeUnitByServiceNumber(Integer serviceNumber) {
		Bson match = match(size("services", serviceNumber));
		Document groupFields = new Document(new HashMap<String, Object>() {{
			put("room_type", "$room_type");
			put("time_unit", "$time_unit");
		}});
		Bson group = group(groupFields, max("tariffMaxPrice", "$price"));
		Bson project = project(
				fields(excludeId(), computed("room_type", "$_id.room_type"), computed("time_unit", "$_id.time_unit"), include("tariffMaxPrice")));
		return database.getCollection(TARIFF_COLLECTION)
				.aggregate(Arrays.asList(match, group, project))
				.into(new ArrayList<>());
	}

	@Override
	public Map<Pair<String, String>, Double> readMaxPriceByRoomTypeTimeUnitByServiceNumberWithoutAggregation(Integer serviceNumber) {
		return database.getCollection(TARIFF_COLLECTION)
				.find(size("services", serviceNumber))
				.projection(fields(excludeId(), include("time_unit"), include("room_type"), include("price")))
				.into(new ArrayList<>())
				.stream()
				.collect(Collectors.groupingBy(document -> new ImmutablePair<>(document.getString("room_type"), document.getString("time_unit")),
						Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(document -> document.getDouble("price"))),
								document -> document.orElse(new Document())
										.getDouble("price"))));
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

	@Override
	public void attach(Observer observer) {

	}

	@Override
	public void detach(Observer observer) {

	}

	@Override
	public void notify(String notificationMessage) {

	}
}
