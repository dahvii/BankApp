package app.Entities;

import app.annotations.Column;
import java.time.LocalDate;

public class Account {
    @Column("bankNr")
    private String bankNr;
    @Column("name")
    private String name;
    @Column("usage")
    private String usage;
    @Column("user")
    private String user;
    @Column("balance")
    private double balance;


    public String getBankNr() {
        return bankNr;
    }

    public String getName() {
        return name;
    }

    public String getUsage() {
        return usage;
    }

    public String getUser() {
        return user;
    }

    public double getBalance() { return balance; }

     @Override
     public String toString(){
        return String.format("Account:bankNr: %s ", bankNr );   }
}
