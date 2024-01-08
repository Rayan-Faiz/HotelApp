package rf.hotel.Client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import rf.hotel.Classes.Clients;
import rf.hotel.DBConnector;

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
    private TableColumn<Clients, String> firstNameColumn;
    @FXML
    private TableColumn<Clients, String> lastNameColumn;
    @FXML
    private TableColumn<Clients, String> emailColumn;
    @FXML
    private TableColumn<Clients, String> pwdColumn;
    @FXML
    private TableColumn<Clients, String> addrColumn;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField pwdField;
    @FXML
    private TextArea addrArea;

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
                String firstname = resultSet.getString("c_firstname");
                String lastname = resultSet.getString("c_lastname");
                String email = resultSet.getString("c_email");
                String pwd = resultSet.getString("c_pwd");
                String addr = resultSet.getString("c_addr");

                // Create Client object from fetched data
                Clients client = new Clients(id, firstname, lastname, email, pwd, addr);
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
                    firstNameField.setText(selectedClient.getFirstName());
                    lastNameField.setText(selectedClient.getLastName());
                    emailField.setText(selectedClient.getEmail());
                    pwdField.setText(selectedClient.getPwd());
                    addrArea.setText(selectedClient.getAddr());
                }
            }
        });

        // Initialize table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        pwdColumn.setCellValueFactory(cellData -> cellData.getValue().pwdProperty());
        addrColumn.setCellValueFactory(cellData -> cellData.getValue().addrProperty());

        Connection connection = DBConnector.getConnections();
        String query ="SELECT * FROM clients";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Clients client = new Clients(resultSet.getInt("c_id"), resultSet.getString("c_firstname"), resultSet.getString("c_lastname"), resultSet.getString("c_email"), resultSet.getString("c_pwd"), resultSet.getString("c_addr"));
            clientTableView.getItems().add(client);
        }


        DBConnector.closeConnections();
    }

    public Clients getClient() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String pwd = pwdField.getText();
        String addr = addrArea.getText();
        return new Clients(firstName, lastName, email, pwd, addr);
    }

    public void searchClients() {
        String searchQuery = searchField.getText().toLowerCase().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Clients> filteredList = new FilteredList<>(clientTableView.getItems());

        // Apply a Predicate to filter based on the name property
        filteredList.setPredicate(client ->
                client.getLastName().toLowerCase().contains(searchQuery));

        // Update the TableView with the filtered data
        clientTableView.setItems(filteredList);
    }

    public void refreshClients() throws SQLException {
        currentId = 0;
        searchField.clear();
        firstNameField.clear();
        lastNameField.clear();
        emailField.clear();
        pwdField.clear();
        addrArea.clear();
        ObservableList<Clients> updatedClientData = fetchClientData();
        clientTableView.setItems(updatedClientData);
    }

    @FXML
    private void addClient() throws SQLException {
        if (isInputValid()) {
            String INSERT_CLIENT = "INSERT INTO clients (c_firstname, c_lastname, c_email, c_pwd, c_addr) VALUES (?, ?, ?, ?, ?)";
            Clients client = getClient();
            Connection conn = DBConnector.getConnections();

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(INSERT_CLIENT);
                preparedStatement.setString(1, client.getFirstName());
                preparedStatement.setString(2, client.getLastName());
                preparedStatement.setString(3, client.getEmail());
                preparedStatement.setString(4, client.getPwd());
                preparedStatement.setString(5, client.getAddr());
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

    public void updateClient(ActionEvent actionEvent) {
        if (currentId != 0 && isInputValid()) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the update query
                String updateQuery = "UPDATE clients SET c_name = ?, c_email = ?, c_pwd = ?, c_addr = ? WHERE c_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                // Set values from text fields to the prepared statement
                preparedStatement.setString(1, firstNameField.getText());
                preparedStatement.setString(2, lastNameField.getText());
                preparedStatement.setString(3, emailField.getText());
                preparedStatement.setString(4, pwdField.getText());
                preparedStatement.setString(5, addrArea.getText());
                preparedStatement.setInt(6, currentId);

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

    private boolean isInputValid() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String pwd = pwdField.getText();
        String addr = addrArea.getText();

        return !firstName.isEmpty() && !lastName.isEmpty() && !email.isEmpty() && !pwd.isEmpty() && !addr.isEmpty();
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
