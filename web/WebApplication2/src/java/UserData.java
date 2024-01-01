public class UserData {

    private int userId;
    private String email;
    private String firstName;
    private String lastName;
    private String address;

    public UserData(int userId, String email, String firstName, String lastName, String address) {
        this.userId = userId;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }
}
