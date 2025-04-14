package com.example.invoicemaker.Model;

public class Item {
    String name,price,itemId,totalQuantity,totalPrice;

    public Item(String name, String price, String itemId) {
        this.name = name;
        this.price = price;
        this.itemId = itemId;
    }
    public Item(String name, String price,String totalPrice,String totalQuantity, String itemId) {
        this.name = name;
        this.price = price;
        this.itemId = itemId;
        this.totalPrice=totalPrice;
        this.totalQuantity=totalQuantity;
    }

    public String getName() {
        return name;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public String getPrice() {
        return price;
    }

    public String getItemId() {
        return itemId;
    }
}
