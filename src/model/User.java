package model;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String password;  // lebih umum dibanding 'hashedPassword'

    public User() {
        // constructor kosong untuk deserialization (opsional)
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "User{username='" + username + "'}";
    }
}
