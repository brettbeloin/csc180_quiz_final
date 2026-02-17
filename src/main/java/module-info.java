module com.csc180.brettbeloin {
    opens com.csc180.brettbeloin to javafx.fxml;

    requires javafx.controls;
    requires javafx.fxml;

    opens com.csc180.brettbeloin.controllers to javafx.fxml;

    exports com.csc180.brettbeloin;
    exports com.csc180.brettbeloin.controllers;
}
