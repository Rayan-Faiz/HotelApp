package rf.hotel.Resa;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import rf.hotel.Classes.Resa;
import rf.hotel.DBConnector;

import java.sql.*;
import java.time.LocalDate;

public class ResaManagerController {
    @FXML
    public DatePicker startDatePicker;
    @FXML
    public DatePicker endDatePicker;
    @FXML
    public ComboBox clientComboBox;
    @FXML
    public ComboBox roomComboBox;
    @FXML
    private TableView<Resa> resaTableView;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Resa, Integer> resaIdColumn;
    @FXML
    private TableColumn<Resa, String> startDateColumn;
    @FXML
    private TableColumn<Resa, String> endDateColumn;
    @FXML
    private TableColumn<Resa, Integer> clientIdColumn;
    @FXML
    private TableColumn<Resa, Integer> roomNumberColumn;

    static int currentResaId;

    private ObservableList<Resa> fetchResaData() throws SQLException {
        ObservableList<Resa> resaData = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBConnector.getConnections();

            String query = "SELECT resa.rs_id, resa.rs_start, resa.rs_end, clients.c_id, rooms.rm_number\n" +
                    "FROM resa\n" +
                    "JOIN clients ON resa.c_id = clients.c_id\n" +
                    "JOIN rooms ON resa.rm_number = rooms.rm_number;\n" ;

            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int resaId = resultSet.getInt("rs_id");
                LocalDate startDate = resultSet.getDate("rs_start").toLocalDate();
                LocalDate endDate = resultSet.getDate("rs_end").toLocalDate();
                int clientId = resultSet.getInt("c_id");
                int roomNumber = resultSet.getInt("rm_number");

                // Construct Resa instances with the fetched data
                Resa resa = new Resa(resaId, startDate, endDate, clientId, roomNumber);
                resaData.add(resa);
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

        return resaData;
    }

