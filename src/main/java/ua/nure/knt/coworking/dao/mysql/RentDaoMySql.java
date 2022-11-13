package ua.nure.knt.coworking.dao.mysql;

import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.entity.RentPlace;
import ua.nure.knt.coworking.util.PlaceBuilder;
import ua.nure.knt.coworking.util.RentApplicationBuilder;
import ua.nure.knt.coworking.util.RentPlaceBuilder;
import ua.nure.knt.coworking.util.RoomBuilder;
import ua.nure.knt.coworking.util.StatusBuilder;
import ua.nure.knt.coworking.util.TariffBuilder;
import ua.nure.knt.coworking.util.TimeUnitBuilder;
import ua.nure.knt.coworking.util.UserBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RentDaoMySql implements RentDao {
	private final Connection connection;

	// READ
	private static final String GET_RENT_APPLICATION_BY_STATUS = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE status.name = ?";
	private static final String GET_RENT_APPLICATION_BY_USER = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE user.email = ?";
	private static final String GET_RENT_APPLICATION_BY_USER_AND_STATUS = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE user.email = ? AND status.name = ?";
	private static final String GET_RENT_APPLICATION_BY_ID = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE rent_application.id = ?";
	private static final String GET_RENT_PLACE_BY_RENT_APPLICATION_ID = "SELECT place_id, room.id, room.name, rent_start, rent_end, rent_amount, tariff.name, tariff.price, duration FROM rent_place JOIN place ON place_id = place.id JOIN room ON id_room = room.id JOIN tariff ON tariff_id = tariff.id JOIN time_unit ON id_time_unit = time_unit.id WHERE rent_application_id = ?";

	// CREATE
	private static final String INSERT_RENT_APPLICATION = "INSERT INTO rent_application VALUES(null, null, null, null, (SELECT id FROM status WHERE name = ?), (SELECT id FROM user WHERE email = ?), null)";
	private static final String INSERT_RENT_PLACE = "INSERT INTO rent_place VALUES(?, ?, ?, ?, null, (SELECT id FROM tariff WHERE name = ?))";

	// UPDATE
	private static final String UPDATE_RENT_APPLICATION_STATUS = " UPDATE rent_application SET id_status = (SELECT id FROM status WHERE name = ?) WHERE id = ?";

	public RentDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<RentApplication> readAllRentApplicationByStatus(StatusEnum statusEnum) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_RENT_APPLICATION_BY_STATUS);
			ps.setString(1, statusEnum.getStatus());
			ResultSet rs = ps.executeQuery();
			List<RentApplication> rentApplications = extractListRentApplicationFromResultSet(rs);
			rs.close();
			ps.close();
			return rentApplications;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<RentApplication> readAllRentApplicationByUser(String userEmail) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_RENT_APPLICATION_BY_USER);
			ps.setString(1, userEmail);
			ResultSet rs = ps.executeQuery();
			List<RentApplication> rentApplications = extractListRentApplicationFromResultSet(rs);
			rs.close();
			ps.close();
			return rentApplications;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<RentApplication> readAllRentApplicationByUserAndStatus(String userEmail, StatusEnum statusEnum) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_RENT_APPLICATION_BY_USER_AND_STATUS);
			ps.setString(1, userEmail);
			ps.setString(2, statusEnum.getStatus());
			ResultSet rs = ps.executeQuery();
			List<RentApplication> rentApplications = extractListRentApplicationFromResultSet(rs);
			rs.close();
			ps.close();
			return rentApplications;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public RentApplication readRentApplicationById(Integer id) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_RENT_APPLICATION_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			RentApplication rentApplication = null;
			if (rs.next()) {
				rentApplication = extractRentApplicationFromResultSet(rs);
			}
			rs.close();
			ps.close();
			return rentApplication;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer createRentApplication(RentApplication rentApplication) throws SQLException {
		connection.setAutoCommit(false);
		try {
			PreparedStatement ps = connection.prepareStatement(INSERT_RENT_APPLICATION, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, StatusEnum.NEW.getStatus());
			ps.setString(2, rentApplication.getUser()
					.getEmail());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			int insertRentApplicationId = 0;
			if (keys.next()) {
				insertRentApplicationId = keys.getInt(1);
			}

			for (RentPlace rentPlace : rentApplication.getRentPlaces()) {
				createRentPlace(rentPlace, insertRentApplicationId);
			}

			keys.close();
			ps.close();
			connection.commit();
			return insertRentApplicationId;
		} catch (SQLException exception) {
			connection.rollback();
			return null;
		} finally {
			connection.setAutoCommit(true);
			connection.close();
		}
	}

	@Override
	public Integer updateRentApplicationStatus(StatusEnum statusEnum) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(UPDATE_RENT_APPLICATION_STATUS);
			ps.setString(1, statusEnum.getStatus());
			int updatedRows = ps.executeUpdate();
			ps.close();
			return updatedRows;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	private List<RentPlace> readRentPlaceByRentApplicationId(Integer rentApplicationId) throws SQLException {
		PreparedStatement psRentPlace = connection.prepareStatement(GET_RENT_PLACE_BY_RENT_APPLICATION_ID);
		psRentPlace.setInt(1, rentApplicationId);
		List<RentPlace> rentPlaces = new ArrayList<>();
		ResultSet rsRentPlace = psRentPlace.executeQuery();
		while (rsRentPlace.next()) {
			rentPlaces.add(extractRentPlaceFromResultSet(rsRentPlace));
		}
		rsRentPlace.close();
		psRentPlace.close();
		return rentPlaces;
	}

	private int createRentPlace(RentPlace rentPlace, Integer idRentApplication) throws SQLException {
		PreparedStatement ps = connection.prepareStatement(INSERT_RENT_PLACE);
		ps.setInt(1, rentPlace.getPlace()
				.getId());
		ps.setInt(2, idRentApplication);
		ps.setTimestamp(3, Timestamp.valueOf(rentPlace.getRentStart()
				.atStartOfDay()));
		ps.setTimestamp(4, Timestamp.valueOf(rentPlace.getRentEnd()
				.atStartOfDay()));
		ps.setString(5, rentPlace.getTariff()
				.getName());
		int insertedRows = ps.executeUpdate();
		ps.close();
		return insertedRows;
	}

	private RentPlace extractRentPlaceFromResultSet(ResultSet rs) throws SQLException {
		return new RentPlaceBuilder().setPlace(new PlaceBuilder().setId(rs.getInt(1))
						.setRoom(new RoomBuilder().setId(rs.getInt(2))
								.setName(rs.getString(3))
								.build())
						.build())
				.setRentStart(rs.getTimestamp(4)
						.toLocalDateTime()
						.toLocalDate())
				.setRentEnd(rs.getTimestamp(5)
						.toLocalDateTime()
						.toLocalDate())
				.setRentAmount(rs.getDouble(6))
				.setTariff(new TariffBuilder().setName(rs.getString(7))
						.setPrice(rs.getDouble(7))
						.setTimeUnit(new TimeUnitBuilder().setName(rs.getString(8))
								.build())
						.build())
				.build();
	}

	private RentApplication extractRentApplicationFromResultSet(ResultSet rs) throws SQLException {
		int rentApplicationId = rs.getInt(1);
		List<RentPlace> rentPlaces = readRentPlaceByRentApplicationId(rentApplicationId);
		RentApplicationBuilder rentApplicationBuilder = new RentApplicationBuilder().setId(rentApplicationId)
				.setCreateDate(rs.getTimestamp(2)
						.toLocalDateTime())
				.setLastChange(rs.getTimestamp(3)
						.toLocalDateTime())
				.setLeaseAgreement(rs.getString(4))
				.setStatus(new StatusBuilder().setName(rs.getString(5))
						.build())
				.setUser(new UserBuilder().setEmail(rs.getString(6))
						.build())
				.setRentAmount(rs.getDouble(7));
		rentPlaces.forEach(rentApplicationBuilder::setRentPlace);
		return rentApplicationBuilder.build();
	}

	private List<RentApplication> extractListRentApplicationFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<RentApplication> rentApplications = new ArrayList<>();
		while (rs.next()) {
			rentApplications.add(extractRentApplicationFromResultSet(rs));
		}
		return rentApplications;
	}
}
