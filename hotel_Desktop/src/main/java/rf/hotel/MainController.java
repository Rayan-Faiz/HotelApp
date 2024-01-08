package rf.hotel;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import rf.hotel.Client.ClientManager;
import rf.hotel.Employee.EmployeeManager;
import rf.hotel.Resa.ResaManager;
import rf.hotel.Room.RoomManager;

import java.io.IOException;

public class MainController {


    public void EmployeeManagerLaunch(ActionEvent actionEvent) throws IOException {
        EmployeeManager.EmployeeManagementUI(new Stage());
    }

    public void ClientManagerLaunch(ActionEvent actionEvent) throws IOException {
        ClientManager.ClientManagementUI(new Stage());
    }

    public void RoomManagerLaunch(ActionEvent actionEvent) throws IOException {
        RoomManager.RoomManagementUI(new Stage());
    }

    public void ResaManagerLaunch(ActionEvent actionEvent) throws IOException {
        ResaManager.ResaManagementUI(new Stage());
    }

    public void quit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
