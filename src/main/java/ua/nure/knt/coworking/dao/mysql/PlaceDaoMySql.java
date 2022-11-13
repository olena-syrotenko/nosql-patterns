package ua.nure.knt.coworking.dao.mysql;

import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.entity.Place;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaceDaoMySql implements PlaceDao {
	private final Connection connection;

	// READ
	private static final String GET_PLACE_BY_ROOM_NAME = "SELECT place.id, place.area, room.id, room.name, room_type.name FROM place JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id WHERE room.name = ?";
	private static final String GET_PLACE_BY_ID = "SELECT place.id, place.area, room.id, room.name, room_type.name FROM place JOIN room ON id_room = room.id JOIN room_type ON id_room_type = room_type.id WHERE place.id = ?";

	// CREATE
	private static final String INSERT_PLACE = "INSERT INTO place VALUES(null, (SELECT id FROM room WHERE name = ?), ?)";

	// UPDATE
	private static final String UPDATE_PLACE = "UPDATE place SET area = ? WHERE id = ?";

	// DELETE
	private static final String DELETE_PLACE_BY_ID = "DELETE FROM place WHERE id = ?";

	// PROCEDURE
	private static final String GET_AVAILABLE_PLACE_BY_ROOM_TYPE = "{call AvailablePlace(?,?,?)}";

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
			PreparedStatement ps = connection.prepareStatement(INSERT_PLACE, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, place.getRoom()
					.getName());
			ps.setDouble(2, place.getArea());
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
