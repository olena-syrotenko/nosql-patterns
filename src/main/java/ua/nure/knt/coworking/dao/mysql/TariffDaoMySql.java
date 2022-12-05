package ua.nure.knt.coworking.dao.mysql;

import org.apache.commons.collections4.CollectionUtils;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.entity.RoomType;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.entity.TimeUnit;
import ua.nure.knt.coworking.util.RoomTypeBuilder;
import ua.nure.knt.coworking.util.ServiceBuilder;
import ua.nure.knt.coworking.util.TariffBuilder;
import ua.nure.knt.coworking.util.TimeUnitBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TariffDaoMySql implements TariffDao {
	private final Connection connection;

	// READ
	private static final String GET_ALL_TARIFFS = "SELECT tariff.id, tariff.name, duration, id_room_type, room_type.name, price FROM tariff JOIN time_unit ON id_time_unit = time_unit.id JOIN room_type ON room_type.id = id_room_type";
	private static final String GET_SERVICE_BY_TARIFF_ID = "SELECT service.name FROM service JOIN tariff_has_service ON id_service = service.id WHERE id_tariff = ?";
	private static final String GET_TARIFF_BY_ROOM_TYPE_NAME = "SELECT tariff.id, tariff.name, duration, id_room_type, room_type.name, price FROM tariff JOIN time_unit ON id_time_unit = time_unit.id JOIN room_type ON room_type.id = id_room_type WHERE room_type.name = ?";
	private static final String GET_TARIFF_BY_TIME_UNIT = "SELECT tariff.id, tariff.name, duration, id_room_type, room_type.name, price FROM tariff JOIN time_unit ON id_time_unit = time_unit.id JOIN room_type ON room_type.id = id_room_type WHERE duration = ?";
	private static final String GET_TARIFF_BY_ID = "SELECT tariff.id, tariff.name, duration, id_room_type, room_type.name, price FROM tariff JOIN time_unit ON id_time_unit = time_unit.id JOIN room_type ON room_type.id = id_room_type WHERE tariff.id = ?";

	// CREATE
	private static final String INSERT_TARIFF = "INSERT INTO tariff VALUES(?, ?, (SELECT id FROM time_unit WHERE duration = ?), (SELECT id FROM room_type WHERE name = ?), ?)";
	private static final String INSERT_TARIFF_SERVICE = "INSERT INTO tariff_has_service VALUES(?, (SELECT id FROM service WHERE name = ?))";

	// UPDATE
	private static final String UPDATE_TARIFF = "UPDATE tariff SET name = ?, price = ? WHERE id = ?";

	// DELETE
	private static final String DELETE_TARIFF_BY_ID = "DELETE FROM tariff WHERE id = ?";
	private static final String DELETE_TARIFF_SERVICE = "DELETE FROM tariff_has_service WHERE id_tariff = ? AND id_service = (SELECT id FROM service WHERE name = ?)";

	// MIGRATION
	private static final String GET_ROOM_TYPE_BY_NAME = "SELECT id, name FROM room_type WHERE name = ?";
	private static final String INSERT_ROOM_TYPE = "INSERT INTO room_type(name) VALUES(?)";
	private static final String GET_TIME_UNIT_BY_DURATION = "SELECT id, duration FROM time_unit WHERE duration = ?";
	private static final String INSERT_TIME_UNIT = "INSERT INTO time_unit(duration) VAlUES(?)";
	private static final String GET_SERVICE_BY_NAME = "SELECT id, name FROM service WHERE name = ?";
	private static final String INSERT_SERVICE = "INSERT INTO service(name) VALUES(?)";

	public TariffDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Tariff> readAllTariffs() throws SQLException {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(GET_ALL_TARIFFS);
			List<Tariff> tariffs = extractTariffListFromResultSet(rs);
			rs.close();
			st.close();
			return tariffs;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<Tariff> readTariffByRoomType(String roomTypeName) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_TARIFF_BY_ROOM_TYPE_NAME);
			ps.setString(1, roomTypeName.toLowerCase(Locale.ROOT));
			ResultSet rs = ps.executeQuery();
			List<Tariff> tariffs = extractTariffListFromResultSet(rs);
			rs.close();
			ps.close();
			return tariffs;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<Tariff> readTariffByTimeUnit(String timeUnit) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_TARIFF_BY_TIME_UNIT);
			ps.setString(1, timeUnit.toLowerCase(Locale.ROOT));
			ResultSet rs = ps.executeQuery();
			List<Tariff> tariffs = extractTariffListFromResultSet(rs);
			rs.close();
			ps.close();
			return tariffs;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Tariff readTariffById(Integer id) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_TARIFF_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			Tariff tariff = null;
			if (rs.next()) {
				tariff = extractTariffFromResultSet(rs);
			}
			rs.close();
			ps.close();
			return tariff;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer createTariff(Tariff tariff) throws SQLException {
		connection.setAutoCommit(false);
		try {
			PreparedStatement ps = connection.prepareStatement(INSERT_TARIFF, Statement.RETURN_GENERATED_KEYS);
			if (tariff.getId() == null) {
				ps.setNull(1, Types.NULL);
			} else {
				ps.setInt(1, tariff.getId());
			}
			ps.setString(2, tariff.getName());
			ps.setString(3, tariff.getTimeUnit()
					.getName());
			ps.setString(4, tariff.getRoomType()
					.getName());
			ps.setDouble(5, tariff.getPrice());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			int insertTariffId = 0;
			if (keys.next()) {
				insertTariffId = keys.getInt(1);
			}

			for (Service service : tariff.getServices()) {
				createTariffService(insertTariffId, service.getName());
			}

			keys.close();
			ps.close();
			connection.commit();
			return insertTariffId;
		} catch (SQLException exception) {
			connection.rollback();
			return null;
		} finally {
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Override
	public Integer updateTariff(Tariff tariff) throws SQLException {
		connection.setAutoCommit(false);
		try {
			PreparedStatement ps = connection.prepareStatement(UPDATE_TARIFF);
			ps.setString(1, tariff.getName());
			ps.setDouble(2, tariff.getPrice());
			ps.setInt(3, tariff.getId());
			int updatedRows = ps.executeUpdate();
			ps.close();

			List<String> services = readTariffServiceByTariffId(tariff.getId()).stream()
					.map(Service::getName)
					.toList();
			List<String> updateServices = tariff.getServices()
					.stream()
					.map(Service::getName)
					.toList();

			List<String> toInsert = CollectionUtils.subtract(updateServices, services)
					.stream()
					.toList();
			for (String service : toInsert) {
				updatedRows += createTariffService(tariff.getId(), service);
			}
			List<String> toDelete = CollectionUtils.subtract(services, updateServices)
					.stream()
					.toList();
			for (String service : toDelete) {
				updatedRows += deleteTariffService(tariff.getId(), service);
			}

			connection.commit();
			return updatedRows;
		} catch (SQLException exception) {
			connection.rollback();
			return null;
		} finally {
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Override
	public Integer deleteTariffById(Integer id) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(DELETE_TARIFF_BY_ID);
			ps.setInt(1, id);

			int deletedRows = ps.executeUpdate();
			ps.close();
			return deletedRows;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer migrate(List<Tariff> tariffs) throws SQLException {
		try {
			for (Tariff tariff : tariffs) {
				readOrInsertRoomType(tariff.getRoomType());
				readOrInsertTimeUnit(tariff.getTimeUnit());
				for (Service service : tariff.getServices()) {
					readOrInsertService(service);
				}
				createTariff(tariff);
			}
			return tariffs.size();
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	private Integer readOrInsertRoomType(RoomType roomType) throws SQLException {
		return readOrInsertCategory(GET_ROOM_TYPE_BY_NAME, INSERT_ROOM_TYPE, roomType.getName());
	}

	private Integer readOrInsertTimeUnit(TimeUnit timeUnit) throws SQLException {
		return readOrInsertCategory(GET_TIME_UNIT_BY_DURATION, INSERT_TIME_UNIT, timeUnit.getName());
	}

	private Integer readOrInsertService(Service service) throws SQLException {
		return readOrInsertCategory(GET_SERVICE_BY_NAME, INSERT_SERVICE, service.getName());
	}

	private Integer readOrInsertCategory(String readStatement, String insertStatement, String categoryName) throws SQLException {
		PreparedStatement readPs = connection.prepareStatement(readStatement);
		readPs.setString(1, categoryName);
		ResultSet readRs = readPs.executeQuery();
		Integer categoryId = null;
		if (readRs.next()) {
			categoryId = readRs.getInt("id");
		} else {
			PreparedStatement insertPs = connection.prepareStatement(insertStatement, Statement.RETURN_GENERATED_KEYS);
			insertPs.setString(1, categoryName);
			insertPs.executeUpdate();
			ResultSet keys = insertPs.getGeneratedKeys();
			if (keys.next()) {
				categoryId = keys.getInt(1);
			}
			keys.close();
			insertPs.close();
		}
		readRs.close();
		readPs.close();
		return categoryId;
	}

	private List<Service> readTariffServiceByTariffId(Integer id) throws SQLException {
		PreparedStatement psService = connection.prepareStatement(GET_SERVICE_BY_TARIFF_ID);
		psService.setInt(1, id);
		List<Service> services = new ArrayList<>();
		ResultSet rsService = psService.executeQuery();
		while (rsService.next()) {
			services.add(extractServiceFromResultSet(rsService));
		}
		rsService.close();
		psService.close();
		return services;
	}

	private int createTariffService(Integer idTariff, String serviceName) throws SQLException {
		PreparedStatement psService = connection.prepareStatement(INSERT_TARIFF_SERVICE);
		psService.setInt(1, idTariff);
		psService.setString(2, serviceName);
		int insertedRows = psService.executeUpdate();
		psService.close();
		return insertedRows;
	}

	private int deleteTariffService(Integer idTariff, String serviceName) throws SQLException {
		PreparedStatement psService = connection.prepareStatement(DELETE_TARIFF_SERVICE);
		psService.setInt(1, idTariff);
		psService.setString(2, serviceName);
		int deletedRows = psService.executeUpdate();
		psService.close();
		return deletedRows;
	}

	private Tariff extractTariffFromResultSet(ResultSet rs) throws SQLException {
		int tariffId = rs.getInt(1);
		List<Service> tariffServices = readTariffServiceByTariffId(tariffId);
		TariffBuilder tariffBuilder = new TariffBuilder().setId(tariffId)
				.setName(rs.getString(2))
				.setTimeUnit(new TimeUnitBuilder().setName(rs.getString(3))
						.build())
				.setRoomType(new RoomTypeBuilder().setId(rs.getInt(4))
						.setName(rs.getString(5))
						.build())
				.setPrice(rs.getDouble(6));
		tariffServices.forEach(tariffBuilder::setService);
		return tariffBuilder.build();
	}

	private Service extractServiceFromResultSet(ResultSet rs) throws SQLException {
		return new ServiceBuilder().setName(rs.getString(1))
				.build();
	}

	private List<Tariff> extractTariffListFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<Tariff> tariffs = new ArrayList<>();
		while (rs.next()) {
			tariffs.add(extractTariffFromResultSet(rs));
		}
		return tariffs;
	}
}
