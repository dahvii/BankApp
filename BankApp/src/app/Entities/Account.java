package app.Entities;

import app.annotations.Column;

public class Account {
    @Column("bankNr")
    private String bankNr;
    @Column("name")
    private String name;
    @Column("function")
    private String function;
    @Column("user")
    private String user;
    @Column("balance")
    private double balance;
    @Column("boundary")
    private long boundary;

    public String getBankNr() {
        return bankNr;
    }

    public String getName() {
        return name;
    }

    public String getFunction() {
        return function;
    }

    public String getUser() {
        return user;
    }

    public double getBalance() { return balance; }

    public long getBoundary(){return boundary;}

     @Override
     public String toString(){
        return String.format("Account:bankNr: %s ", bankNr );   }
}
