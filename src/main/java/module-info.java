module org.example.digitallibrarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens org.example.Library to javafx.base;
    opens org.example.Accounts to javafx.base;
    opens org.example.digitallibrarymanagementsystem to javafx.fxml;
    opens org.example to javafx.fxml;

    exports org.example.digitallibrarymanagementsystem;
    exports org.example;
}