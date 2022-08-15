package model;

import java.time.LocalDateTime;

public class PackageFactory {
    //use getShape method to get object of type shape
    public static Package getInstance(String packageType, String name, String notes, double priceInDollar, double weight, LocalDateTime expectedDeliveryDate, String authorName, LocalDateTime expiryDate, double environmentalHandlingFee){
        if(packageType == null){
            return null;
        }
        if(packageType.equalsIgnoreCase("Book")){
            return new Book(name,notes,priceInDollar,weight,expectedDeliveryDate,packageType,authorName);

        } else if(packageType.equalsIgnoreCase("Perishable")){
            return new Perishable(name,notes,priceInDollar,weight,expectedDeliveryDate,packageType,expiryDate);

        } else if(packageType.equalsIgnoreCase("Electronic")){
            return new Electronic(name,notes,priceInDollar,weight,expectedDeliveryDate,packageType,environmentalHandlingFee);
        }
        return null;
    }
}