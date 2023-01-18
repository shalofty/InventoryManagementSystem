module com.example.inv_man_sys {
    requires javafx.controls;
    requires javafx.fxml;


    opens InventoryManagementSystem to javafx.fxml;
    exports InventoryManagementSystem;
}