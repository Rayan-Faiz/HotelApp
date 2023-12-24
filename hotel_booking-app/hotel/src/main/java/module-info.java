module rl.hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens rl.hotel to javafx.fxml;
    exports rl.hotel;
}