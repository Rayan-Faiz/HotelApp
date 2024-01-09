package rf.hotel.Client;


import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rf.hotel.Main;

import java.io.IOException;

public class ClientManager {
    public static void ClientManagementUI(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Client-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 650);
        stage.setTitle("Client Manager");
        stage.setScene(scene);
        stage.show();
    }
}