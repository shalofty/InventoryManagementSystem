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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    // per the included UML class diagram requirements

    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * addPart function to add newPart to Inventory
     * */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * addProduct function to add newProduct to Inventory
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
     * */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> dittoPart = FXCollections.observableArrayList();
        if (partName.length() > 0) {
            for (Part allPart : allParts) {
                if (allPart.getName().toUpperCase().contains(partName.toUpperCase())) {
                    dittoPart.add(allPart);
                }
            }
        }
        else {dittoPart = allParts;}
        return dittoPart;
    }

    /**
     * lookupProduct function via productName
     * */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> dittoProduct = FXCollections.observableArrayList();
        if (productName.length() != 0) {
            int pokeball = 0;
            while (pokeball < allProducts.size()) {
                if (allProducts.get(pokeball).getName().toUpperCase().contains(productName.toUpperCase())) {
                    dittoProduct.add(allProducts.get(pokeball));
                } else {pokeball++;}
            }
        }
        else {dittoProduct = allProducts;}
        return dittoProduct;
    }

    /**
     * updatePart function using set()
     * */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * updateProduct function using set()
     * */
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * deletePart function to remove selectedPart using remove()
     * */
    public static boolean deletePart(Part selectedPart) {
        return allParts.remove(selectedPart);
    }

    /**
     * deleteProduct function to remove selectedProduct using remove()
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
