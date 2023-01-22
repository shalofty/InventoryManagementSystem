/**
 * F.  Add the following functionalities to the following product forms:
 *
 * 1.  The Add Product form
 *
 * •   The application auto-generates a unique product ID. The product IDs can be, but do not need to be, contiguous. ✔
 *
 * -   The product ID text field must be disabled and cannot be edited or changed. ✔
 *
 * •   The user should be able to enter a product name, inventory level or stock, a price, and maximum and minimum values. ✔
 *
 * •   The user can search for parts (top table) by ID or name (partial or full name). If the part or parts are found, the application highlights a single part or filters multiple parts. XXX
 *     If the part or parts are not found, the application displays an error message in the UI or in a dialog box. XXX
 *
 * •   If the search field is set to empty, the table should be repopulated with all available parts. ✔
 *
 * •   The top table should be identical to the Parts TableView in the Main form. ✔
 *
 * •   The user can select a part from the top table. The user then clicks the Add button, and the part is copied to the bottom table. (This associates one or more parts with a product.) ✔
 *
 * •   The Remove Associated Part button removes a selected part from the bottom table. (This dissociates or removes a part from a product.) ✔
 *
 * •   After saving the data, the user is automatically redirected to the Main form. ✔
 *
 * •   Canceling or exiting this form redirects users to the Main form. ✔
 *
 * Note: When a product is deleted, so can its associated parts without affecting the part inventory. The Remove Associated Part button removes a selected part from the bottom table. (This dissociates or removes a part from a product.)
 * */

package InventoryManagementSystem;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import java.net.URL;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.Random;
import java.util.ResourceBundle;

public class addProductController implements Initializable {

    @FXML public Button backButton, saveButton, addPartButton, delPartButton;
    @FXML public TextField idField, nameField, invField, priceField, maxField, minField, partSearchBox, associatedPartSearchBox;
    @FXML public TableColumn<Part, Integer> partIDColumn, partInvColumn, proPartIDColumn, proPartInvColumn;
    @FXML public TableColumn<Part, String> partNameColumn, proPartNameColumn;
    @FXML public TableColumn<Part, Double> partPriceColumn, proPartPriceColumn;
    @FXML public TableView<Part> viewParts; // viewParts -> all addable parts
    @FXML public TableView<Part> viewProductParts; // viewProductParts -> parts within a product

    private final ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private final ObservableList<Part> originalParts = FXCollections.observableArrayList();

    private Product selectedProduct;

    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    /**
     * enable buttons function for addPart button
     * Prevents buttons being pressed while there is a null value
     * which would trigger an exception
     * */
    public void enableAddButton() {
        viewParts.setOnMouseClicked(mouseEvent -> {
            addPartButton.setDisable(viewParts.getSelectionModel().getSelectedIndex() < 0);
        });
    }

    public void enableRemoveButton() {
        viewProductParts.setOnMouseClicked(mouseEvent -> {
            delPartButton.setDisable(viewProductParts.getSelectionModel().getSelectedIndex() < 0);
        });
    }

    /**
     * Generating a random ID
     * @param min and max are parameters for the randID function
     * This code generate a random ID within a  specified range. It then enters a loop where it checks
     * ID's of products in the product list to see if they match the number.
     **/
    public static int randID(int min, int max) {
        Random num = new Random();
        ObservableList<Product> productList = Inventory.getAllProducts();
        int nextNum = num.nextInt(min, max);
        int finalNum = nextNum;
        while (productList.stream().anyMatch(product -> product.getID() == finalNum)) {
            nextNum = num.nextInt(min, max);
        }
        return nextNum;
    }

    /**
     * addPart function adds a part to the associated parts table
     * @param event triggers when user clicks the add part button
     * */
    @FXML void partAdd(ActionEvent event) {
        // apply selection to variable
        Part selectedPart = this.viewParts.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {
            // general null value warning
            Alert nullAlert = new Alert(Alert.AlertType.WARNING);
            nullAlert.setTitle("Warning");
            nullAlert.setHeaderText("No Selection");
            nullAlert.setContentText("Please select from the table");
            nullAlert.showAndWait();
        } else {
            this.associatedParts.add(selectedPart);
            this.updateParts();
            this.updateProductParts();
        }
    }

