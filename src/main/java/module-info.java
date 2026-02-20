module com.csc180.brettbeloin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    opens com.csc180.brettbeloin.controllers to javafx.fxml;
    opens com.csc180.brettbeloin to javafx.fxml;

    exports com.csc180.brettbeloin;
    exports com.csc180.brettbeloin.controllers;
}
