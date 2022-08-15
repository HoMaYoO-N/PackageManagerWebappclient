package model;

import java.time.LocalDateTime;

public class Book extends Package {
    private String authorName;



    @Override
    public String toString() {
        return super.toString() + "Author: " + authorName + '\n';
    }

    public Book(String name, String notes, double priceInDollar, double weight, LocalDateTime expectedDeliveryDate,String type,String authorName) {
        super(name, notes, priceInDollar, weight, expectedDeliveryDate,type);
        this.authorName = authorName;
    }
}