    // Initialize the controller
    public void initialize() throws SQLException {
        resaTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Resa selectedReservations = resaTableView.getSelectionModel().getSelectedItem();
                if (selectedReservations != null) {
                    currentResaId = selectedReservations.getResaId();
                    startDatePicker.setValue(selectedReservations.getStartDate());
                    endDatePicker.setValue(selectedReservations.getEndDate());
                    clientComboBox.setValue(selectedReservations.getClientId());
                    roomComboBox.setValue(selectedReservations.getRoomNumber());
                }
            }
        });
        clientComboBox.setItems(populateClientComboBox());
        roomComboBox.setItems(populateRoomComboBox());
        // Initialize table columns for the Resa TableView
        resaIdColumn.setCellValueFactory(cellData -> cellData.getValue().resaIdProperty().asObject());
        startDateColumn.setCellValueFactory(cellData -> cellData.getValue().startDateProperty().asString());
        endDateColumn.setCellValueFactory(cellData -> cellData.getValue().endDateProperty().asString());
        clientIdColumn.setCellValueFactory(cellData -> cellData.getValue().clientIdProperty().asObject());
        roomNumberColumn.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());

        ObservableList<Resa> resaData = fetchResaData();
        resaTableView.setItems(resaData);
    }
    private ObservableList<Integer> populateClientComboBox() {
        ObservableList<Integer> clientIds = FXCollections.observableArrayList();

        try (Connection connection = DBConnector.getConnections();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT c_id FROM clients")) {

            while (resultSet.next()) {
                int clientId = resultSet.getInt("c_id");
                clientIds.add(clientId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBConnector.closeConnections();
        }

        return clientIds;
    }

    private ObservableList<Integer> populateRoomComboBox() {
        ObservableList<Integer> roomIds = FXCollections.observableArrayList();

        try (Connection connection = DBConnector.getConnections();
             Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT rm_number FROM rooms");){

            while (resultSet.next()) {
                int roomId = resultSet.getInt("rm_number");
                roomIds.add(roomId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnector.closeConnections();
        }
        return roomIds;
    }

    public Resa getResa() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Integer clientId = (Integer) clientComboBox.getValue();
        Integer roomId = (Integer) roomComboBox.getValue();

        return new Resa(startDate, endDate, clientId, roomId);
    }

    public void searchResa() {
        String searchQuery = searchField.getText().toLowerCase().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Resa> filteredList = new FilteredList<>(resaTableView.getItems());

        // Apply a Predicate to filter based on the properties relevant to Resa
        filteredList.setPredicate(resa ->
                resa.resaIdProperty().toString().contains(searchQuery));

        // Update the TableView with the filtered data
        resaTableView.setItems(filteredList);
    }

    public void refreshResa() throws SQLException {
        searchField.clear();
        currentResaId = 0;
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        clientComboBox.getSelectionModel().clearSelection();
        roomComboBox.getSelectionModel().clearSelection();

        ObservableList<Resa> updatedResaData = fetchResaData();
        resaTableView.setItems(updatedResaData);
    }

    @FXML
    private void addResa() throws SQLException {
        boolean roomIsReserved = false;
        Resa resa = getResa();
        Connection conn = DBConnector.getConnections();
        String query = "SELECT COUNT(*) AS count FROM resa WHERE rm_number = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, resa.getRoomNumber());

        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            roomIsReserved = count > 0;
        }

        resultSet.close();
        pstmt.close();
        if (isInputValid() && !roomIsReserved) {
            String INSERT_RESA = "INSERT INTO resa (rs_start, rs_end, c_id, rm_number) VALUES (?, ?, ?, ?)";

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(INSERT_RESA);
                preparedStatement.setString(1, String.valueOf(resa.getStartDate()));
                preparedStatement.setString(2, String.valueOf(resa.getEndDate()));
                preparedStatement.setString(3, String.valueOf(resa.getClientId()));
                preparedStatement.setString(4, String.valueOf(resa.getRoomNumber()));

                preparedStatement.executeUpdate();
                preparedStatement.close();

                String UPDATE_STATUS = "UPDATE rooms SET rm_status = 1 WHERE rm_number = ?";
                PreparedStatement prepStatement = conn.prepareStatement(UPDATE_STATUS);
                prepStatement.setInt(1, resa.getRoomNumber());
                prepStatement.executeUpdate();
                prepStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBConnector.closeConnections();
            }
            refreshResa();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid values for all required fields.");
            alert.showAndWait();
        }
    }


    private boolean isInputValid() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Integer clientId = (Integer) clientComboBox.getValue();
        Integer roomId = (Integer) roomComboBox.getValue();


        return startDate != null && endDate != null && clientId != null && roomId != null && startDate.isBefore(endDate) && !startDate.isBefore(LocalDate.now());
    }

    public void updateResa() throws SQLException {
        boolean roomIsReserved = false;
        Resa resa = getResa();
        Connection conn = DBConnector.getConnections();
        String query = "SELECT COUNT(*) AS count FROM resa WHERE rm_number = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, resa.getRoomNumber());

        ResultSet resultSet = pstmt.executeQuery();
        if (resultSet.next()) {
            int count = resultSet.getInt("count");
            roomIsReserved = count > 0;
        }

        resultSet.close();
        pstmt.close();
        if (currentResaId != 0 && isInputValid() && !roomIsReserved) {
            try {
                // Prepare and execute the update query
                String updateQuery = "UPDATE resa SET rs_start = ?, rs_end = ?, c_id = ?, rm_number = ? WHERE rs_id = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(updateQuery);

                // Set values from fields to the prepared statement
                preparedStatement.setDate(1, java.sql.Date.valueOf(startDatePicker.getValue()));
                preparedStatement.setDate(2, java.sql.Date.valueOf(endDatePicker.getValue()));
                preparedStatement.setInt(3, (Integer) clientComboBox.getValue());
                preparedStatement.setInt(4, (Integer) roomComboBox.getValue());
                preparedStatement.setInt(5, currentResaId);

                preparedStatement.executeUpdate();
                preparedStatement.close();

                String UPDATE_STATUS = "UPDATE rooms SET rm_status = 1 WHERE rm_number = ?";
                PreparedStatement prepStatement = conn.prepareStatement(UPDATE_STATUS);
                prepStatement.setInt(1, (Integer) roomComboBox.getValue());

                prepStatement.executeUpdate();
                prepStatement.close();

                DBConnector.closeConnections();
                refreshResa();
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


    public void deleteResa() {
        if (currentResaId != 0) {
            try (Connection conn = DBConnector.getConnections();
                 PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM resa WHERE rs_id = ?")) {

                preparedStatement.setInt(1, currentResaId);
                preparedStatement.executeUpdate();

                String UPDATE_STATUS = "UPDATE rooms SET rm_status = 1 WHERE rm_number = (SELECT rm_number FROM resa WHERE rs_id = ?)";
                PreparedStatement prepStatement = conn.prepareStatement(UPDATE_STATUS);
                prepStatement.setInt(1, currentResaId);
                prepStatement.executeUpdate();
                prepStatement.close();

                refreshResa();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Selection Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a reservation from the table.");
            alert.showAndWait();
        }
    }


    @FXML
    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
