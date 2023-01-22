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

public class Inventory {
    // per the included UML class diagram requirements

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * addPart function to add newPart to Inventory
     * @param newPart is the new part to be added to the Inventory
     * */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * addProduct function to add newProduct to Inventory
     * @param newProduct is the new product to be added to the Inventory
     * */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    /**
     * ATTENTION
     * there are 4 total lookupStuff functions
     * 2 functions use an int ID parameter
     * 2 functions use String name parameter
     * */

    /**
     * lookupPart function via partID
     * @param partID is the parameter for the lookupPart function
     * */
    public static Part lookupPartbyID(int partID) {
        // Pokemon number 132 is Ditto, it can freely recombine its own structure to transform to other forms.
        Part ditto = null;
        for (Part part : allParts) {
            if (partID == part.getId()){
                ditto = part;
            }
        }
        return ditto;
    }

    /**
     * lookupProduct function via productID
     * @param productID is the parameter for the lookupProduct function
     * */
    public static Product lookupProductbyID(int productID) {
        Product ditto = null;
        for (Product product : allProducts) {
            if (productID == product.getID()) {
                ditto = product;
            }
        }
        return ditto;
    }

    /**
     * lookupPart function via partName
     * @param partName is the parameter for the lookupPart function
     * */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> dittoPart = FXCollections.observableArrayList();
        if (partName.length() > 0) {
            for (Part thePart : allParts) {
                if (thePart.getName().toUpperCase().contains(partName.toUpperCase())) {
                    dittoPart.add(thePart);
                }
            }
        }
        else {dittoPart = allParts;}
        return dittoPart;
    }

    /**
     * lookupProduct function via productName
     * @param productName is the parameter for the lookupProduct function
     * */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> dittoProduct = FXCollections.observableArrayList();
        if (productName.length() > 0) {
            for (Product theProduct : allProducts) {
                if (theProduct.getName().toUpperCase().contains(productName.toUpperCase())) {
                    dittoProduct.add(theProduct);
                }
            }
        }
        else {dittoProduct = allProducts;}
        return dittoProduct;
    }

    /**
     * updatePart function using set()
     * @param index is the parameter for the updatePart function
     * @param selectedPart is the parameter for the updatePart function
     * */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * updateProduct function using set()
     * @param index is the parameter for the updateProduct function
     * @param newProduct is the parameter for the updateProduct function
     * */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * deletePart function to remove selectedPart using remove()
     * @param selectedPart is the parameter for the deletePart function
     * */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * deleteProduct function to remove selectedProduct using remove()
     * @param selectedProduct is the parameter for the deleteProduct function
     * */
    public static boolean deleteProduct(Product selectedProduct) {
        return allProducts.remove(selectedProduct);
    }

    /**
     * @return returns allParts
     * */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * @return returns allProducts
     * */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
}
