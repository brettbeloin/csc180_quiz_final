package com.csc180.brettbeloin.controllers;

import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import com.csc180.brettbeloin.models.Game;
import com.csc180.brettbeloin.dal.MongoDAL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GameController {
    private final MongoDAL dal = new MongoDAL();
    private String difficulty;
    private String category;
    private Game game_instance;
    private List<Document> game_questions;
    private List<Document> test_questions = new ArrayList<>();
    private int current_question = 0;

    @FXML
    private BorderPane root_node;

    @FXML
    private VBox game_box;

    @FXML
    private Label question_id;

    @FXML
    private Label question_box;

    @FXML
    private Button new_game;

    @FXML
    private Button start_game;

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button btn4;

    @FXML
    private ImageView score_image;

    @FXML
    private TextArea stats;

    @FXML
    private void submit(ActionEvent event) {
        if (this.current_question < this.test_questions.size() - 1) {
            check_answers(event, this.current_question);
            set_ui(this.current_question);
        } else {
            disable_buttons();
            display_image();
            this.new_game.setVisible(true);
            this.start_game.setVisible(true);
        }

    }

    @FXML
    private void new_game() throws IOException {
        // debug("You can see me now");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/startPage.fxml"));
        Parent root = loader.load();

        Stage stage = (Stage) this.root_node.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Triva");
        stage.show();
    }

    @FXML
    private void start_game() throws IOException {
        // debug("You can see me now");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Triva.fxml"));
        Parent root = loader.load();

        GameController gameController = loader.getController();
        gameController.init_data(this.difficulty, this.category);

        Stage stage = (Stage) this.root_node.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Triva");
        stage.show();

    }

    private void display_image() {
        String path = (this.game_instance.getScore() >= 75.0) ? "/img/happy.jpg" : "/img/angry.jpg";

        Image image = new Image(getClass().getResource(path).toExternalForm());

        this.score_image.setImage(image);
        this.score_image.setFitWidth(100);
        this.score_image.setPreserveRatio(true);
        this.score_image.setVisible(true);
    }

    protected String html_decoder(String input) {
        if (input == null)
            return null;

        return input.replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&amp;", "&")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&rsquo;", "'")
                .replace("&deg;", "°");
    }

    private void disable_buttons() {
        for (Node node : this.game_box.getChildren()) {
            if (node instanceof Button) {
                node.setDisable(true);
            }
        }
    }

    protected void init_data(String difficulty, String category) {
        this.difficulty = difficulty;
        this.category = extract_category_name(category);

        this.game_instance = new Game(0, 0, 0.0);
        this.game_questions = get_question(difficulty, this.category);

        this.current_question = 0;
        set_ui(this.current_question);
    }

    private void set_ui(int curr_question) {
        List<String> answers = randomize_questions(curr_question);

        this.question_id.setText(String.format("Question: %d", curr_question + 1));
        this.question_box.setText(html_decoder(this.game_questions.get(curr_question).getString("question")));
        this.stats.setText(this.game_instance.toString());

        this.btn1.setText(answers.get(0));
        this.btn2.setText(answers.get(1));
        this.btn2.setText(answers.get(2));
        this.btn4.setText(answers.get(3));

    }

    private void check_answers(ActionEvent event, int curr_question) {
        Button clicked_button = (Button) event.getSource();
        String submited_answer = clicked_button.getText();
        String correct_answer = this.game_questions.get(curr_question).getString("correct_answer");

        if (submited_answer.equals(correct_answer)) {
            this.game_instance.setCorrect_guesses(this.game_instance.getCorrect_guesses() + 1);
        } else {
            this.game_instance.setWrong_guesses(this.game_instance.getWrong_guesses() + 1);
        }

        if (this.current_question < this.test_questions.size() - 1) {
            this.current_question++;
        }

        this.game_instance
                .setScore(calculate_score(this.game_instance.getCorrect_guesses(),
                        this.game_instance.getWrong_guesses()));
    }

    protected List<String> create_question_array(int curr_question) {
        Document doc = this.game_questions.get(curr_question);

        List<String> raw_incorrect = (List<String>) doc.get("incorrect_answers");
        List<String> clean_answers = new ArrayList<>();
        for (String s : raw_incorrect) {
            clean_answers.add(html_decoder(s));
        }

        clean_answers.add(html_decoder(doc.getString("correct_answer")));
        return clean_answers;
    }

    private List<String> randomize_questions(int curr_question) {
        var foo = create_question_array(curr_question);

        // debug(foo.toString());

        Collections.shuffle(foo);

        return foo;
    }

    protected double calculate_score(int correct_guesses, int wrong_guesses) {
        int total_question = correct_guesses + wrong_guesses;

        if (wrong_guesses == 0) {
            return 100.0;
        }

        return ((double) correct_guesses / total_question) * 100;
    }

    protected String extract_category_name(String category_name) {
        String regex = "^(.+?)(?=\s*\\(id:)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(category_name);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return category_name;
    }

    protected List<Document> get_question(String difficulty, String category) {
        List<Document> questions = this.dal.get_questions_by_genre(this.dal.connect(), "quiz", difficulty, category);

        this.test_questions.clear();

        Collections.shuffle(questions);
        int limit = Math.min(10, questions.size());

        for (int i = 0; i < limit; i++) {
            this.test_questions.add(questions.get(i));
        }

        return this.test_questions;
    }

    private void debug(String problem) {
        System.out.println(String.format("[DEBUG] %s", problem));
    }

}
