package com.csc180.brettbeloin.controllers;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class startPageController {
    @FXML
    private BorderPane root_node;

    @FXML
    private ComboBox<String> diff;

    @FXML
    private ComboBox<String> cat;

    @FXML
    private Button sub;

    @FXML
    public void submit() throws IOException {
        call_api();
    }

    private void call_api() {
        final String get_diff = diff.getValue();
        final String get_cat = extract_cat_id();
        final String question_api_url = String.format(
                "https://opentdb.com/api.php?amount=10&category=%s&difficulty=%s&type=boolean", get_cat, get_diff);
        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(question_api_url))
                    .GET()
                    .build();

            CompletableFuture<HttpResponse<String>> response = client.sendAsync(request,
                    HttpResponse.BodyHandlers.ofString());

            response.thenAccept(res -> {
                if (res.statusCode() == 200) {
                    System.out.println("Response: " + res.body());
                    connect_mongo(res);
                } else {
                    System.out.println("Error: " + res.statusCode());
                }
            }).join();

        } catch (Exception e) {
            System.out.println("there was an Error");
            System.out.println(e.getMessage());
        }
    }

    private void connect_mongo(HttpResponse<String> response) {
        final String connectionString = "mongodb+srv://Dev:password}@neumont.pjdf2lr.mongodb.net/?appName=neumont";
        final String db = "csc180_quiz_final";
        final String db_coll = "quiz";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongoClient.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    private String extract_cat_id() {
        Pattern pattern = Pattern.compile("\\(id: (\\d+)\\)");
        Matcher matcher = pattern.matcher(cat.getValue());
        if (matcher.find()) {
            return matcher.group(1);
        }

        return "Error: Found nothing";
    }

}
