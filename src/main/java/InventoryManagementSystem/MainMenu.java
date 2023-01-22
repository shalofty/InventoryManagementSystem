/**
 * You are working for a small manufacturing organization that has outgrown its current inventory system.
 * Members of the organization have been using a spreadsheet program to manually enter inventory additions, deletions, and other data from a paper-based system but would now like you to develop a more sophisticated inventory program.
 *
 * You have been provided with a mock-up of the user interface to use in the design and development of the system (see the attached “Software 1 GUI Mock-Up”) and a class diagram to assist you in your work (see the attached “UML Class Diagram”).
 * The organization also has specific business requirements that must be considered for the application.
 * A systems analyst created the solution statements outlined in the requirements section of this task based on the business requirements.
 * You will use these solution statements to develop your application.
 * */
package InventoryManagementSystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainMenu extends Application {

    private static Stage mainstage;

    @Override
    public void start(Stage mainstage) throws IOException {

        // sample data
        Inventory.addPart(new InHouse(1564, "Coffee", 1.99, 50, 0, 100, 19));
        Inventory.addPart(new Outsourced(24775, "Juice", 3.99, 100, 0, 1000, "Greek Grapes"));
        Inventory.addProduct(new Product(1533, "Just Juice", 9.99, 34, 0, 100));
        Inventory.addProduct(new Product(1534, "Just Coffee", 9.99, 34, 0, 100));
        Inventory.addProduct(new Product(1535, "Juice & Coffee", 9.99, 34, 0, 100));

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
