package rf.hotel.Room;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import rf.hotel.Classes.Rooms;
import rf.hotel.DBConnector;

import java.sql.*;
import java.time.LocalDate;

import static rf.hotel.DBConnector.connection;

public class RoomManagerController {
    @FXML
    private TableView<Rooms> roomTableView;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Rooms, Integer> numberColumn;
    @FXML
    private TableColumn<Rooms, String> typeColumn;
    @FXML
    private TableColumn<Rooms, String> bedColumn;
    @FXML
    private TableColumn<Rooms, String> statusColumn;
    @FXML
    private TableColumn<Rooms, String> priceColumn;
    @FXML
    private TextField numberField;
    @FXML
    private TextField typeField;
    @FXML
    private TextField bedField;
    @FXML
    private CheckBox statusCheckBox;
    @FXML
    private TextField priceField;

    static int currentId;

    private ObservableList<Rooms> fetchRoomData() throws SQLException {
        ObservableList<Rooms> roomData = FXCollections.observableArrayList();


        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        ResultSet rs =null;

        try {
            // Establish a database connection
            connection = DBConnector.getConnections();

            // Query to retrieve room data
            String query = "SELECT * FROM rooms";
            preparedStatement = connection.prepareStatement(query);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the results
            while (resultSet.next()) {
                int number = resultSet.getInt("rm_number");
                String type = resultSet.getString("rm_type");
                String bed = resultSet.getString("rm_bed");
                Boolean status = resultSet.getBoolean("rm_status");
                Float price = Float.valueOf(resultSet.getString("rm_price"));

                // Verify status
                String q = "SELECT COUNT(*) AS count, rs_end AS endresa FROM resa WHERE rm_number = ?";
                PreparedStatement prepStatement = connection.prepareStatement(q);
                prepStatement.setInt(1, number);
                rs = prepStatement.executeQuery();
                while (rs.next()) {
                    int count = rs.getInt("count");
                    if(rs.getDate("endresa") != null) {
                        LocalDate endResa = rs.getDate("endresa").toLocalDate();
                        if (status == false && count != 0 && endResa.isAfter(LocalDate.now())) {
                            q = "UPDATE rooms SET rm_status = 1 WHERE rm_number = ?";
                            prepStatement = connection.prepareStatement(q);
                            prepStatement.setInt(1, number);
                            prepStatement.executeUpdate();
                            status = true;
                        }
                    }else if (status == true && count == 0) {
                        q = "UPDATE rooms SET rm_status = 0 WHERE rm_number = ?";
                        prepStatement = connection.prepareStatement(q);
                        prepStatement.setInt(1, number);
                        prepStatement.executeUpdate();
                        status = false;
                    }
                }
                // Create Room object from fetched data
                Rooms room = new Rooms(number, type, bed, status, price);
                roomData.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return roomData;
    }

    public void initialize() throws SQLException {
        // Double clicked elements are put in the textfields
        roomTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Rooms selectedRoom = roomTableView.getSelectionModel().getSelectedItem();
                if (selectedRoom != null) {
                    // Populate your text fields with the selected room's data
                    currentId = selectedRoom.getNumber();
                    numberField.setText(String.valueOf(selectedRoom.getNumber()));
                    typeField.setText(selectedRoom.getType());
                    bedField.setText(selectedRoom.getBed());
                    statusCheckBox.setSelected(selectedRoom.getStatus());
                    priceField.setText(String.valueOf(selectedRoom.getPrice()));
                }
            }
        });

        // Initialize table columns
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        bedColumn.setCellValueFactory(cellData -> cellData.getValue().bedProperty());
        statusColumn.setCellValueFactory(cellData ->
                cellData.getValue().getStatus() ?
                        new SimpleStringProperty("Occupied") :
                        new SimpleStringProperty("Vacant")
        );
        priceColumn.setCellValueFactory(cellData -> {
            Rooms room = cellData.getValue();
            FloatBinding priceBinding = Bindings.createFloatBinding(room::getPrice);
            return priceBinding.asString();
        });

