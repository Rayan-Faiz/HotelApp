package rf.hotel.Classes;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Clients {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty email;
    private final SimpleStringProperty pwd;
    private final SimpleStringProperty addr;

    public Clients(int id, String name, String email, String pwd, String addr) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.pwd = new SimpleStringProperty(pwd);
        this.addr = new SimpleStringProperty(addr);
    }

    public Clients(String name, String email, String pwd, String addr) {

        id = null;
        this.name = new SimpleStringProperty(name);
        this.email = new SimpleStringProperty(email);
        this.pwd = new SimpleStringProperty(pwd);
        this.addr = new SimpleStringProperty(addr);
    }



    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public  SimpleStringProperty nameProperty() {
        return name;
    }
    public SimpleStringProperty emailProperty() {
        return email;
    }
    public SimpleStringProperty pwdProperty() {
        return pwd;
    }
    public SimpleStringProperty addrProperty() {return addr;}
    public int getId() {return this.id.get();}
    public String getName() {
        return (this.name.get());
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
