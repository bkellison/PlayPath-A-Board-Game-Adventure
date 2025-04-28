module org.example.sdprototype {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.sdprototype to javafx.fxml;
    exports org.example.sdprototype.Application;
    opens org.example.sdprototype.Application to javafx.fxml;
    exports org.example.sdprototype.UI;
    opens org.example.sdprototype.UI to javafx.fxml;
    exports org.example.sdprototype.Controllers;
    opens org.example.sdprototype.Controllers to javafx.fxml;
}