package rf.hotel.Employee;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.FloatBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import rf.hotel.Classes.Employees;
import rf.hotel.DBConnector;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static rf.hotel.DBConnector.connection;

public class EmployeeManagerController {
    @FXML
    private TableView<Employees> employeeTableView;
    @FXML
    private TextField searchField;
    @FXML
    private TableColumn<Employees, Integer> idColumn;
    @FXML
    private TableColumn<Employees, String> nameColumn;
    @FXML
    private TableColumn<Employees, String> numColumn;
    @FXML
    private TableColumn<Employees, String> rankColumn;
    @FXML
    private TableColumn<Employees, String> salaryColumn;
    @FXML
    private TextField nameField;
    @FXML
    private TextField numField;
    @FXML
    private TextField rankField;
    @FXML
    private TextField salaryField;

    static int currentId;
    // Method to fetch employee data from the database
    private ObservableList<Employees> fetchEmployeeData() throws SQLException {
        ObservableList<Employees> employeeData = FXCollections.observableArrayList();

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Establish a database connection
            connection = DBConnector.getConnections();

            // Query to retrieve employee data
            String query = "SELECT * FROM employees";
            preparedStatement = connection.prepareStatement(query);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Process the results
            while (resultSet.next()) {
                int id = resultSet.getInt("e_id");
                String name = resultSet.getString("e_name");
                String number = resultSet.getString("e_num");
                String rank = resultSet.getString("e_rank");
                float salary = resultSet.getFloat("e_salary");

                // Create Employee object from fetched data
                Employees employee = new Employees(id, name, number, rank, salary);
                employeeData.add(employee);
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

        return employeeData;
    }
    public void initialize() throws SQLException {
        // Double clicked elements are put in the textfields
        employeeTableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Employees selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
                if (selectedEmployee != null) {
                    // Populate your text fields with the selected employee's data
                    currentId = selectedEmployee.getId();
                    nameField.setText(selectedEmployee.getName());
                    numField.setText(selectedEmployee.getNum());
                    rankField.setText(selectedEmployee.getRank());
                    salaryField.setText(String.valueOf(selectedEmployee.getSalary()));
                }
            }
        });
        // Initialize table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        numColumn.setCellValueFactory(cellData -> cellData.getValue().numProperty());
        rankColumn.setCellValueFactory(cellData -> cellData.getValue().rankProperty());
        salaryColumn.setCellValueFactory(cellData -> {
            Employees employee = cellData.getValue();
            FloatBinding salaryBinding = Bindings.createFloatBinding(employee::getSalary);
            return salaryBinding.asString();
        });
        Connection connection = DBConnector.getConnections();
        String query ="SELECT * FROM employees";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Employees employee = new Employees(resultSet.getInt("e_id"), resultSet.getString("e_name"), resultSet.getString("e_num"), resultSet.getString("e_rank"), resultSet.getFloat("e_salary"));
            employeeTableView.getItems().add(employee);
        }


        DBConnector.closeConnections();
    }

    public Employees getEmployee() {
        // Create an Employee object based on the form fields
        String name = nameField.getText();
        String num = numField.getText();
        String rank = rankField.getText();
        Float salary = Float.valueOf(salaryField.getText());
        return new Employees(name, num, rank, salary);
    }

    public void searchEmployees(ActionEvent actionEvent) {
        String searchQuery = searchField.getText().toLowerCase().trim();

        // Create a FilteredList to filter the TableView based on search criteria
        FilteredList<Employees> filteredList = new FilteredList<>(employeeTableView.getItems());

        // Apply a Predicate to filter based on the name property
        filteredList.setPredicate(employee ->
                employee.getName().toLowerCase().contains(searchQuery));

        // Update the TableView with the filtered data
        employeeTableView.setItems(filteredList);
    }

    public void refreshEmployees() throws SQLException {
        searchField.clear();
        currentId = 0;
        nameField.clear();
        numField.clear();
        rankField.clear();
        salaryField.clear();
        ObservableList<Employees> updatedEmployeeData = fetchEmployeeData();
        employeeTableView.setItems(updatedEmployeeData);
    }

    @FXML
    private void addEmployee() throws SQLException {
        if (isInputValid()) {
            String INSERT_EMPLOYEE = "INSERT INTO employees (e_name, e_num, e_rank, e_salary) VALUES (?, ?, ?, ?)";
            Employees emp = getEmployee();
            Connection conn = DBConnector.getConnections();

            try {
                PreparedStatement preparedStatement = conn.prepareStatement(INSERT_EMPLOYEE);
                preparedStatement.setString(1, emp.getName());
                preparedStatement.setString(2,emp.getNum());
                preparedStatement.setString(3, emp.getRank());
                preparedStatement.setFloat(4, emp.getSalary());
                // Execute the query to insert the employee
                preparedStatement.executeUpdate();
            }catch(SQLException e){
                e.printStackTrace();
                System.out.println(emp.getName());
            }
            DBConnector.closeConnections();
            refreshEmployees();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid values for Name and Number fields.");
            alert.showAndWait();
        }
    }


    // Validate input fields (if required)
    private boolean isInputValid() {
        String name = nameField.getText();
        String num = numField.getText();
        String rank = rankField.getText();
        Float salary = Float.valueOf(salaryField.getText());

        // Perform validation based on your criteria
        return !name.isEmpty() && !num.isEmpty() && !rank.isEmpty() && salary != 0;
    }

    public void updateEmployee() {
        if (currentId != 0 && isInputValid()) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the deletion query
                String deleteQuery = "UPDATE employees SET e_name = ?, e_num = ?, e_rank = ?, e_salary = ? WHERE e_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                // Set values from text fields to the prepared statement
                preparedStatement.setString(1, nameField.getText());
                preparedStatement.setString(2, numField.getText());
                preparedStatement.setString(3, rankField.getText());
                preparedStatement.setFloat(4, Float.parseFloat(salaryField.getText()));
                preparedStatement.setInt(5, currentId);

                preparedStatement.executeUpdate();

                // Close resources after the operation (close only when done with all operations)
                preparedStatement.close();
                DBConnector.closeConnections();
                refreshEmployees();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            currentId = 0;
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Input/Selection Validation Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select from the table or enter proper inputs.");
            alert.showAndWait();
        }
    }

    public void deleteEmployee() {
        if (currentId != 0) {
            try {
                DBConnector.getConnections(); // Re-establish the connection if closed or null
                // Prepare and execute the deletion query
                String deleteQuery = "DELETE FROM employees WHERE e_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, currentId);
                preparedStatement.executeUpdate();

                // Close resources after the operation (close only when done with all operations)
                preparedStatement.close();
                DBConnector.closeConnections();
                refreshEmployees();
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

    public void exportToCSV() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Export to CSV");
        alert.setContentText("Are you sure you want to export this table to a CSV file on your Desktop?");

        // Show the confirmation dialog
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Getting the path to the desktop directory
                    String desktopPath = System.getProperty("user.home") + "/Desktop/";

                    FileWriter csvWriter = new FileWriter(desktopPath + "employees.csv");

                    // Writing headers to the CSV file
                    csvWriter.append("ID,Name,Number,Rank,Salary\n");

                    // Iterating through the TableView data and writing to the CSV file
                    for (Employees employee : employeeTableView.getItems()) {
                        csvWriter.append(String.valueOf(employee.getId()))
                                .append(",")
                                .append(employee.getName())
                                .append(",")
                                .append(employee.getNum())
                                .append(",")
                                .append(employee.getRank())
                                .append(",")
                                .append(String.valueOf(employee.getSalary()))
                                .append("\n");
                    }

                    csvWriter.flush();
                    csvWriter.close();

                    System.out.println("CSV file created successfully at: " + desktopPath);
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception properly
                }
            }
        });
    }

    public void quit() {
        Platform.exit();
    }
}
