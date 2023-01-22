/**
 * 2.  The Modify Product form
 *
 * •   The text fields populate with the data from the chosen product, and the bottom TableView populates with the associated parts. ✔
 *
 * •   The user can search for parts (top table) by ID or name (partial or full name). If the part or parts are found, the application highlights a single part or filters multiple parts. ✔
 *     If the part is not found, the application displays an error message in the UI or a dialog box.
 *
 * •   If the search text field is set to empty, the table should be repopulated with all available parts. ✔
 *
 * •   The top table should be identical to the Parts TableView in the Main form. ✔
 *
 * •   The user may modify or change data values. ✔
 *
 * -   The product ID text field must be disabled and cannot be edited or changed. ✔
 *
 * •   The user can select a part from the top table. The user then clicks the Add button, and the part is copied to the bottom table. (This associates one or more parts with a product.) ✔
 *
 * •   The user may associate zero, one, or more parts with a product. ✔
 *
 * •   The user may remove or disassociate a part from a product. ✔
 *
 * •   After saving modifications to the product, the user is automatically redirected to the Main form. ✔
 *
 * •   Canceling or exiting this form redirects users to the Main form. ✔
 *
 * Note: The Remove Associated Part button removes a selected part from the bottom table. (This dissociates or removes a part from a product.)
 * */

package InventoryManagementSystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class modProductController implements Initializable {

    @FXML private Button backButton, saveButton, addButton, delButton;
    @FXML private TextField idField, nameField, invField, priceField, maxField, minField, partSearchBox, associatedPartSearchBox;
    @FXML private TableColumn<Part, Integer> partIDColumn, partInvColumn, proPartIDColumn, proPartInvColumn;
    @FXML private TableColumn<Part, String> partNameColumn, proPartNameColumn;
    @FXML private TableColumn<Part, Double> partPriceColumn, proPartPriceColumn;
    @FXML private TableView<Part> viewParts; // viewParts -> all addable parts
    @FXML private TableView<Part> viewProductParts; // viewProductParts -> parts within a product

    public ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> originalParts = FXCollections.observableArrayList();

    private Product selectedProduct;

    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    public void productAttributes(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        this.idField.setText(String.valueOf(selectedProduct.getID()));
        this.nameField.setText(selectedProduct.getName());
        this.invField.setText(String.valueOf(selectedProduct.getStock()));
        this.minField.setText(String.valueOf(selectedProduct.getMin()));
        this.maxField.setText(String.valueOf(selectedProduct.getMax()));
        this.priceField.setText(String.valueOf(Double.valueOf(selectedProduct.getPrice())));
        associatedParts.addAll(selectedProduct.getAllAssociated());
    }

    // enableAddButton()
    public void enableAddButton() {
        viewParts.setOnMouseClicked(mouseEvent -> {
            addButton.setDisable(viewParts.getSelectionModel().getSelectedIndex() < 0);
        });
    }

    // enableRemoveButton()
    public void enableRemoveButton() {
        viewProductParts.setOnMouseClicked(mouseEvent -> {
            delButton.setDisable(viewProductParts.getSelectionModel().getSelectedIndex() < 0);
        });
    }

    /**
     * partAdd function for addPart button
     * Adds a part to the associatedParts list
     * @param event triggered by clicking the addPart button
     */
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
     * partDel function deletes selectedPart from viewProductParts
     * @param event triggered by delButton
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
            this.selectedProduct.deleteAssociatedPart(selectedPart);
            this.updateParts();
            this.updateProductParts();
        }
    }

    /**
     * partSearch function searches for a part in the parts table
     * @param event triggers when user uses search bar
     * */
    public void partSearch(ActionEvent event) throws IOException {
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
    public void associatedPartSearch(ActionEvent event) throws IOException {
        String query = associatedPartSearchBox.getText();
        ObservableList<Part> queryNameList = Inventory.lookupPart(query);
        if (queryNameList.size() > 0) {
            viewProductParts.setItems(queryNameList);
        } else {
            try {
                int partID = Integer.parseInt(query);
                Part part = Inventory.lookupPartbyID(partID);
                String nameByID = part.getName();
                ObservableList<Part> queryIDList = Inventory.lookupPart(nameByID);
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
     * commitProduct function saves the product
     * Implements error checking
     * @param event triggered by saveButton
     */
    @FXML void commitProduct(ActionEvent event) throws IOException {

        if (this.associatedParts.isEmpty()) {
            // no selected parts warning
            Alert noPartsAlert = new Alert(Alert.AlertType.WARNING);
            noPartsAlert.setTitle("Warning");
            noPartsAlert.setHeaderText("Parts cannot be empty");
            noPartsAlert.setContentText("Please select from the table");
            noPartsAlert.showAndWait();
        }

        if (emptyFields()) {
            // empty fields warning
            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText("One of more fields is empty");
            emptyFieldsAlert.setContentText("All fields are required");
            emptyFieldsAlert.showAndWait();
        }

        if (impossibleRange()) {
            // impossible min/max range warning
            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText("Range values are impossible");
            emptyFieldsAlert.setContentText("Please update min/max values");
            emptyFieldsAlert.showAndWait();
        }

        if (rangeBreach()) {
            // range breach warning
            Alert emptyFieldsAlert = new Alert(Alert.AlertType.WARNING);
            emptyFieldsAlert.setTitle("Warning");
            emptyFieldsAlert.setHeaderText("Inventory has fallen outside of min/max range");
            emptyFieldsAlert.setContentText("Please update values");
            emptyFieldsAlert.showAndWait();
        }

        if (!this.associatedParts.isEmpty() && !emptyFields() && !impossibleRange() && !rangeBreach()) {
            // product index
            int index = Inventory.getAllProducts().indexOf(this.selectedProduct);

            // if all conditions are met, save the product
            int id = this.selectedProduct.getID();
            String name = this.nameField.getText();
            int stock = Integer.parseInt(this.invField.getText());
            int min = Integer.parseInt(this.minField.getText());
            int max = Integer.parseInt(this.maxField.getText());
            double price = Double.parseDouble(this.priceField.getText());
            selectedProduct.setID(id);
            selectedProduct.setName(name);
            selectedProduct.setStock(stock);
            selectedProduct.setMin(min);
            selectedProduct.setMax(max);
            selectedProduct.setPrice(price);

            // add selected parts to associated parts
            for (Part part : associatedParts) {
                if (selectedProduct.getAllAssociated().contains(part)) {
                    continue;
                }
                else {
                    selectedProduct.addAssociatedPart(part);
                }
            }

            Inventory.updateProduct(index, selectedProduct);

            // return to main menu
            Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
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
     * goBack function to return to main menu
     * @param event triggered when user clicks back button
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
    public void updateParts() {
        viewParts.setItems(Inventory.getAllParts());
    }

    public void updateProductParts() {
        viewProductParts.setItems(associatedParts);
    }
    public void initialize(URL location, ResourceBundle bundle) {
        // addable parts
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        viewParts.setItems(Inventory.getAllParts());


        // parts of selected product
        proPartIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        proPartNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        proPartInvColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        proPartPriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));
        viewProductParts.setItems(associatedParts);

        // update tables
        this.updateParts();
        this.updateProductParts();
    }

}
