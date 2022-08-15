package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents the package information such as name, notes, price, weight , delivery status and expected delivery date.
 * It implements comparable interface by defining the sorting mechanism to be on delivery date of objects.
 *
 * @author Homayoun
 */

public class Package implements Comparable<Package> {
    private String name;

    public void setType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }

    /**
     * Getter method for package name.
     *
     * @return the name of package.
     */
    public String getName() {
        return name;
    }

    private String notes;

    private double priceInDollar;
    private double weight;

    public boolean getIsDelivered() {
        return isDelivered;
    }

    private boolean isDelivered;

    /**
     * set the isDelivered parameter. By default, it is false, however, the user can change this in menu option.
     *
     * @param delivered is local variable for isDelivered.
     */
    public void setDelivered(boolean delivered) {
        isDelivered = delivered;
    }


    private LocalDateTime expectedDeliveryDate;

    /**
     * Getter for expectedDeliveryDate. Used in options 4 and 5.
     *
     * @return the expectedDeliveryDate
     */
    public LocalDateTime getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    /**
     * Constructs a package info object that holds various information about a package.
     *
     * @param name                 the name of package. Not unique!
     * @param notes                mentions any notes worth knowing about a package.
     * @param priceInDollar        is the price in US dollars.
     * @param weight               how heavy the package is, in kg measurements.
     * @param expectedDeliveryDate when we expect our package to be delivered.
     */
    public Package(String name, String notes, double priceInDollar, double weight, LocalDateTime expectedDeliveryDate, String type) {
        this.name = name;
        this.notes = notes;
        this.priceInDollar = priceInDollar;
        this.weight = weight;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.isDelivered = false;
        this.type = type;
    }

    /**
     * default toString method format is changed to suit our output format needs.
     *
     * @return the package, notes, price in US dollars, weight in KG, expected delivery date, and delivered status.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String isDeliveredtoPrintYesNo = "No";
        if (isDelivered == true)
            isDeliveredtoPrintYesNo = "Yes";

        return "Name: " + name + '\n' +
                "Notes: " + notes + '\n' +
                "Price: $" + priceInDollar + '\n' +
                "Weight: " + weight + "kg" + '\n' +
                "Expected Delivery Date: " + expectedDeliveryDate.format(formatObj) + '\n' +
                 //"Type: " + type + '\n' +
                "Delivered? " + isDeliveredtoPrintYesNo + '\n';
    }

    @Override
    public int compareTo(Package other) {
        LocalDateTime a =  this.getExpectedDeliveryDate();
        LocalDateTime b = other.getExpectedDeliveryDate();
        return a.compareTo(b);
    }
}
