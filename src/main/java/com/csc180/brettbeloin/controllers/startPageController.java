package com.csc180.brettbeloin.controllers;

import com.csc180.brettbeloin.dal.MongoDAL;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.bson.json.JsonObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

public class startPageController {
    private MongoDAL dal = new MongoDAL();
    final String db_coll = "quiz";
    final String t_db_coll = "Games";

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

    private String call_api() {
        final String get_diff = diff.getValue();
        final String get_cat = extract_cat_id();
        final String question_api_url = String.format(
                "https://opentdb.com/api.php?amount=10&category=%s&difficulty=%s&type=multiple", get_cat, get_diff);
        System.out.println("url: " + question_api_url);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(question_api_url))
                    .GET()
                    .build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(res -> {
                        if (res.statusCode() == 200) {
                            System.out.println("Response: " + res.body());
                            return res.body();
                        } else {
                            System.out.println("Error: " + res.statusCode());
                            return null;
                        }
                    })
                    .join();

        } catch (Exception e) {
            System.out.println("there was an Error");
            System.out.println(e.getMessage());
            return null;
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
