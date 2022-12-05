package ua.nure.knt.coworking.dao.mysql;

import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.entity.Room;
import ua.nure.knt.coworking.entity.RoomType;
import ua.nure.knt.coworking.util.PlaceBuilder;
import ua.nure.knt.coworking.util.RoomBuilder;
import ua.nure.knt.coworking.util.RoomTypeBuilder;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class PlaceDaoMySql implements PlaceDao {
	private final Connection connection;

	// READ
	private static final String GET_PLACE_BY_ROOM_NAME = "SELECT place.id, place.area, room.id, room.name, room_type.name FROM place JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id WHERE room.name = ?";
	private static final String GET_PLACE_BY_ID = "SELECT place.id, place.area, room.id, room.name, room_type.name FROM place JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id WHERE place.id = ?";
	private static final String GET_ALL_PLACE = "SELECT place.id, place.area, room.id, room.name, room_type.name FROM place JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id";

	// CREATE
	private static final String INSERT_PLACE = "INSERT INTO place VALUES(?, (SELECT id FROM room WHERE name = ?), ?)";

	// UPDATE
	private static final String UPDATE_PLACE = "UPDATE place SET area = ? WHERE id = ?";

	// DELETE
	private static final String DELETE_PLACE_BY_ID = "DELETE FROM place WHERE id = ?";

	// PROCEDURE
	private static final String GET_AVAILABLE_PLACE_BY_ROOM_TYPE = "{call AvailablePlace(?,?,?)}";

	// MIGRATION
	private static final String GET_ROOM_TYPE_BY_NAME = "SELECT id, name FROM room_type WHERE name = ?";
	private static final String INSERT_ROOM_TYPE = "INSERT INTO room_type(name) VALUES(?)";
	private static final String GET_ROOM_BY_NAME = "SELECT id, name FROM room WHERE name = ?";
	private static final String INSERT_ROOM = "INSERT INTO room VALUES(?,?,?,?,(SELECT id FROM room_type WHERE room_type.name = ?)) ON DUPLICATE KEY UPDATE name = VALUES(name), id_room_type = VALUES(id_room_type)";

	public PlaceDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public List<Place> readPlaceByRoomName(String roomName) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_PLACE_BY_ROOM_NAME);
			ps.setString(1, roomName);
			ResultSet rs = ps.executeQuery();

			List<Place> places = extractPlaceListFromResultSet(rs);
			rs.close();
			ps.close();
			return places;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<Place> readAvailablePlace(LocalDate dateFrom, LocalDate dateTo, Integer idRoomType) throws SQLException {
		try {
			CallableStatement cs = connection.prepareCall(GET_AVAILABLE_PLACE_BY_ROOM_TYPE);
			cs.setTimestamp(1, Timestamp.valueOf(dateFrom.atStartOfDay()));
			cs.setTimestamp(2, Timestamp.valueOf(dateTo.atStartOfDay()));
			cs.setInt(3, idRoomType);

			ResultSet rs = cs.executeQuery();
			List<Place> places = extractPlaceListFromResultSet(rs);
			rs.close();
			cs.close();
			return places;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public List<Place> readAllPlaces() throws SQLException {
		try {
			Statement st = connection.createStatement();
			ResultSet rs = st.executeQuery(GET_ALL_PLACE);

			List<Place> places = extractPlaceListFromResultSet(rs);
			rs.close();
			st.close();
			return places;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Place readPlaceById(Integer id) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(GET_PLACE_BY_ID);
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			Place place = null;
			if (rs.next()) {
				place = extractPlaceFromResultSet(rs);
			}
			rs.close();
			ps.close();
			return place;
		} catch (SQLException ex) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer createPlace(Place place) throws SQLException {
		try {
			readOrInsertRoomType(place.getRoom()
					.getRoomType());
			readOrInsertRoom(place.getRoom());
			PreparedStatement ps = connection.prepareStatement(INSERT_PLACE, Statement.RETURN_GENERATED_KEYS);
			if (place.getId() == null) {
				ps.setNull(1, Types.NULL);
			} else {
				ps.setInt(1, place.getId());
			}
			ps.setString(2, place.getRoom()
					.getName());
			ps.setDouble(3, place.getArea());
			ps.executeUpdate();
			ResultSet keys = ps.getGeneratedKeys();

			if (keys.next()) {
				return keys.getInt(1);
			}
			keys.close();
			ps.close();
			return 0;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer updatePlace(Place place) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(UPDATE_PLACE);
			ps.setDouble(1, place.getArea());
			ps.setInt(2, place.getId());

			int updatedRows = ps.executeUpdate();
			ps.close();
			return updatedRows;
		} catch (SQLException exception) {
			return null;
		} finally {
			connection.close();
		}
	}

	@Override
	public Integer deletePlaceById(Integer id) throws SQLException {
		try {
			PreparedStatement ps = connection.prepareStatement(DELETE_PLACE_BY_ID);
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

	private Integer readOrInsertRoomType(RoomType roomType) throws SQLException {
		PreparedStatement readPs = connection.prepareStatement(GET_ROOM_TYPE_BY_NAME);
		readPs.setString(1, roomType.getName());
		ResultSet readRs = readPs.executeQuery();
		Integer roomTypeId = null;
		if (readRs.next()) {
			roomTypeId = readRs.getInt("id");
		} else {
			PreparedStatement insertPs = connection.prepareStatement(INSERT_ROOM_TYPE, Statement.RETURN_GENERATED_KEYS);
			insertPs.setString(1, roomType.getName());
			insertPs.executeUpdate();
			ResultSet keys = insertPs.getGeneratedKeys();
			if (keys.next()) {
				roomTypeId = keys.getInt(1);
			}
			keys.close();
			insertPs.close();
		}
		readRs.close();
		readPs.close();
		return roomTypeId;
	}

	private Integer readOrInsertRoom(Room room) throws SQLException {
		PreparedStatement readPs = connection.prepareStatement(GET_ROOM_BY_NAME);
		readPs.setString(1, room.getName());
		ResultSet readRs = readPs.executeQuery();
		Integer roomId = null;
		if (readRs.next()) {
			roomId = readRs.getInt("id");
		}
		if (!Objects.equals(roomId, room.getId())) {
			PreparedStatement insertPs = connection.prepareStatement(INSERT_ROOM);
			if (room.getId() == null) {
				insertPs.setNull(1, Types.NULL);
			} else {
				insertPs.setInt(1, room.getId());
			}
			insertPs.setString(2, room.getName());
			insertPs.setDouble(3, Optional.ofNullable(room.getArea())
					.orElse(0.0));
			insertPs.setInt(4, Optional.ofNullable(room.getMaxPlaces())
					.orElse(1));
			insertPs.setString(5, room.getRoomType()
					.getName());
			insertPs.executeUpdate();
			insertPs.close();
			roomId = room.getId();
		}
		readRs.close();
		readPs.close();
		return roomId;
	}

	private List<Place> extractPlaceListFromResultSet(ResultSet rs) throws SQLException {
		ArrayList<Place> places = new ArrayList<>();
		while (rs.next()) {
			places.add(extractPlaceFromResultSet(rs));
		}
		return places;
	}

	private Place extractPlaceFromResultSet(ResultSet rs) throws SQLException {
		return new PlaceBuilder().setId(rs.getInt(1))
				.setArea(rs.getDouble(2))
				.setRoom(new RoomBuilder().setId(rs.getInt(3))
						.setName(rs.getString(4))
						.setRoomType(new RoomTypeBuilder().setName(rs.getString(5))
								.build())
						.build())
				.build();
	}
}
