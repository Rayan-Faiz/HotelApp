package rf.hotel.Resa;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rf.hotel.Main;

import java.io.IOException;

public class ResaManager {
    public static void ResaManagementUI(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Resa-View.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 800);
        stage.setTitle("Reservation Manager");
        stage.setScene(scene);
        stage.show();
    }
}
