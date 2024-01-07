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
    static int currentClientId;
    static int currentRoomId;

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
        Integer clientId = (Integer) clientComboBox.getValue(); // Assuming this combo box stores the client ID
        Integer roomId = (Integer) roomComboBox.getValue(); // Assuming this combo box stores the room ID

        // Create and return a new Resa object
        return new Resa(startDate, endDate, clientId, roomId);
    }

    public void searchResa(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().toLowerCase().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Resa> filteredList = new FilteredList<>(resaTableView.getItems());

        // Apply a Predicate to filter based on the properties relevant to Resa
        filteredList.setPredicate(resa ->
                resa.resaIdProperty().toString().contains(searchQuery));

        // Update the TableView with the filtered data
        resaTableView.setItems(filteredList);
    }

    public void refreshReservations() throws SQLException {
        searchField.clear();
        currentResaId = 0;
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        clientComboBox.getSelectionModel().clearSelection();
        roomComboBox.getSelectionModel().clearSelection();

        ObservableList<Resa> updatedResaData = fetchResaData();
        resaTableView.setItems(updatedResaData);
    }

    private boolean isResaInputValid() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Integer clientId = (Integer) clientComboBox.getValue();
        Integer roomId = (Integer) roomComboBox.getValue();

        return startDate != null && endDate != null && clientId != null && roomId != null;
    }


    public void deleteResa() {
        if (currentResaId != 0) {
            try (Connection connection = DBConnector.getConnections();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM resa WHERE resa_id = ?")) {

                preparedStatement.setInt(1, currentResaId);
                preparedStatement.executeUpdate();

                refreshReservations();
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
