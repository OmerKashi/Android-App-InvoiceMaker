package com.example.invoicemaker.Model;

public class Invoice {
    String id,date;

    public Invoice(String id, String date) {

        this.id = id;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }
}
