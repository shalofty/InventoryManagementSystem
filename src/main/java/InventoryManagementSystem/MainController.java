package InventoryManagementSystem;

import javafx.application.Platform;
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
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML public Button mainexitButton, partaddButton, partmodButton, partdelButton, proaddButton, promodButton, prodelButton;
    @FXML public TextField partSearch, productSearch;
    @FXML public TableColumn<Part, Integer> partidColumn, partinvColumn;
    @FXML public TableColumn<Product, Integer> proidColumn, proinvColumn;
    @FXML public TableColumn<Part, String> partnameColumn;
    @FXML public TableColumn<Product, String> pronameColumn;
    @FXML public TableColumn<Part, Double> partpriceColumn;
    @FXML public TableColumn<Product, Double> propriceColumn;
    @FXML public TableView<Part> viewParts;
    @FXML public TableView<Product> viewProducts;

    private static Stage stage;
    private static Scene scene;
    private static Parent root;


    /**
     * partAdd function stages addPart.fxml scene
     * @param event triggers when user clicks the add part button
     * */
    public void partAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addPart.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * partMod function stages modPart.fxml scene
     * @param event triggers when user clicks the mod part button
     * */
    public void partMod(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modPart.fxml"));
        loader.load();

        modPartController mpc = loader.getController();
        mpc.partAttributes(viewParts.getSelectionModel().getSelectedItem());


        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * enable buttons functions for delete and modify buttons
     * This prevents the buttons being pressed while there is a null value
     * which would trigger an exception
     * */
    public void enablePartButtons() {
        viewParts.setOnMouseClicked(mouseEvent -> {
            if(viewParts.getSelectionModel().getSelectedIndex() >= 0) {
                partdelButton.setDisable(false);
                partmodButton.setDisable(false);
            }
            else {
                partdelButton.setDisable(true);
                partmodButton.setDisable(true);
            }
        });
    }

    public void enableProductButton() {
        viewProducts.setOnMouseClicked(mouseEvent -> {
            if(viewProducts.getSelectionModel().getSelectedIndex() >= 0) {
                prodelButton.setDisable(false);
                promodButton.setDisable(false);
            }
            else {
                prodelButton.setDisable(true);
                promodButton.setDisable(true);
            }
        });
    }

    /**
     * partDel function remove parts from inventory
     * using the deletePart function in the Inventory class
     * @param event triggers when user clicks the delete part button
     * */
    public void partDel(ActionEvent event) throws IOException {
        Part selectedPart = viewParts.getSelectionModel().getSelectedItem();
        Inventory.deletePart(selectedPart);
    }

    /**
     * partSeachBox function
     * uses the lookupPart function defined in the Inventory class
     * @param event triggers when user searches in search bar
     * */
    public void partSearchBox(ActionEvent event) {
        String query = partSearch.getText();
        ObservableList<Part> dittoParts = Inventory.lookupPart(query);
        if(dittoParts.isEmpty()) {
            viewParts.setItems(null);
        }
        else {
            viewParts.setItems(dittoParts);
        }
    }

    /**
     * proAdd function stages addProduct.fxml scene
     * */
    public void proAdd(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addProduct.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * proMod function stages modProduct.fxml scene
     * */
    public void proMod(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("modProduct.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * proDel function to delete products from inventory
     * */
    public void proDel(ActionEvent event) throws IOException {
        Product selectedProduct = viewProducts.getSelectionModel().getSelectedItem();
        Inventory.deleteProduct(selectedProduct);
    }

    /**
     * closeApp function for mainMenu scene.
     * @param event triggers when user clicks the exit button
     * */
    public void closeApp(ActionEvent event) {
        Platform.exit();
    }

    @Override
    public void initialize (URL location, ResourceBundle bundles) {
        // parts
        viewParts.setItems(Inventory.getAllParts());
        partidColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partnameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partinvColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partpriceColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        // products
        viewProducts.setItems(Inventory.getAllProducts());
        proidColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("ID"));
        pronameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        proinvColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        propriceColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));
    }
}