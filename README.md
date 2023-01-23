# InventoryManagementSystem
An inventory management system based in Java. Built for my C482, Software I class.


Notable ideas I haven't seen included into any other projects on GH:

1) fieldSpy function, activated upon the mouse entering the anchor pane. Uses BooleanBinding and the emptyFields function to monitor text fields, disabling/enabling the 
save button if there fields are filled. Provides a layer of exception prevention because the user cannot submit null data.

2) Seperate functions to check data. verifyData function to nest them into a single function. Cleans the code up, more readable and presentable.
