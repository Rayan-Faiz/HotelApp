package rf.hotel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import rf.hotel.Employee.EmployeeManager;

import java.io.IOException;

public class MainController {


    public void EmployeeManagerLaunch(ActionEvent actionEvent) throws IOException {
        EmployeeManager.EmployeeManagementUI(new Stage());
    }

    public void ClientManagerLaunch(ActionEvent actionEvent) {
    }

    public void RoomManagerLaunch(ActionEvent actionEvent) {
    }

    public void ResaManagerLaunch(ActionEvent actionEvent) {
    }

    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
