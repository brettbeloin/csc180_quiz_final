package com.csc180.brettbeloin.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.csc180.brettbeloin.controllers.startPageController;
import com.csc180.brettbeloin.models.Question;

import java.util.List;

class startPageControllerTest {
    startPageController stp = new startPageController();

    @Test
    void test_validate_category_success() {
        // Test valid ID extraction
        String result = stp.validate_category("Mythology (id: 20)");
        assertEquals("20", result, "Should extract the ID '20' from the string");
    }

    @Test
    void test_validate_category_failure() {
        // Test invalid ID
        String result = stp.validate_category("Invalid Category (id: 99)");
        assertNull(result, "Should return null for IDs not in the allowed list (15, 18, 20)");
    }

    @Test
    void test_validate_difficulty_valid() {
        // Test difficulty regex logic
        assertNotNull(stp.validate_difficulty("hard"));
        assertNotNull(stp.validate_difficulty("easy"));
        assertNull(stp.validate_difficulty("extreme"), "Should return null for unsupported difficulties");
    }

    @Test
    void test_call_api_integration() {
        // Test live API call with validated parameters
        List<Question> questions = stp.call_api("20", "hard");

        assertNotNull(questions, "API should return a list of questions");
        assertEquals(10, questions.size(), "API should return exactly 10 questions");

        // Verify a specific field in the returned record
        assertNotNull(questions.get(0).question(), "Question text should not be null");
    }
}
