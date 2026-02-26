package com.csc180.brettbeloin.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;

import com.csc180.brettbeloin.models.Game;
import com.csc180.brettbeloin.dal.MongoDAL;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameController {
    private final MongoDAL dal = new MongoDAL();
    private String difficulty;
    private String category;
    private Game game_instance;
    private List<Document> game_questions;

    @FXML
    private BorderPane root_node;

    @FXML
    private Label question_id;

    @FXML
    Label question_box;

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
    private void submit() {
        debug("yes I am here");
    }

    @FXML
    private void start_game() {
        debug("You can't see me");
        /*
         * what Im thinking for this:
         * Display a pop up for if you want to get new question:
         * if you do send back to the start page
         * if not reset the game class
         */
    }

    protected void init_data(String difficulty, String category) {
        this.difficulty = difficulty;
        this.category = extract_category_name(category);

        this.game_instance = new Game(0, 0, 0.0);
        this.game_questions = get_question(difficulty, this.category);
        // debug(tmp.getFirst().get("question").toString());
        set_ui(0);
    }

    private void set_ui(int curr_question) {
        var questions = randomize_questions(curr_question);

        this.question_id.setText(String.format("Question: %s", Integer.toString(++curr_question)));
        this.question_box.setText(this.game_questions.get(curr_question).get("question").toString());
    }

    private List<String> create_question_array(int curr_question) {
        List<String> question_answers = (ArrayList) game_questions.get(curr_question).get("incorrect_answers");
        question_answers.add(game_questions.get(curr_question).get("correct_answer").toString());
        return question_answers;
    }

    private List<String> randomize_questions(int curr_question) {
        Random rand = new Random();
        var foo = create_question_array(curr_question);
        int baz = rand.nextInt((0 - foo.size()) + 1) + foo.size();
        List<String> bar;

        return bar;
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
        return questions;
    }

    private void debug(String problem) {
        System.out.println(String.format("[DEBUG] %s", problem));
    }

}
