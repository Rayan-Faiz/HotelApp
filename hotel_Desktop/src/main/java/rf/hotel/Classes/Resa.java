package rf.hotel.Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.time.LocalDate;
import java.util.Date;

public class Resa {
    private final SimpleIntegerProperty resaId;
    private final SimpleObjectProperty<LocalDate> startDate;
    private final SimpleObjectProperty<LocalDate> endDate;
    private final SimpleIntegerProperty clientId;
    private final SimpleIntegerProperty roomNumber;



    public Resa(int resaId, LocalDate startDate, LocalDate endDate, int clientId, int roomNumber) {
        this.resaId = new SimpleIntegerProperty(resaId);
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.clientId = new SimpleIntegerProperty(clientId);
        this.roomNumber = new SimpleIntegerProperty(roomNumber);
    }

    public Resa(LocalDate startDate, LocalDate endDate, int c_id, int rm_number) {
        this.resaId = null;
        this.startDate = new SimpleObjectProperty<>(startDate);
        this.endDate = new SimpleObjectProperty<>(endDate);
        this.clientId = new SimpleIntegerProperty(c_id);
        this.roomNumber = new SimpleIntegerProperty(rm_number);
    }

    public SimpleIntegerProperty resaIdProperty() {
        return resaId;
    }

    public SimpleObjectProperty<LocalDate> startDateProperty() {
        return startDate;
    }

    public SimpleObjectProperty<LocalDate> endDateProperty() {
        return endDate;
    }

    public SimpleIntegerProperty clientIdProperty() {return clientId;}

    public SimpleIntegerProperty roomNumberProperty() {
        return roomNumber;
    }

    public int getResaId() {
        return this.resaId.get();
    }

    public LocalDate getStartDate() {
        return this.startDate.get();
    }

    public LocalDate getEndDate() {
        return this.endDate.get();
    }

    public int getClientId() {
        return this.clientId.get();
    }

    public int getRoomNumber() {
        return this.roomNumber.get();
    }
}
