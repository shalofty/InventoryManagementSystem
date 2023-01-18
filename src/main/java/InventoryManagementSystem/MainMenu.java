package InventoryManagementSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {

    private static Stage mainstage, partstage, productstage;

    @Override
    public void start(Stage mainstage) throws IOException {

        // sample data
        Inventory.addPart(new InHouse(1564, "Coffee", 1.99, 50, 0, 100, 19));
        Inventory.addPart(new Outsourced(24775, "Juice", 3.99, 100, 0, 1000, "Greek Grapes"));
        Inventory.addProduct(new Product(1533, "6 Pack of Juice", 9.99, 34, 0, 100));

        // creating scene
        FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("mainMenu.fxml"));
        Scene main = new Scene(fxmlLoader.load(), 1000, 360);
        mainstage.setResizable(false);
        mainstage.setTitle("Inventory Management System | Stephan Haloftis | shaloft@wgu.edu");
        mainstage.setScene(main);
        mainstage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
