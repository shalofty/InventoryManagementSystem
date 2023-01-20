/**
 * II.  Application
 *
 * C.  Create classes with data and logic that map to the UML class diagram and include the supplied Part class provided in the attached “Part.java.”
 * Do not alter the provided class. Include all the classes and members as shown in the UML diagram. Your code should demonstrate the following:
 *
 * •   inheritance
 *
 * •   abstract and concrete classes
 *
 * •   instance and static variables
 *
 * •   instance and static methods
 * */

package InventoryManagementSystem;

import javafx.collections.ObservableList;

import static InventoryManagementSystem.Inventory.getAllParts;

/**
 * creation of InHouse class which extends to the Abstract Part class per the UML class diagram
 * */
public class InHouse extends Part {
    // per the UML class diagram

    // -machineID:int
    private int machineID;

    /**
     * class constructor for InHouse class
     * differs from Outsourced because there is no companyName, there is a machineID
     * */
    public InHouse (int id, String name, double price, int stock, int min, int max, int machineID) {
        super(id, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * @param machineID to set machineID
     * */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

    /**
     * getMachineID function to get machineID
     * */
    public int getMachineID() {
        return machineID;
    }
}
