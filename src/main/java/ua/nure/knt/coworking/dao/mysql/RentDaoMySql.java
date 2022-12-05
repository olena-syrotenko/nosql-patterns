package ua.nure.knt.coworking.dao.mysql;

import org.apache.commons.lang3.StringUtils;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.entity.RentPlace;
import ua.nure.knt.coworking.entity.Status;
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
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RentDaoMySql implements RentDao {
	private final Connection connection;

	// READ
	private static final String GET_RENT_APPLICATION_BY_STATUS = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, user.passport_id, user.last_name, user.first_name, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE status.name = ?";
	private static final String GET_RENT_APPLICATION_BY_USER = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, user.passport_id, user.last_name, user.first_name, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE user.email = ?";
	private static final String GET_RENT_APPLICATION_BY_USER_AND_STATUS = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, user.passport_id, user.last_name, user.first_name, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE user.email = ? AND status.name = ?";
	private static final String GET_RENT_APPLICATION_BY_ID = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, user.passport_id, user.last_name, user.first_name, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id WHERE rent_application.id = ?";
	private static final String GET_ALL_RENT_APPLICATION = "SELECT rent_application.id, create_date, last_change, lease_agreement, status.name, user.email, user.passport_id, user.last_name, user.first_name, rent_amount FROM rent_application JOIN status ON id_status = status.id JOIN user ON id_user = user.id";
	private static final String GET_RENT_PLACE_BY_RENT_APPLICATION_ID = "SELECT place_id, room.id, room.name, rent_start, rent_end, rent_amount, tariff.name, tariff.price, duration FROM rent_place JOIN place ON place_id = place.id JOIN room ON id_room = room.id JOIN tariff ON tariff_id = tariff.id JOIN time_unit ON id_time_unit = time_unit.id WHERE rent_application_id = ?";

	// CREATE
	private static final String INSERT_RENT_APPLICATION = "INSERT INTO rent_application VALUES(?, ?, ?, ?, (SELECT id FROM status WHERE name = ?), (SELECT id FROM user WHERE email = ?), ?)";
	private static final String INSERT_RENT_PLACE = "INSERT INTO rent_place VALUES(?, ?, ?, ?, ?, (SELECT id FROM tariff WHERE name = ?))";

	// UPDATE
	private static final String UPDATE_RENT_APPLICATION_STATUS = " UPDATE rent_application SET id_status = (SELECT id FROM status WHERE name = ?) WHERE id = ?";

	// MIGRATION
	private static final String GET_STATUS_BY_NAME = "SELECT id, name FROM status WHERE name = ?";
	private static final String INSERT_STATUS = "INSERT INTO status(name) VALUES(?)";

	public RentDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<RentApplication> readAllRentApplication() throws SQLException {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(GET_ALL_RENT_APPLICATION);
			List<RentApplication> rentApplications = extractListRentApplicationFromResultSet(rs);
			rs.close();
			st.close();
			return rentApplications;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
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
			readOrInsertStatus(rentApplication.getStatus());
			PreparedStatement ps = connection.prepareStatement(INSERT_RENT_APPLICATION, Statement.RETURN_GENERATED_KEYS);
			if (rentApplication.getId() == null) {
				ps.setNull(1, Types.NULL);
			} else {
				ps.setInt(1, rentApplication.getId());
			}
			ps.setTimestamp(2, Timestamp.valueOf(Optional.ofNullable(rentApplication.getCreateDate())
					.orElse(LocalDateTime.now())));
			ps.setTimestamp(3, Timestamp.valueOf(Optional.ofNullable(rentApplication.getLastChange())
					.orElse(LocalDateTime.now())));
			ps.setString(4, StringUtils.defaultString(rentApplication.getLeaseAgreement(), " "));
			ps.setString(5, rentApplication.getStatus().getName());
			ps.setString(6, rentApplication.getUser()
					.getEmail());
			if (rentApplication.getRentAmount() == null) {
				ps.setNull(7, Types.NULL);
			} else {
				ps.setDouble(7, rentApplication.getRentAmount());
			}
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();
			int insertRentApplicationId = 0;
			if(rentApplication.getId() != null) {
				insertRentApplicationId = rentApplication.getId();
			}
			else if (keys.next()) {
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
	public Integer updateRentApplicationStatus(RentApplication rentApplication) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(UPDATE_RENT_APPLICATION_STATUS);
			ps.setString(1, rentApplication.getStatus().getName());
			ps.setInt(2, rentApplication.getId());
			int updatedRows = ps.executeUpdate();
			ps.close();
			return updatedRows;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	private Integer readOrInsertStatus(Status status) throws SQLException {
		PreparedStatement readPs = connection.prepareStatement(GET_STATUS_BY_NAME);
		readPs.setString(1, status.getName());
		ResultSet readRs = readPs.executeQuery();
		Integer statusId = null;
		if (readRs.next()) {
			statusId = readRs.getInt("id");
		} else {
			PreparedStatement insertPs = connection.prepareStatement(INSERT_STATUS, Statement.RETURN_GENERATED_KEYS);
			insertPs.setString(1, status.getName());
			insertPs.executeUpdate();
			ResultSet keys = insertPs.getGeneratedKeys();
			if (keys.next()) {
				statusId = keys.getInt(1);
			}
			keys.close();
			insertPs.close();
		}
		readRs.close();
		readPs.close();
		return statusId;
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
		if (rentPlace.getRentAmount() == null) {
			ps.setNull(5, Types.NULL);
		} else {
			ps.setDouble(5, rentPlace.getRentAmount());
		}
		ps.setString(6, rentPlace.getTariff()
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
						.setPrice(rs.getDouble(8))
						.setTimeUnit(new TimeUnitBuilder().setName(rs.getString(9))
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
						.setPassportId(rs.getString(7))
						.setLastName(rs.getString(8))
						.setFirstName(rs.getString(9))
						.build())
				.setRentAmount(rs.getDouble(10));
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
