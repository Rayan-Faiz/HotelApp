package rf.hotel.Classes;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employees {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty name;
    private final SimpleStringProperty num;
    private final SimpleStringProperty rank;
    private final SimpleFloatProperty salary;

    public Employees(int id, String name, String num, String rank, Float salary) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.num = new SimpleStringProperty(num);
        this.rank = new SimpleStringProperty(rank);
        this.salary = new SimpleFloatProperty(salary);
    }

    public Employees(String name, String num, String rank, Float salary) {

        id = null;
        this.name = new SimpleStringProperty(name);
        this.num = new SimpleStringProperty(num);
        this.rank = new SimpleStringProperty(rank);
        this.salary = new SimpleFloatProperty(salary);
    }



    public SimpleIntegerProperty idProperty() {
        return id;
    }
    public  SimpleStringProperty nameProperty() {
        return name;
    }
    public SimpleStringProperty numProperty() {
        return num;
    }
    public SimpleStringProperty rankProperty() {
        return rank;
    }
    public SimpleFloatProperty salaryProperty() {return salary;}
    public int getId() {return this.id.get();}
    public String getName() {
        return (this.name.get());
    }
    public String getNum() {
        return (this.num.get());
    }
    public String getRank() {
        return (this.rank.get());
    }
    public float getSalary() {return salary.get();}


}
