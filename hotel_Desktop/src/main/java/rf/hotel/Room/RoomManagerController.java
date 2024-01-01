package rf.hotel.Room;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
import rf.hotel.Classes.Rooms;
import rf.hotel.DBConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
                Boolean status = Boolean.valueOf(resultSet.getString("rm_status"));
                Float price = Float.valueOf(resultSet.getString("rm_price"));

                // Create Room object from fetched data
                Rooms room = new Rooms(number, type, bed, status, price);
                roomData.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in a finally block
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
                    typeField.setText(selectedRoom.getType());
                    bedField.setText(selectedRoom.getBed());
                    statusCheckBox.setText(String.valueOf(selectedRoom.getStatus()));
                    priceField.setText(String.valueOf(selectedRoom.getPrice()));
                }
            }
        });

        // Initialize table columns
        numberColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        bedColumn.setCellValueFactory(cellData -> cellData.getValue().bedProperty());
        statusColumn.setCellValueFactory(cellData ->
                Bindings.when(cellData.getValue().statusProperty())
                        .then("Occupied")
                        .otherwise("Vacant"));
        priceColumn.setCellValueFactory(cellData -> {
            Rooms room = cellData.getValue();
            FloatBinding priceBinding = Bindings.createFloatBinding(room::getPrice);
            return priceBinding.asString();
        });

        Connection connection = DBConnector.getConnections();
        String query = "SELECT * FROM rooms";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Rooms room = new Rooms(resultSet.getInt("rm_number"), resultSet.getString("rm_type"), resultSet.getString("rm_bed"), resultSet.getBoolean("rm_status"), resultSet.getFloat("rm_price"));
            roomTableView.getItems().add(room);
        }

        DBConnector.closeConnections();
    }

    public Rooms getRoom() {
        Integer number = Integer.valueOf(numberField.getText());
        String type = typeField.getText();
        String bed = bedField.getText();
        Boolean status = Boolean.valueOf(statusCheckBox.getText());
        Float price = Float.valueOf(priceField.getText());
        return new Rooms(number, type, bed, status, price);
    }

    public void searchRooms(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().toLowerCase().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Rooms> filteredList = new FilteredList<>(roomTableView.getItems());

        // Apply a Predicate to filter based on the type property (assuming "type" is a property of Rooms)
        filteredList.setPredicate(room ->
                room.getType().toLowerCase().contains(searchQuery));

        // Update the TableView with the filtered data
        roomTableView.setItems(filteredList);
    }

    public void refreshRooms() throws SQLException {
        searchField.clear();
        currentId = 0;
        typeField.clear();
        bedField.clear();
//        statusCheckBox.clear();
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
                preparedStatement.setString(1, room.getType());
                preparedStatement.setString(2, room.getBed());
                preparedStatement.setBoolean(3, room.getStatus());
                preparedStatement.setFloat(4, room.getPrice());
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
            alert.setContentText("Please enter valid values for Type, Bed, Status, and Price fields.");
            alert.showAndWait();
        }
    }

    // Validate input fields (if required)
    private boolean isInputValid() {
        Integer number = Integer.valueOf(numberField.getText());
        String type = typeField.getText();
        String bed = bedField.getText();
        boolean status = Boolean.parseBoolean(statusCheckBox.getText());
        Float price = Float.valueOf(priceField.getText());

        return number != 0 &&!type.isEmpty() && !bed.isEmpty() && status && price != 0;
    }

    /*public void updateRoom(ActionEvent actionEvent) {
        if (currentId != 0) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the update query
                String updateQuery = "UPDATE rooms SET rm_number = ?, rm_type = ?, rm_bed = ?, rm_status = ?, rm_price = ? WHERE rm_number = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                // Set values from text fields to the prepared statement
                preparedStatement.setInt(1, currentId);
                preparedStatement.setString(2, typeField.getText());
                preparedStatement.setString(3, bedField.getText());
                preparedStatement.setBoolean(4, (if(statusField.getText()=="Occipied"););
                preparedStatement.setString(5, priceField.getText());

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
    }*/

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

    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }

    // Rest of the methods (, , , , updateRoom, ) remain unchanged
    // Replace "Clients" with "Rooms" and appropriately adjust method names and variable references
}
