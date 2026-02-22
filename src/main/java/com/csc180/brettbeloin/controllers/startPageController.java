package com.csc180.brettbeloin.controllers;

import com.csc180.brettbeloin.models.Question;

import com.csc180.brettbeloin.dal.MongoDAL;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
        add_new_question();
        change_page();
    }

    private void add_new_question() {
        List<Document> documents_to_insert = convert_questions_to_document();
        dal.insert_documents(dal.connect(), "quiz", documents_to_insert);
        // debug(String.format("The Question: %s", foo));
    }

    private List<Document> convert_questions_to_document() {
        List<Question> foo = extract_data();
        List<Document> documents_to_insert = new ArrayList<>();

        for (Question q : foo) {
            Document doc = new Document("type", q.type())
                    .append("difficulty", q.difficulty())
                    .append("category", q.category())
                    .append("question", q.question())
                    .append("correct_answer", q.correct_answer())
                    .append("incorrect_answers", q.incorrect_answers())
                    .append("category", q.category())
                    .append("difficulty", q.difficulty())
                    .append("type", q.type());

            documents_to_insert.add(doc);
        }

        return documents_to_insert;
    }

    private List<Question> extract_data() {
        final String diff = validate_difficulty(this.diff.getValue());
        final String cat = validate_category(this.cat.getValue());

        if (diff == null || cat == null) {
            display_warning("Invalid Entry", "Make sure that both difficulty and category are set");
        }

        return call_api(cat, diff);
    }

    private void change_page() throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/views/Triva.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) this.root_node.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Triva");
        stage.show();
    }

    protected List<Question> call_api(String cat, String diff) {
        ObjectMapper mapper = new ObjectMapper();

        final String question_api_url = String.format(
                "https://opentdb.com/api.php?amount=10&category=%s&difficulty=%s&type=multiple", cat, diff);

        // debug(String.format("url: %s", question_api_url));

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(question_api_url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            var root_node = mapper.readTree(response.body());

            // debug(String.format("Response: %s", response.body()));
            return mapper.convertValue(root_node.get("results"), new TypeReference<>() {
            });

        } catch (Exception e) {
            debug(String.format("[ERROR] %s", e.getMessage()));
            return null;
        }
    }

    protected String validate_category(String cat) {
        if (cat == null) {
            return null;
        }

        Pattern pattern = Pattern.compile("^.*\\(id: (15|18|20)\\)$");
        Matcher matcher = pattern.matcher(cat);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }

    protected String validate_difficulty(String diff) {
        String regex = "^(easy|medium|hard)$";

        if (diff == null) {
            return null;
        }

        if (diff.matches(regex)) {
            return diff;
        }

        return null;
    }

    private void display_warning(String title, String content) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void debug(String problem) {
        System.out.println(String.format("[DEBUG] %s", problem));
    }

}