    /**
     * delPart function removes a part from the associated parts table
     * @param event triggers when user clicks the delete part button
     * */
    @FXML void partDel(ActionEvent event) {
        Part selectedPart = viewProductParts.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            // general null value warning
            Alert nullAlert = new Alert(Alert.AlertType.WARNING);
            nullAlert.setTitle("Warning");
            nullAlert.setHeaderText("No Selection");
            nullAlert.setContentText("Please select from the table");
        }
        else {
            this.associatedParts.remove(selectedPart);
            this.updateParts();
            this.updateProductParts();
        }
    }

    /**
     * partSearch function searches for a part in the parts table
     * @param event triggers when user uses search bar
     * */
    @FXML public void partSearch(ActionEvent event) throws IOException {
        String query = partSearchBox.getText(); // input from text field
        ObservableList<Part> queryNameList = Inventory.lookupPart(query); // list of parts that match the query
        if (queryNameList.size() > 0) {
            viewParts.setItems(queryNameList);
        } else {
            try {
                int partID = Integer.parseInt(query); // converts the query to an int, for use if query happens to be int
                Part part = Inventory.lookupPartbyID(partID); // looking up part by ID using int PartID
                String nameByID = part.getName(); // getting the name of the part by ID because Inventory.lookupPartbyID returns a part object
                ObservableList<Part> queryIDList = Inventory.lookupPart(nameByID); // list of parts that match the query by ID to use for a table view
                if (queryIDList.isEmpty()) {
                    viewParts.setItems(queryNameList);
                }
                else {
                    viewParts.setItems(queryIDList);
                    viewParts.getSelectionModel().select(part);
                }
            } catch (Exception e) {
                viewParts.setItems(null);
                Alert notFound = new Alert(Alert.AlertType.ERROR);
                notFound.setTitle("Error");
                notFound.setHeaderText("Part not found");
                notFound.setContentText("Please try again");
                notFound.showAndWait();
            }
        }
    }

    /**
     * associatedPartSearch function searches for a part in the associated parts table
     * @param event triggers when user uses search bar
     * */
    @FXML public void associatedPartSearch(ActionEvent event) throws IOException {
        String query = associatedPartSearchBox.getText(); // input from text field
        ObservableList<Part> queryNameList = Inventory.lookupPart(query); // list of parts that match the query
        if (queryNameList.size() > 0) {
            viewProductParts.setItems(queryNameList);
        } else {
            try {
                int partID = Integer.parseInt(query); // converts the query to an int, for use if query happens to be int
                Part part = Inventory.lookupPartbyID(partID); // looking up part by ID using int PartID
                String nameByID = part.getName(); // getting the name of the part by ID because Inventory.lookupPartbyID returns a part object
                ObservableList<Part> queryIDList = Inventory.lookupPart(nameByID); // list of parts that match the query by ID to use for a table view
                if (queryIDList.isEmpty()) {
                    viewProductParts.setItems(queryNameList);
                }
                else {
                    viewProductParts.setItems(queryIDList);
                    viewProductParts.getSelectionModel().select(part);
                }
            } catch (Exception e) {
                viewProductParts.setItems(null);
                Alert notFound = new Alert(Alert.AlertType.ERROR);
                notFound.setTitle("Error");
                notFound.setHeaderText("Part not found");
                notFound.setContentText("Please try again");
                notFound.showAndWait();
            }
        }
    }

    /**
     * fieldSpy function I created in order to keep the save button disabled until all the fields are filled
     * I cleaned up the code using a lambda expression and the emptyFields function below
     * The function is called as the mouse enters the anchor pane, so essentially as soon as the user opens the form
     * This is what I consider a great method of exception prevention
     * FUTURE IMPROVEMENT: would be to expand this method of thinking into datatype validation to prevent the user from entering invalid data
     * */
    @FXML public void fieldSpy() {
        try {
            saveButton.disableProperty().bind(Bindings.createBooleanBinding(
                    this::emptyFields,
                    nameField.textProperty(),
                    invField.textProperty(),
                    priceField.textProperty(),
                    maxField.textProperty(),
                    minField.textProperty()));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED ESPIONAGE");
            alert.setHeaderText("Something went wrong! Our spy has been caught!");
            alert.setContentText("In some parts of the world, espionage is a crime. \nIn others, it's a career choice. \nIn this case, it's a bug. \nPlease contact your local developer!");
            alert.showAndWait();
        }
    }

    /**
     * commitProduct functions commits the product to the inventory
     * @param event triggers when user clicks the save button
     * */
    @FXML void commitProduct(ActionEvent event) throws IOException {
        try {
            if (this.associatedParts.isEmpty()) {
                // no selected parts warning
                Alert noPartsAlert = new Alert(Alert.AlertType.WARNING);
                noPartsAlert.setTitle("Warning");
                noPartsAlert.setHeaderText("Parts cannot be empty");
                noPartsAlert.setContentText("Please select from the table");
                noPartsAlert.showAndWait();
            }

            if (!this.associatedParts.isEmpty() && dataVerified()) {
                // create new product instance, assign values from fields
                Product newProduct = new Product();
                newProduct.setID(addProductController.randID(0, 9999));
                newProduct.setName(this.nameField.getText());
                newProduct.setStock(Integer.parseInt(this.invField.getText()));
                newProduct.setMin(Integer.parseInt(this.minField.getText()));
                newProduct.setMax(Integer.parseInt(this.maxField.getText()));
                newProduct.setPrice(Double.parseDouble(this.priceField.getText()));

                associatedParts.stream()
                                .filter(part -> !newProduct.getAllAssociated().contains(part))
                                .forEach(newProduct::addAssociatedPart);

                Inventory.addProduct(newProduct);

                // return to main menu
                Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
        catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Something went wrong!");
            alert.setContentText(e.getMessage() + "\nPlease try again");
            alert.showAndWait();
        }
    }

    /**
     * emptyFields functions checks if fields are empty
     * returns boolean values for use in other functions
     * */
    public boolean emptyFields() {
        // if any fields are empty, function return true
        // if no fields are empty, functions returns false
        if (this.nameField.getText().isEmpty() || this.invField.getText().isEmpty() || this.maxField.getText().isEmpty() || this.minField.getText().isEmpty() || this.priceField.getText().isEmpty()) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * impossibleRange function checks if min/max values are possible
     * returns boolean values for use in other functions
     * */
    public boolean impossibleRange() {
        int max = Integer.parseInt(this.maxField.getText());
        int min = Integer.parseInt(this.minField.getText());
        // return true if the max is less than the min
        // return false if the max is greater than the min
        if (max < min) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * rangeBreach function checks if inventory is within min/max range
     * returns boolean values for use in other functions
     * */
    public boolean rangeBreach() {
        int stock = Integer.parseInt(this.invField.getText());
        int max = Integer.parseInt(this.maxField.getText());
        int min = Integer.parseInt(this.minField.getText());
        // returns true if the stock is less than the min or greater than the max
        if (stock < min || stock > max) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * iterates through several functions to check if data is valid
     * I created this to clean up the savePart function
     * */
    public boolean dataVerified() {
        // checking fields and ranges
        if (emptyFields()) {
            // empty fields warning
            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText("One of more fields is empty");
            emptyFieldsAlert.setContentText("All fields are required");
            emptyFieldsAlert.showAndWait();
            return false;
        }
        else if (impossibleRange()) {
            // impossible min/max range warning
            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText("Range values are impossible");
            emptyFieldsAlert.setContentText("Please update min/max values");
            emptyFieldsAlert.showAndWait();
            return false;
        }
        else if (rangeBreach()) {
            // range breach warning
            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText("Inventory has fallen outside of min/max range");
            emptyFieldsAlert.setContentText("Please update values");
            emptyFieldsAlert.showAndWait();
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * goBack function cancels the product creation, returns to previous menu
     * @param event triggers when user clicks the cancel button
     * */
    public void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * updateParts & updateProductParts functions
     * used within initialize to keep table views up-to-date
     * */
    @FXML public void updateParts() {
        this.viewParts.setItems(Inventory.getAllParts());
    }

    @FXML public void updateProductParts() {
        this.viewProductParts.setItems(this.associatedParts);
    }

    public void initialize(URL location, ResourceBundle bundle) {
        // addable parts
        viewParts.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // parts of selected product
        viewProductParts.setItems(this.originalParts);
        proPartIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        proPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        proPartInvColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        proPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        viewProductParts.setItems(this.associatedParts);

        // update tables
        this.updateParts();
        this.updateProductParts();
    }
}
