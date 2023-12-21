public class User {
    // declaring "User" class specific instance variables
    private String username;
    private String password;

    // constructor for the "User" class
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    // getters and setters for the instance variables
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
