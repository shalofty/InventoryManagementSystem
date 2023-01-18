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
