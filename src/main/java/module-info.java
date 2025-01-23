module org.example.sdprototype {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.sdprototype to javafx.fxml;
    exports org.example.sdprototype;
}