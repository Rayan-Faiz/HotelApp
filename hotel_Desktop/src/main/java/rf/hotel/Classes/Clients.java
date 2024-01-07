package rf.hotel.Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Clients {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty firstName;
    private final SimpleStringProperty lastName;
    private final SimpleStringProperty email;
    private final SimpleStringProperty pwd;
    private final SimpleStringProperty addr;

    public Clients(int id, String firstName, String lastName, String email, String pwd, String addr) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.pwd = new SimpleStringProperty(pwd);
        this.addr = new SimpleStringProperty(addr);
    }

    public Clients(String firstName, String lastName, String email, String pwd, String addr) {

        id = null;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.email = new SimpleStringProperty(email);
        this.pwd = new SimpleStringProperty(pwd);
        this.addr = new SimpleStringProperty(addr);
    }



    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public  SimpleStringProperty firstNameProperty() {
        return firstName;
    }
    public  SimpleStringProperty lastNameProperty() {
        return lastName;
    }
    public SimpleStringProperty emailProperty() {
        return email;
    }
    public SimpleStringProperty pwdProperty() {
        return pwd;
    }
    public SimpleStringProperty addrProperty() {return addr;}
    public int getId() {return this.id.get();}
    public String getFirstName() {
        return (this.firstName.get());
    }
    public String getLastName() {
        return (this.lastName.get());
    }
    public String getEmail() {
        return (this.email.get());
    }
    public String getPwd() {
        return (this.pwd.get());
    }
    public String getAddr() {
        return (this.addr.get());
    }


}
