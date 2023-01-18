package InventoryManagementSystem;

import InventoryManagementSystem.Part;
import javafx.collections.FXCollections;
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

public class modProductController {

    @FXML public Button backButton, saveButton, addPartButton, delPartButton;
    @FXML public TextField modproIDField, modproNameField, modproInvField, modproPriceField, modproMaxField, modproMinField;
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

    public void goBack(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
