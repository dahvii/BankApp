package app.Entities;


import app.annotations.Column;

import java.time.LocalDate;

public class Transaction {
    @Column("id")
    private long id;
    @Column("fromAccount")
    private long fromAccount;
    @Column("toAccount")
    private long toAccount;
    @Column("amount")
    private double amount;
    @Column ("message")
    private String message;
    @Column("status")
    private String status;
    @Column("date")
    private LocalDate date;

    public long getFromAccount() {
        return fromAccount;
    }

    public long getToAccount() {
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
