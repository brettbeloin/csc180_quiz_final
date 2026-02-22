package com.csc180.brettbeloin.controllers;

import com.csc180.brettbeloin.models.Game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

public class GameController {

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
    protected void submit() {
        debug("yes I am here");
    }

    @FXML
    protected void start_game() {
        debug("You can't see me");
        /*
         * what Im thinking for this:
         * Display a pop up for if you want to get new question:
         * if you do send back to the start page
         * if not reset the game class
         */
    }

    /*
     * protected void init_data() {
     * 
     * }
     */

    private void debug(String problem) {
        System.out.println(String.format("[DEBUG] %s", problem));
    }

}
