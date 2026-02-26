package com.csc180.brettbeloin.controllers;

import javafx.scene.control.TextArea;
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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class GameController {
    private final MongoDAL dal = new MongoDAL();
    private String difficulty;
    private String category;
    private Game game_instance;
    private List<Document> game_questions;
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
    private TextArea stats;

    @FXML
    private void submit(ActionEvent event) {
        if (this.current_question <= 10) {
            check_answers(event, this.current_question);
            set_ui(this.current_question);
            this.game_instance
                    .setScore(calculate_score(game_instance.getCorrect_guesses(), game_instance.getWrong_guesses()));
        }
    }

    @FXML
    private void new_game() {
        debug("You can see me now");
    }

    @FXML
    private void start_game() {
        debug("You can see me now");
        /*
         * what Im thinking for this:
         * Display a pop up for if you want to get new question:
         * if you do send back to the start page
         * if not reset the game class
         */
    }

    private void disable_buttons() {
        for (Node node : game_box.getChildren()) {
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
        // debug(tmp.getFirst().get("question").toString());
        set_ui(this.current_question);

    }

    private void set_ui(int curr_question) {
        List<String> answers = randomize_questions(curr_question);

        this.question_id.setText(String.format("Question: %d", curr_question + 1));
        this.question_box.setText(this.game_questions.get(curr_question).getString("question"));

        btn1.setText(answers.get(0));
        btn2.setText(answers.get(1));
        btn3.setText(answers.get(2));
        btn4.setText(answers.get(3));

        if (this.current_question == 10) {
            disable_buttons();
            new_game.setVisible(true);
            start_game.setVisible(true);
        }

        stats.setText(game_instance.toString());
    }

    private void check_answers(ActionEvent event, int curr_question) {
        Button clicked_button = (Button) event.getSource();
        String submited_answer = clicked_button.getText();
        String correct_answer = this.game_questions.get(curr_question).getString("correct_answer");

        if (submited_answer.equals(correct_answer)) {
            game_instance.setCorrect_guesses(game_instance.getCorrect_guesses() + 1);
        } else {
            game_instance.setWrong_guesses(game_instance.getWrong_guesses() + 1);
        }

        this.current_question++;
    }

    private List<String> create_question_array(int curr_question) {
        Document doc = game_questions.get(curr_question);

        List<String> question_answers = new ArrayList<>((List<String>) doc.get("incorrect_answers"));
        question_answers.add(doc.getString("correct_answer"));

        // List<String> question_answers = (ArrayList)
        // game_questions.get(curr_question).get("incorrect_answers");
        // question_answers.add(game_questions.get(curr_question).get("correct_answer").toString());
        return question_answers;
    }

    private List<String> randomize_questions(int curr_question) {
        var foo = create_question_array(curr_question);

        debug(foo.toString());

        Collections.shuffle(foo);

        return foo;
    }

    protected double calculate_score(int correct_guesses, int wrong_guesses) {
        if (wrong_guesses == 0) {
            return 100.0;
        }

        return correct_guesses / wrong_guesses;
    }

    protected String extract_category_name(String category_name) {
        String regex = "^(.+?)(?=\s*\\(id:)";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(category_name);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return "";
    }

    protected List<Document> get_question(String difficulty, String category) {
        List<Document> questions = dal.get_questions_by_genre(dal.connect(), "quiz", difficulty, category);
        Collections.shuffle(questions);
        return questions;
    }

    private void debug(String problem) {
        System.out.println(String.format("[DEBUG] %s", problem));
    }

}
