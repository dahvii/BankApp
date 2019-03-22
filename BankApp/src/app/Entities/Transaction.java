package app.Entities;


import app.annotations.Column;

import java.sql.Date;
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
    private Date date;

    public Transaction(){}

    public Transaction(String fromAccount, String toAccount, double amount, String message, String status, Date date) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.message = message;
        this.status = status;
        this.date = date;
    }

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

    public Date getDate() {
        return date;
    }

}
