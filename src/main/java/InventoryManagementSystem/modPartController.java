package InventoryManagementSystem;

import InventoryManagementSystem.addPartController;
import InventoryManagementSystem.InHouse;
import InventoryManagementSystem.Inventory;
import InventoryManagementSystem.Outsourced;
import InventoryManagementSystem.Part;
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


    private int partID;
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
    public void inRadio(ActionEvent event) {
        this.radioResult.setText("Machine ID");
        osRadio.setSelected(false);
    }

    public void osRadio(ActionEvent event) {
        this.radioResult.setText("Outsourced");
        ihRadio.setSelected(false);
    }

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
     * @param event triggered when user presses the save button
     * Generates a new partID, and deletes the old entry from the table
     * */
    @FXML public void savePart(ActionEvent event) throws IOException {
        // generating part ID using generatedID function
        int partID = addPartController.randID(0, 100000);

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

        // deleting the old entry
        this.partDel(selectedPart);
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

    @Override
    public void initialize(URL url, ResourceBundle bundles) {}
}
