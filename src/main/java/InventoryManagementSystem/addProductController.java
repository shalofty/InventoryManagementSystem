package InventoryManagementSystem;

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
     **/
    public static int randID(int min, int max) {
        Random num = new Random();
        return num.nextInt((max - min) + 1) + min;
    }

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

    public void partSearch(ActionEvent event) {
        String query = partSearchBox.getText();
        ObservableList<Part> dittoParts = Inventory.lookupPart(query);
        if(dittoParts.isEmpty()) {
            viewParts.setItems(null);
        }
        else {
            viewParts.setItems(dittoParts);
        }
    }

    public void associatedPartSearch(ActionEvent event) {
        String query = associatedPartSearchBox.getText();
        ObservableList<Part> dittoParts = Inventory.lookupPart(query);
        if(dittoParts.isEmpty()) {
            viewProductParts.setItems(null);
        }
        else {
            viewProductParts.setItems(dittoParts);
        }
    }

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

            // create new product instance, assign values from fields
            Product newProduct = new Product();
            newProduct.setID(addProductController.randID(0, 999999));
            newProduct.setName(this.nameField.getText());
            newProduct.setStock(Integer.parseInt(this.invField.getText()));
            newProduct.setMin(Integer.parseInt(this.minField.getText()));
            newProduct.setMax(Integer.parseInt(this.maxField.getText()));
            newProduct.setPrice(Double.parseDouble(this.priceField.getText()));
            Inventory.addProduct(newProduct);

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
        this.viewParts.setItems(Inventory.getAllParts());
    }

    public void updateProductParts() {
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
