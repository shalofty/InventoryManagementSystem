/**
 * II.  Application
 *
 * C.  Create classes with data and logic that map to the UML class diagram and include the supplied Part class provided in the attached “Part.java.”
 * Do not alter the provided class. Include all the classes and members as shown in the UML diagram. Your code should demonstrate the following:
 *
 * •   inheritance ✔
 *
 * •   abstract and concrete classes ✔
 *
 * •   instance and static variables ✔
 *
 * •   instance and static methods ✔
 * */

package InventoryManagementSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    // per the UML class diagram requirements
    // modeled this class after the part class included with the project while following the UML class diagram for specifications

    private final ObservableList<Part> associatedParts;
    private int ID, stock, min, max;
    private String name;
    private double price;

    // Product
    /**
     * class constructor for Product
     * */
    public Product(int ID, String name, double price, int stock, int min, int max) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.associatedParts = FXCollections.observableArrayList();
    }

    public Product() {
        this(0, null, 0.00, 0, 0, 0);
    }

    // set functions
    /**
     * @param ID to set
     * */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @param name to set
     * */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price to set
     * */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @param stock to set
     * */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @param min to set
     * */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @param max to set
     * */
    public void setMax(int max) {
        this.max = max;
    }

    // get functions
    /**
     * @return ID
     * */
    public int getID() {
        return ID;
    }

    /**
     * @return name
     * */
    public String getName() {
        return name;
    }

    /**
     * @return price
     * */
    public double getPrice() {
        return price;
    }

    /**
     * @return stock
     * */
    public int getStock() {
        return stock;
    }

    /**
     * @return min
     * */
    public int getMin() {
        return min;
    }

    /**
     * @return max
     * */
    public int getMax() {
        return max;
    }

    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        return this.associatedParts.remove(selectedAssociatedPart);
    }

    /**
    * @return list of parts
    * */
    public ObservableList<Part> getAllAssociated() {
        return associatedParts;
    }
}
