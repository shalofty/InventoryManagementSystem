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

/**
 * creation of Outsourced class which extends to the Abstract Part class per the UML class diagram
 * */
public class Outsourced extends Part {
    // per the UML class diagram

    // -companyName:String
    private String companyName;

    /**
     * class constructor for Outsourced class
     * differs from InHouse because there is no machineID, there is a companyName
     * */
    public Outsourced (int ID, String name, double price, int stock, int min, int max, String companyName) {
        super(ID, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @param companyName to set companyName
     * */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * getCompanyName to get companyName
     * */
    public String getCompanyName() {
        return companyName;
    }
}
