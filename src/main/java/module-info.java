module com.csc180.brettbeloin {
    requires java.net.http;

    requires javafx.controls;
    requires javafx.fxml;

    requires org.mongodb.bson;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires com.fasterxml.jackson.databind;

    // add this back if I want logging of mongo
    // requires org.slf4j;

    opens com.csc180.brettbeloin.controllers to javafx.fxml;
    opens com.csc180.brettbeloin to javafx.fxml;

    exports com.csc180.brettbeloin;
    exports com.csc180.brettbeloin.controllers;
    exports com.csc180.brettbeloin.dal;
}
