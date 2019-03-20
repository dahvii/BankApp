package app.Entities;


import app.annotations.Column;

import java.time.LocalDate;

public class Transaction {
    @Column
    private long id;
    @Column
    private String message;
    @Column
    private float amount;
    @Column
    private LocalDate date;

    public String getMessage() { return message; }
    public float getAmount() { return amount; }
    public LocalDate getDate() { return date; }
}
