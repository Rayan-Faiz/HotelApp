package rf.hotel.Classes;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Rooms {
    private final SimpleIntegerProperty number;
    private final SimpleStringProperty type;
    private final SimpleStringProperty bed;
    private final SimpleBooleanProperty status;
    private final SimpleFloatProperty price;

    public Rooms(int number, String type, String bed, Boolean status, Float price) {
        this.number = new SimpleIntegerProperty(number);
        this.type = new SimpleStringProperty(type);
        this.bed = new SimpleStringProperty(bed);
        this.status = new SimpleBooleanProperty(status);
        this.price = new SimpleFloatProperty(price);
    }

    public Rooms(String type, String bed, Boolean status, Float price) {

        number = null;
        this.type = new SimpleStringProperty(type);
        this.bed = new SimpleStringProperty(bed);
        this.status = new SimpleBooleanProperty(status);
        this.price = new SimpleFloatProperty(price);
    }



    public SimpleIntegerProperty numberProperty() {
        return number;
    }
    public SimpleStringProperty typeProperty() {
        return type;
    }
    public SimpleStringProperty bedProperty() {
        return bed;
    }
    public SimpleBooleanProperty statusProperty() {
        return status;
    }
    public SimpleFloatProperty priceProperty() {return price;}
    public int getNumber() {return this.number.get();}
    public String getType() {
        return (this.type.get());
    }
    public String getBed() {
        return (this.bed.get());
    }
    public Boolean getStatus() {
        return (this.status.get());
    }
    public Float getPrice() {
        return (this.price.get());
    }

}
