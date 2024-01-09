package rf.hotel.Employee;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rf.hotel.Main;

import java.io.IOException;

public class EmployeeManager {
    public static void EmployeeManagementUI(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Employee-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 650);
        stage.setTitle("Employee Manager");
        stage.setScene(scene);
        stage.show();
    }
}

