/**
 * I.  User Interface
 *
 * A.  Create a JavaFX application with a graphical user interface (GUI) based on the attached “Software 1 GUI Mock-Up.”
 * You may use JavaFX with or without FXML to create your GUI, or you may use Scene Builder to create your FXML file; use of Swing is not permitted.
 * The user interface (UI) should closely match the organization of the GUI layout and contain all UI components (buttons, text fields, etc.) in each of the following GUI mock-up forms:
 *
 * 1.  Main form ✔
 *
 * 2.  Add Part form ✔
 *
 * 3.  Modify Part form ✔
 *
 * 4.  Add Product form ✔
 *
 * 5.  Modify Product form ✔
 *
 * Per the requirements listed in I.A. User Interface, the UI I created has a Main Menu form, addPart form, modPart form, addProduct form, and modProduct form.
 * A notable runtime error that I came across during development was that a null pointer exception that occurred if the user pressed the add or mod buttons while the table was empty.
 * I fixed this by adding a layer of exception prevention by disabling the buttons unless the index of the selection is >= 0. This prevented most of the null exceptions.
 * In order to prevent all null exceptions, I created an alert warning that notified the user there was nothing accepted because theoretically an exception may still occur if the user manually deletes all
 * elements from the tables and then presses the mod or delete buttons.
 *
 * .............................................................................................................................................................................
 *
 * D.  Add the following functionalities to the Main form:
 *
 * 1.  The Parts pane
 *
 * •   The Add button under the Parts TableView opens the Add Part form. ✔
 *
 * •   The Modify button under the Parts TableView opens the Modify Part form. ✔
 *
 * •   The Delete button under the Parts TableView deletes the selected part from the Parts TableView or displays a descriptive error message in the UI or in a dialog box if a part is not deleted. ✔
 *
 * •   When the user searches for parts by ID or name (partial or full name) using the text field, the application displays matching results in the Parts TableView. ✔
 *     (Including a search button is optional.) If the part or parts are found, the application highlights a single part or filters multiple parts. If the part is not found, the application displays an error message in the UI or in a dialog box.
 *
 * •   If the search field is set to empty, the table should be repopulated with all available parts. ✔
 *
 * 2.  The Products pane
 *
 * •   The Add button under the Products TableView opens the Add Product form. ✔
 *
 * •   The Modify button under the Products TableView opens the Modify Product form. ✔
 *
 * •   The Delete button under the Products TableView deletes the selected product (if appropriate) from the Products TableView or displays a descriptive error message in the UI or in a dialog box if a product is not deleted. ✔
 *
 * •   When the user searches for products by ID or name (partial or full name) using the text field, the application displays matching results in the Products TableView. (Including a search button is optional.) ✔
 *     If a product or products are found, the application highlights a single product or products or filters multiple products. If a product or products are not found, the application displays an error message in the UI or in a dialog box.
 *
 * •   If the search field is set to empty, the table should be repopulated with all available products. ✔
 * */


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
    @FXML public TextField partSearchBox, productSearchBox;
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
     * My idea for this was to have a layer of exception prevention
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
     * FUTURE IMPROVEMENT: could be cleaner by returning a more relevant datatype from the lookupPart function in the Inventory class or creating a new function altogether
     * This was the first build of the function, and I used it for the others, so they don't have inline comments
     * */
    public void partSearch(ActionEvent event) throws IOException {
        String query = partSearchBox.getText(); // input from text field
        ObservableList<Part> queryNameList = Inventory.lookupPart(query); // list of parts that match the query
        if (queryNameList.size() > 0) {
            viewParts.setItems(queryNameList);
        }
        else {
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
            }
            catch (Exception e) {
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
     * prductSearchBox function
     * @param event triggers when user searches in search bar
     * */
    public void productSearch(ActionEvent event) throws IOException {
        String query = productSearchBox.getText();
        ObservableList<Product> queryNameList = Inventory.lookupProduct(query);
        if (queryNameList.size() > 0) {
            viewProducts.setItems(queryNameList);
        }
        else {
            try {
                int productID = Integer.parseInt(query);
                Product product = Inventory.lookupProductbyID(productID);
                String nameByID = product.getName();
                ObservableList<Product> queryIDList = Inventory.lookupProduct(nameByID);
                if (queryIDList.isEmpty()) {
                    viewProducts.setItems(queryNameList);
                }
                else {
                    viewProducts.setItems(queryIDList);
                    viewProducts.getSelectionModel().select(product);
                }
            }
            catch (Exception e) {
                viewProducts.setItems(null);
                Alert notFound = new Alert(Alert.AlertType.ERROR);
                notFound.setTitle("Error");
                notFound.setHeaderText("Part not found");
                notFound.setContentText("Please try again");
                notFound.showAndWait();
            }
        }
    }

    /**
     * proAdd function stages addProduct.fxml scene
     * @param event triggers when user clicks the add product button
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
     * @param event triggers when user clicks the mod product button
     * */
    public void proMod(ActionEvent event) throws IOException {
        Product selectedProduct = viewProducts.getSelectionModel().getSelectedItem();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modProduct.fxml"));
        loader.load();

        modProductController controller = loader.getController();
        controller.productAttributes(selectedProduct);


        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(loader.getRoot());
        stage.setScene(scene);
        stage.show();
    }

    /**
     * proDel function to delete products from inventory
     * @param event triggers when user clicks the delete product button
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