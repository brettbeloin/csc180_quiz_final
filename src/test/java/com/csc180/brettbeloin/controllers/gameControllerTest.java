package com.csc180.brettbeloin.controllers;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class gameControllerTest {
    GameController controller = new GameController();

    @Test
    void test_calculate_score_accuracy() {
        // Test standard percentage calculation
        double score = controller.calculate_score(8, 2);
        assertEquals(80.0, score, 0.01, "8 correct out of 10 should be 80%");

        // Test 100% case
        double perfect = controller.calculate_score(10, 0);
        assertEquals(100.0, perfect, 0.01);

        // Test 0% case
        double zero = controller.calculate_score(0, 5);
        assertEquals(0.0, zero, 0.01);
    }

    @Test
    void test_extract_category_name() {
        // Test regex extraction of category name before the ID suffix
        String input = "Science: Computers (id: 18)";
        String result = controller.extract_category_name(input);
        assertEquals("Science: Computers", result, "Should strip the (id: X) suffix");

        // Test with Mythology
        String myth = controller.extract_category_name("Mythology (id: 20)");
        assertEquals("Mythology", myth);

        // Test empty return on mismatch
        String invalid = controller.extract_category_name("No ID Here");
        assertEquals("", invalid);
    }

    @Test
    void test_html_decoder() {
        // Test cleaning of common OpenTDB HTML entities
        String input = "Which company was called &#039;Cadabra&quot; &amp; more?";
        String expected = "Which company was called 'Cadabra\" & more?";

        assertEquals(expected, controller.html_decoder(input), "Should convert entities to literal characters");
        assertNull(controller.html_decoder(null), "Should handle null input gracefully");
    }

}