        ObservableList<Rooms> roomData = fetchRoomData();
        roomTableView.setItems(roomData);
    }

    public Rooms getRoom() {
        int number = Integer.parseInt(numberField.getText());
        String type = typeField.getText();
        String bed = bedField.getText();
        Boolean status = statusCheckBox.isSelected();
        Float price = Float.valueOf(priceField.getText());
        return new Rooms(number, type, bed, status, price);
    }

    public void searchRooms() {
        String searchQuery = searchField.getText().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Rooms> filteredList = new FilteredList<>(roomTableView.getItems());

        // Apply a Predicate to filter based on the room number property
        filteredList.setPredicate(room ->
                String.valueOf(room.getNumber()).contains(searchQuery));

        // Update the TableView with the filtered data
        roomTableView.setItems(filteredList);
    }

    public void refreshRooms() throws SQLException {
        currentId = 0;
        searchField.clear();
        numberField.clear();
        typeField.clear();
        bedField.clear();
        statusCheckBox.setSelected(false);
        priceField.clear();
        ObservableList<Rooms> updatedRoomData = fetchRoomData();
        roomTableView.setItems(updatedRoomData);
    }

    @FXML
    private void addRoom() throws SQLException {
        if (isInputValid()) {
            String INSERT_ROOM = "INSERT INTO rooms (rm_number, rm_type, rm_bed, rm_status, rm_price) VALUES (?, ?, ?, ?, ?)";
            Rooms room = getRoom();
            Connection conn = DBConnector.getConnections();

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(INSERT_ROOM);
                preparedStatement.setInt(1, room.getNumber());
                preparedStatement.setString(2, room.getType());
                preparedStatement.setString(3, room.getBed());
                preparedStatement.setBoolean(4, room.getStatus());
                preparedStatement.setFloat(5, room.getPrice());
                // Execute the query to insert the room
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println(room.getType());
            }
            DBConnector.closeConnections();
            refreshRooms();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid values for Number, Type, Bed, Status, and Price fields.");
            alert.showAndWait();
        }
    }

    // Validate input fields (if required)
    private boolean isInputValid() {
        int number = Integer.parseInt(numberField.getText());
        String type = typeField.getText();
        String bed = bedField.getText();
        float price = Float.parseFloat(priceField.getText());

        return number != 0 &&!type.isEmpty() && !bed.isEmpty() && price != 0;
    }

    public void updateRoom() {
        if (currentId != 0 && isInputValid()) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the update query
                String updateQuery = "UPDATE rooms SET rm_number = ?, rm_type = ?, rm_bed = ?, rm_status = ?, rm_price = ? WHERE rm_number = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                // Set values from text fields to the prepared statement
                preparedStatement.setInt(1, Integer.parseInt(numberField.getText()));
                preparedStatement.setString(2, typeField.getText());
                preparedStatement.setString(3, bedField.getText());
                preparedStatement.setBoolean(4, statusCheckBox.isSelected());
                preparedStatement.setString(5, priceField.getText());
                preparedStatement.setInt(6, currentId);

                preparedStatement.executeUpdate();

                // Close resources after the operation (close only when done with all operations)
                preparedStatement.close();
                DBConnector.closeConnections();
                refreshRooms();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input/Selection Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select from the table or enter proper inputs.");
            alert.showAndWait();
        }
    }

    public void deleteRoom() {
        if (currentId != 0) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the deletion query
                String deleteQuery = "DELETE FROM rooms WHERE rm_number = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, currentId);
                preparedStatement.executeUpdate();

                // Close resources after the operation (close only when done with all operations)
                preparedStatement.close();
                DBConnector.closeConnections();
                refreshRooms();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select from the table.");
            alert.showAndWait();
        }
    }

    public void quit() {Platform.exit();}
}
