package rl.hotel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.sql.*;

public class HotelBackOffice extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Inside your HotelBackOffice class
        try {
            Connection connection = DatabaseConnector.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Create main layout components
        BorderPane root = new BorderPane();
        VBox menu = new VBox();
        VBox content = new VBox();

        // Title Label
        Label titleLabel = new Label("Hotel Management");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        titleLabel.setPadding(new Insets(169, 0, 0, 0));

        // Create menu buttons
        Button manageEmployeesBtn = new Button("Manage Employees");
        Button manageClientsBtn = new Button("Manage Clients");
        Button manageRoomsBtn = new Button("Manage Rooms");
        // ... other menu buttons

        // Set button actions
        manageEmployeesBtn.setOnAction(event -> openEmployeeManagementView());
        // Similar actions for other buttons

        // Add buttons to menu
        menu.getChildren().addAll(
                manageEmployeesBtn,
                manageClientsBtn,
                manageRoomsBtn
                // ... add other buttons
        );

        // Set the layout
        menu.setAlignment(Pos.CENTER);
        menu.setPadding(new Insets(100));
        menu.setSpacing(20);
        root.setCenter(menu);
        root.setTop(titleLabel); // Place the title label at the top
        BorderPane.setAlignment(titleLabel, Pos.CENTER); // Center the label

        // Create the scene and set it to the stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Hotel Back Office");
        primaryStage.show();
    }

    // Method to open the employee management view
    private void openEmployeeManagementView() {
        // Create a new stage for the employee management view
        Stage employeeStage = new Stage();
        // Create and set the content for the employee management view
        VBox employeeRoot = new VBox();
        Scene employeeScene = new Scene(employeeRoot, 600, 400);
        employeeStage.setScene(employeeScene);
        employeeStage.setTitle("Employee Management");
        employeeStage.show();
    }
    // Other methods for managing employees, clients, rooms, etc.
    // You'll need to create separate views and logic for each functionality


}
