package com.csc180.brettbeloin.controllers;

import com.csc180.brettbeloin.models.Question;

import com.csc180.brettbeloin.dal.MongoDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        final String get_diff = diff.getValue();
        final String get_cat = extract_cat_id();
        List<Question> foo = call_api(get_cat, get_diff);
        System.out.println("The Question: " + foo);
    }

    public List<Question> call_api(String get_cat, String get_diff) {
        ObjectMapper mapper = new ObjectMapper();

        final String question_api_url = String.format(
                "https://opentdb.com/api.php?amount=10&category=%s&difficulty=%s&type=multiple", get_cat, get_diff);
        System.out.println("url: " + question_api_url);

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(question_api_url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            var root_node = mapper.readTree(response.body());

            // System.out.println("Response: " + response.body());
            return mapper.convertValue(root_node.get("results"), new TypeReference<>() {
            });

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
