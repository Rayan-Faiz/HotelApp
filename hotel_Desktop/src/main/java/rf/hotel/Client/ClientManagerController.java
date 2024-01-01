package rf.hotel.Client;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import rf.hotel.Classes.Clients; // Assuming this class represents clients
import rf.hotel.Classes.Employees;
import rf.hotel.DBConnector;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static rf.hotel.DBConnector.connection;

public class ClientManagerController {
    @FXML
    private TableView<Clients> clientTableView;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Clients, Integer> idColumn;
    @FXML
    private TableColumn<Clients, String> nameColumn;
    @FXML
    private TableColumn<Clients, String> emailColumn;
    @FXML
    private TableColumn<Clients, String> pwdColumn;
    @FXML
    private TableColumn<Clients, String> addrColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField pwdField;
    @FXML
    private TextField addrField;

    static int currentId;

    private ObservableList<Clients> fetchClientData() throws SQLException {
        ObservableList<Clients> clientData = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish a database connection
            connection = DBConnector.getConnections();

            // Query to retrieve client data
            String query = "SELECT * FROM clients";
            preparedStatement = connection.prepareStatement(query);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the results
            while (resultSet.next()) {
                int id = resultSet.getInt("c_id");
                String name = resultSet.getString("c_name");
                String email = resultSet.getString("c_email");
                String pwd = resultSet.getString("c_pwd");
                String addr = resultSet.getString("c_addr");

                // Create Client object from fetched data
                Clients client = new Clients(id, name, email, pwd, addr);
                clientData.add(client);
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

        return clientData;
    }

    public void initialize() throws SQLException {
        // Double clicked elements are put in the textfields
        clientTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Clients selectedClient = clientTableView.getSelectionModel().getSelectedItem();
                if (selectedClient != null) {
                    // Populate your text fields with the selected employee's data
                    currentId = selectedClient.getId();
                    nameField.setText(selectedClient.getName());
                    emailField.setText(selectedClient.getEmail());
                    pwdField.setText(selectedClient.getPwd());
                    addrField.setText(selectedClient.getAddr());
                }
            }
        });

        // Initialize table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        pwdColumn.setCellValueFactory(cellData -> cellData.getValue().pwdProperty());
        addrColumn.setCellValueFactory(cellData -> cellData.getValue().addrProperty());

        Connection connection = DBConnector.getConnections();
        String query ="SELECT * FROM clients";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Clients client = new Clients(resultSet.getInt("c_id"), resultSet.getString("c_name"), resultSet.getString("c_email"), resultSet.getString("c_pwd"), resultSet.getString("c_addr"));
            clientTableView.getItems().add(client);
        }


        DBConnector.closeConnections();
    }

    public Clients getClient() {
        String name = nameField.getText();
        String email = emailField.getText();
        String pwd = pwdField.getText();
        String addr = addrField.getText();
        return new Clients(name, email, pwd, addr);
    }

    public void searchClients(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().toLowerCase().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Clients> filteredList = new FilteredList<>(clientTableView.getItems());

        // Apply a Predicate to filter based on the name property
        filteredList.setPredicate(client ->
                client.getName().toLowerCase().contains(searchQuery));

        // Update the TableView with the filtered data
        clientTableView.setItems(filteredList);
    }

    public void refreshClients() throws SQLException {
        searchField.clear();
        currentId = 0;
        nameField.clear();
        emailField.clear();
        pwdField.clear();
        addrField.clear();
        ObservableList<Clients> updatedClientData = fetchClientData();
        clientTableView.setItems(updatedClientData);
    }

    @FXML
    private void addClient() throws SQLException {
        if (isInputValid()) {
            String INSERT_CLIENT = "INSERT INTO clients (c_name, c_email, c_pwd, c_addr) VALUES (?, ?, ?, ?)";
            Clients client = getClient();
            Connection conn = DBConnector.getConnections();

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(INSERT_CLIENT);
                preparedStatement.setString(1, client.getName());
                preparedStatement.setString(2, client.getEmail());
                preparedStatement.setString(3, client.getPwd());
                preparedStatement.setString(4, client.getAddr());
                // Execute the query to insert the client
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            DBConnector.closeConnections();
            refreshClients();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid values for Name, Email, Password, and Address fields.");
            alert.showAndWait();
        }
    }

    private boolean isInputValid() {
        String name = nameField.getText();
        String email = emailField.getText();
        String pwd = pwdField.getText();
        String addr = addrField.getText();

        // Perform validation based on your criteria
        return !name.isEmpty() && !email.isEmpty() && !pwd.isEmpty() && !addr.isEmpty();
    }

    public void updateClient(ActionEvent actionEvent) {
        if (currentId != 0) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the update query
                String updateQuery = "UPDATE clients SET c_name = ?, c_email = ?, c_pwd = ?, c_addr = ? WHERE c_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                // Set values from text fields to the prepared statement
                preparedStatement.setString(1, nameField.getText());
                preparedStatement.setString(2, emailField.getText());
                preparedStatement.setString(3, pwdField.getText());
                preparedStatement.setString(4, addrField.getText());
                preparedStatement.setInt(5, currentId);

                preparedStatement.executeUpdate();

                // Close resources after the operation (close only when done with all operations)
                preparedStatement.close();
                DBConnector.closeConnections();
                refreshClients();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input/Selection Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select from the table or enter proper inputs.");
            alert.showAndWait();
        }
    }

    public void deleteClient() {
        if (currentId != 0) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the deletion query
                String deleteQuery = "DELETE FROM clients WHERE c_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, currentId);
                preparedStatement.executeUpdate();

                // Close resources after the operation (close only when done with all operations)
                preparedStatement.close();
                DBConnector.closeConnections();
                refreshClients();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
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
}
