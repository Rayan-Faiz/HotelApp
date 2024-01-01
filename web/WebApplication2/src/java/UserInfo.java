public class UserInfo {
    private int userId;
    private String email;
    private String firstname;
    private String lastname;
    private String address;

    // Constructors, getters, and setters

    public UserInfo(int userId, String email, String firstname, String lastname, String address) {
        this.userId = userId;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.address = address;
    }

    // Add getters and setters as needed

    public int getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getAddress() {
        return address;
    }
}
