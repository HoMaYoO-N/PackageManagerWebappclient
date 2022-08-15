package model;

import java.time.LocalDateTime;

public class Electronic extends Package {
    double environmentalHandlingFee;

    @Override
    public String toString() {
        return super.toString() + "Environmental Handling Fee: " + "$" + environmentalHandlingFee + '\n';
    }

    public Electronic(String name, String notes, double priceInDollar, double weight, LocalDateTime expectedDeliveryDate,String type, double environmentalHandlingFee) {
        super(name, notes, priceInDollar, weight, expectedDeliveryDate,type);
        this.environmentalHandlingFee = environmentalHandlingFee;
    }
}
