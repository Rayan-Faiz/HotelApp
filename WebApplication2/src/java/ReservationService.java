import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReservationService {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hotel";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
    }

    public boolean isRoomAvailable(String roomType, String bedType) {
        String query = "SELECT rm_number FROM rooms WHERE rm_type = ? AND rm_bed = ? AND rm_status = 1";
        

        try (
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, roomType);
            preparedStatement.setString(2, bedType);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); 
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
            return false;
        }
    }

    public boolean isRoomReserved(String roomNumber, String checkInDate, String checkOutDate) {
        String query = "SELECT rm_number FROM resa WHERE rm_number = ? " +
                "AND ((rs_start >= ? AND rs_end < ?) OR (rs_start > ? AND rs_end <= ?))";

        try (
            Connection connection = getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query)
        ) {
            preparedStatement.setString(1, roomNumber);
            preparedStatement.setString(2, checkInDate);
            preparedStatement.setString(3, checkOutDate);
            preparedStatement.setString(4, checkInDate);
            preparedStatement.setString(5, checkOutDate);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean reserveRoom(int userId, String roomNumber, String checkInDate, String checkOutDate) {
        String insertQuery = "INSERT INTO resa (c_id, rm_number, rs_start, rs_end) VALUES (?, ?, ?, ?)";

        try (
            Connection connection = getConnection();
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery)
        ) {
            insertStatement.setInt(1, userId);
            insertStatement.setString(2, roomNumber);
            insertStatement.setString(3, checkInDate);
            insertStatement.setString(4, checkOutDate);

            
            int rowsAffected = insertStatement.executeUpdate();

            
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
