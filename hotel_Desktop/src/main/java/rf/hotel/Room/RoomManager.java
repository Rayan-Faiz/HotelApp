package rf.hotel.Room;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rf.hotel.Main;

import java.io.IOException;

public class RoomManager {
    public static void RoomManagementUI(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Room-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 800);
        stage.setTitle("Room Manager");
        stage.setScene(scene);
        stage.show();
    }
}
