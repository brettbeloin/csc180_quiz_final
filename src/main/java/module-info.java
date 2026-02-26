module com.csc180.brettbeloin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.driver.core;
    requires org.mongodb.bson;
    requires javafx.graphics;

    opens com.csc180.brettbeloin to javafx.fxml, org.junit.platform.commons;
    opens com.csc180.brettbeloin.controllers to javafx.fxml;
    opens com.csc180.brettbeloin.models to com.fasterxml.jackson.databind;

    exports com.csc180.brettbeloin;
    exports com.csc180.brettbeloin.controllers;
    exports com.csc180.brettbeloin.dal;
}
