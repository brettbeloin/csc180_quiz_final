package com.csc180.brettbeloin.controllers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import com.csc180.brettbeloin.controllers.startPageController;
import com.csc180.brettbeloin.models.Question;

import java.util.List;

class startPageControllerTest {
    startPageController stp = new startPageController();

    @Test
    void call_api() {
        List<Question> foo = stp.call_api("18", "hard");
        assertEquals(10, foo.size());
    }

    @Test
    void get_id_value() {
        String foo = stp.validate_category("Mythology (id: 20)");

        assertNotNull(foo);
        assertEquals("20", foo);
    }

    @Test
    void get_diff() {
        String foo = stp.validate_difficulty("hard");

        assertNotNull(foo);
        assertEquals("hard", foo);
    }
}
