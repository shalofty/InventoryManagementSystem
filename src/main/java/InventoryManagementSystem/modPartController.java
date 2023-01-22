/**
 * 2.  The Modify Part form
 * • The text fields populate with the data from the chosen part. ✔
 * • The In-House and Outsourced radio buttons switch the bottom label to the correct value (Machine ID or Company Name) and swap In-House parts and Outsourced parts. ✔
 *   When new objects need to be created after the Save button is clicked, the part ID should be retained. ✔
 * • The user can modify data values in the text fields sent from the Main form except the part ID. ✔
 * • After saving modifications to the part, the user is automatically redirected to the Main form. ✔
 * • Canceling or exiting this form redirects users to the Main form. ✔
 * */

package InventoryManagementSystem;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.net.URL;


import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.ResourceBundle;

public class modPartController implements Initializable {

    @FXML public Button backButton, saveButton;
    @FXML public TextField partidField, partnameField, partinvField, partpriField, partmaxField, partminField, partmachField, radioresultField;
    @FXML public RadioButton ihRadio, osRadio;
    @FXML public Label radioResult;
    @FXML public TableView<Part> viewParts;

    public Part selectedPart;
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
            this.radioresultField.clear();
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
            this.radioresultField.clear();
            this.radioresultField.setPromptText("Disabled");
        }
    }

    /**
     * @param selectedPart the part to be modified
     * This function literally sets the text fields to the values of the selected part
     * */
    public void partAttributes(Part selectedPart) {

        this.selectedPart = selectedPart;

        partidField.setText(Integer.toString(this.selectedPart.getId()));
        partnameField.setText(this.selectedPart.getName());
        partinvField.setText(Integer.toString(this.selectedPart.getStock()));
        partpriField.setText(Double.toString(this.selectedPart.getPrice()));
        partmaxField.setText(Integer.toString(this.selectedPart.getMax()));
        partminField.setText(Integer.toString(this.selectedPart.getMin()));

        if (this.selectedPart instanceof InHouse) {
            ihRadio.setSelected(true);
            this.radioResult.setText("Machine ID");
            if (radioresultField == null) {
                Alert nullAlert = new Alert(Alert.AlertType.WARNING);
                nullAlert.setTitle("BLANK MACHINE ID");
                nullAlert.setContentText("Missing Machine ID");
                nullAlert.showAndWait();
            }
            else {
                radioresultField.setText(String.valueOf(((InHouse) this.selectedPart).getMachineID()));
            }
        }
        else {
            osRadio.setSelected(true);
            this.radioResult.setText("Company Name");
            if (radioresultField == null) {
                Alert nullAlert = new Alert(Alert.AlertType.WARNING);
                nullAlert.setTitle("BLANK COMPANY NAME");
                nullAlert.setContentText("Missing Company Name");
                nullAlert.showAndWait();
            }
            else {
                radioresultField.setText(String.valueOf(((Outsourced) this.selectedPart).getCompanyName()));
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
     * @param event triggered when user presses the save button
     * Generates a new partID, and deletes the old entry from the table
     * */
    @FXML public void savePart(ActionEvent event) throws IOException {
        try {
            // part index for use in updatePart()
            int index = Inventory.getAllParts().indexOf(selectedPart);

            int partID = selectedPart.getId();

            // use getText() for other fields
            String partName = partnameField.getText();
            int partInv = Integer.parseInt(partinvField.getText());
            int partMin = Integer.parseInt(partminField.getText());
            int partMax = Integer.parseInt(partmaxField.getText());
            double partPrice = Double.parseDouble(partpriField.getText());

            // using if/else, creating new objects from InHouse.java or Outsourced.java depending on radio selection
            if (ihRadio.isSelected()) {
                int machineID = Integer.parseInt(radioresultField.getText());
                InHouse part = new InHouse(partID, partName, partPrice, partInv, partMin, partMax, machineID);
                if (dataVerified()) {
                    Inventory.updatePart(index, part); // calling updatePart on the part
                }
            } else {
                String companyName = radioresultField.getText();
                Outsourced part = new Outsourced(partID, partName, partPrice, partInv, partMin, partMax, companyName);
                if (dataVerified()) {
                    Inventory.updatePart(index, part); // calling updatePart on the part
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
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("There was an error saving your part!");
            alert.setContentText(e.getMessage() + "\nPlease adjust this value and try again.");
            alert.showAndWait();
        }

    }

    /**
     * partDel used to delete an old entry during the savePart function
     * @param selectedPart used in the savePart function
     * */
    public void partDel(Part selectedPart) throws IOException {
        Inventory.deletePart(selectedPart);
    }

    /**
     * goBack function used to return to the previous menu
     * @param event triggered when user presses back button
     * */
    public void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * radiocheck function to check if radio buttons are selected
     * */
    public boolean noRadio() {
        if (ihRadio.isSelected() || osRadio.isSelected()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * emptyFields functions checks if fields are empty
     * returns boolean values for use in other functions
     * */
    public boolean emptyFields() {
        // if any fields are empty, function return true
        // if no fields are empty, functions returns false
        if (this.partnameField.getText().isEmpty() || this.partinvField.getText().isEmpty() || this.partmaxField.getText().isEmpty() || this.partminField.getText().isEmpty() || this.partpriField.getText().isEmpty()) {
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

    @Override
    public void initialize(URL url, ResourceBundle bundles) {}
}
