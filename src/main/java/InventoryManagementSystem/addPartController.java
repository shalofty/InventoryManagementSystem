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

import InventoryManagementSystem.InHouse;
import InventoryManagementSystem.Inventory;
import InventoryManagementSystem.Outsourced;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javafx.scene.control.RadioButton;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

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
    public void inRadio(ActionEvent event) {
        this.radioResult.setText("Machine ID");
        osRadio.setSelected(false);
    }

    public void osRadio(ActionEvent event) {
        this.radioResult.setText("Company Name");
        ihRadio.setSelected(false);
    }

    /**
     * Generating a random ID
     * @param min and max are parameters for the randID function
     **/
    public static int randID(int min, int max) {
        Random num = new Random();
        return num.nextInt((max - min) + 1) + min;
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
     * savePart function to save values from the TextFields
     * must call generateID() function
     * use getText() to read TextFields and save values to new variables
     * which will be used as parameters when for new class objects (In-House vs Outsourced)
     * must return to previous mainMenu
     * */
    @FXML public void savePart(ActionEvent event) throws IOException {

        // generating part ID using generatedID function
        int partID = randID(0, 100000);

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
            Inventory.addPart(part);
        } else {
            String companyName = radioresultField.getText();
            Outsourced part = new Outsourced(partID, partName, partPrice, partInv, partMin, partMax, companyName);
            Inventory.addPart(part);
        }
        // returning to mainMenu
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
