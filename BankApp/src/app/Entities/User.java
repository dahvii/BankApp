package app.Entities;


import app.annotations.Column;

public class User {
    @Column("socialNo")
    private String socialNo;
    @Column("password")
    private String password;
    @Column("name")
    private String name;

    public String getSocialNo() {
        return socialNo;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

}
