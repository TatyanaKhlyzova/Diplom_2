package org.example;

public class UpdateUser {
    public final String email;
    public final String password;
    public final String name;

    public UpdateUser(String email,String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
