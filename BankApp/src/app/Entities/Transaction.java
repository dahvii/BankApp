package app.Entities;


import app.annotations.Column;

import java.time.LocalDate;

public class Transaction {
    @Column("id")
    private long id;
    @Column("fromAccount")
    private String fromAccount;
    @Column("toAccount")
    private String toAccount;
    @Column("amount")
    private double amount;
    @Column ("message")
    private String message;
    @Column("status")
    private String status;
    @Column("date")
    private LocalDate date;

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDate() {
        return date;
    }

}
