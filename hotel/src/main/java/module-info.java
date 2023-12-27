module rf.hotel {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.sql;

    opens rf.hotel to javafx.fxml;
    exports rf.hotel;
    opens rf.hotel.Classes to javafx.fxml;
    exports rf.hotel.Classes;
    exports rf.hotel.Employee;
    opens rf.hotel.Employee to javafx.fxml;
    exports rf.hotel.Client;
    opens rf.hotel.Client to javafx.fxml;
}