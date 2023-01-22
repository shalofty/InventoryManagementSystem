/**
 * E.  Add the listed functionalities to the following parts forms:
 *
 * 1.  The Add Part form
 * • The In-House and Outsourced radio buttons switch the bottom label to the correct value (Machine ID or Company Name). ✔
 * •   The application auto-generates a unique part ID. The part IDs can be, but do not need to be, contiguous. ✔
 *
 * -   The part ID text field must be disabled. ✔
 *
 * •   The user should be able to enter a part name, inventory level or stock, a price, maximum and minimum values, and company name or machine ID values into active text fields. ✔
 *
 * •   After saving the data, users are automatically redirected to the Main form. ✔
 *
 * •   Canceling or exiting this form redirects users to the Main form. ✔
 * */
package InventoryManagementSystem;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

public class addPartController {

    @FXML public Button backButton, saveButton;
    @FXML public TextField partidField, partnameField, partinvField, partpriField, partmaxField, partminField, radioresultField;
    @FXML public RadioButton ihRadio, osRadio;
    @FXML public Label radioResult;

    private static Stage stage;
    private static Scene scene;
    private static Parent root;

    /**
     * radio button selection functions
     * initially had a single function with if/else logic
     * but I noticed it was limited to making 1-2 decisions without being faulty
     * so I separated the radio buttons into their own functions
     * */
    public void ihRadio(ActionEvent event) {
        if (ihRadio.isSelected()) {
            this.radioResult.setText("Machine ID");
            this.radioresultField.setEditable(true);
            this.radioresultField.setPromptText("");
            osRadio.setSelected(false);
        }
        else {
            this.radioResult.setText("Select Source");
            this.radioresultField.setEditable(false);
            this.radioresultField.setPromptText("Disabled");
        }
    }

    public void osRadio(ActionEvent event) {
        if (osRadio.isSelected()) {
            this.radioResult.setText("Company Name");
            this.radioresultField.setEditable(true);
            this.radioresultField.setPromptText("");
            ihRadio.setSelected(false);
        }
        else {
            this.radioResult.setText("Select Source");
            this.radioresultField.setEditable(false);
            this.radioresultField.setPromptText("Disabled");
        }
    }

    /**
     * Generating a random ID
     * @param min and max are parameters for the randID function
     **/
    public static int randID(int min, int max) {
        Random num = new Random();
        ObservableList<Part> partList = Inventory.getAllParts();
        int nextNum = num.nextInt(min, max);
        int finalNum = nextNum;
        while (partList.stream().anyMatch(product -> product.getId() == finalNum)) {
            nextNum = num.nextInt(min, max);
        }
        return nextNum;
    }

    /**
     * goBack button functionality
     * @param event triggered during back button click by user
     * */
    @FXML public void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * fieldSpy function I created in order to keep the save button disabled until all the fields are filled
     * I cleaned up the code using a lambda expression and the emptyFields function below
     * The function is called as the mouse enters the anchor pane, so essentially as soon as the user opens the form
     * This is what I consider a great method of exception prevention
     * FUTURE IMPROVEMENT: would be to expand this method of thinking into datatype validation to prevent the user from being able to submit invalid data
     * */
    @FXML public void fieldSpy() {
        try {
            saveButton.disableProperty().bind(Bindings.createBooleanBinding(
                    this::emptyFields,
                    partnameField.textProperty(),
                    partinvField.textProperty(),
                    partpriField.textProperty(),
                    partmaxField.textProperty(),
                    partminField.textProperty(),
                    radioresultField.textProperty()));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FAILED ESPIONAGE");
            alert.setHeaderText("Something went wrong! Our spy has been caught!");
            alert.setContentText("In some parts of the world, espionage is a crime. In others, it's a career choice. In this case, it's a bug. Please contact your local developer!");
            alert.showAndWait();
        }
    }

    /**
     * savePart function to save values from the TextFields
     * must call generateID() function
     * use getText() to read TextFields and save values to new variables
     * which will be used as parameters when for new class objects (In-House vs Outsourced)
     * must return to previous mainMenu
     * */
    @FXML public void savePart(ActionEvent event) throws IOException {
        try {
            // generating part ID using generatedID function
            int partID = randID(0, 9999);

            // use getText() for other fields
            String partName = this.partnameField.getText();
            int partInv = Integer.parseInt(this.partinvField.getText());
            int partMin = Integer.parseInt(this.partminField.getText());
            int partMax = Integer.parseInt(this.partmaxField.getText());
            double partPrice = Double.parseDouble(this.partpriField.getText());

            // using if/else, creating new objects from InHouse.java or Outsourced.java depending on radio selection
            if (ihRadio.isSelected()) {
                int machineID = Integer.parseInt(radioresultField.getText());
                InHouse part = new InHouse(partID, partName, partPrice, partInv, partMin, partMax, machineID);
                if (dataVerified()) {
                    Inventory.addPart(part);
                }
            } else {
                String companyName = radioresultField.getText();
                Outsourced part = new Outsourced(partID, partName, partPrice, partInv, partMin, partMax, companyName);
                if (dataVerified()) {
                    Inventory.addPart(part);
                }
            }

            // returning to mainMenu
            Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error saving your part!");
            alert.setContentText(e.getMessage() + "\nPlease adjust this value and try again.");
            alert.showAndWait();
        }
    }

    /**
     * noRadio function to check if radio buttons are selected
     * */
    public boolean noRadio() {
        try {
            if (ihRadio.isSelected() || osRadio.isSelected()) {
                return false;
            } else {
                return true;
            }
        }
        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error with your input");
            alert.setContentText("Please try again");
            alert.showAndWait();
            return false;
        }
    }

    /**
     * emptyFields functions checks if fields are empty
     * returns boolean values for use in other functions
     * FUTURE IMPROVEMENT: make the logic more complex to iterate through each field separately and return an alert for each individual field
     * */
    public boolean emptyFields() {
        // if any fields are empty, function return true
        // if no fields are empty, functions returns false
        if (this.partnameField.getText().isEmpty() || this.partinvField.getText().isEmpty() || this.partmaxField.getText().isEmpty() || this.partminField.getText().isEmpty() || this.partpriField.getText().isEmpty() || this.radioresultField.getText().isEmpty()){

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
        int max = Integer.parseInt(this.partmaxField.getText());
        int min = Integer.parseInt(this.partminField.getText());
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
        int stock = Integer.parseInt(this.partinvField.getText());
        int max = Integer.parseInt(this.partmaxField.getText());
        int min = Integer.parseInt(this.partminField.getText());
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
        // checking radio
        if (noRadio()) {
            Alert noRadioAlert = new Alert(Alert.AlertType.WARNING);
            noRadioAlert.setTitle("Warning");
            noRadioAlert.setHeaderText("No Radio Button Selected");
            noRadioAlert.setContentText("Please select a radio button");
            noRadioAlert.showAndWait();
            return false;
        }
        // checking fields and ranges
        else if (emptyFields()) {
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
}
