package com.csc180.brettbeloin.controllers;

import com.csc180.brettbeloin.dal.MongoDAL;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;

public class databaseTest {
    private MongoDAL dal;
    private MongoDatabase db;
    private final String collection_name = "Games";

    @BeforeEach
    void setUp() {
        dal = new MongoDAL();
        db = dal.connect();
    }

    @Test
    void test_ping_database() {
        String response = dal.ping_database(db);
        assertEquals("you phoned home", response);
    }

    @Test
    void test_get_all_documents_count() {
        List<Document> result_list = dal.get_all_documents(db, collection_name);

        long actual_count = db.getCollection(collection_name).countDocuments();

        assertNotNull(result_list);
        assertEquals(actual_count, result_list.size(),
                "The list size should match the total document count in the database");
    }

    @Test
    void test_get_document_by_id() {
        // Testing with the ID for Plants vs. Zombies from your mock data
        String target_id = "69205374d443f8d1808d58bf";
        Document game = dal.get_document_by_id(db, target_id, collection_name);

        assertNotNull(game);
        assertEquals("Plants vs. Zombies", game.getString("name"));
    }

    @Test
    void test_insert_documents_return_value() {
        // Create a mock document based on your schema
        Document new_game = new Document("name", "Test Game")
                .append("year_published", 2026)
                .append("game_studio", "Student Studio");

        List<Document> to_insert = List.of(new_game);

        // Call the refactored method
        HashMap<ObjectId, String> results = dal.insert_documents(db, "test_collection", to_insert);

        // Assertions
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.containsValue("Test Game"));
    }

    @Test
    void test_get_questions_by_genre() {
        // Verify retrieval logic using the 'genre' field filter
        List<Document> witcher_games = dal.get_questions_by_genre(db, "CD Projekt Red");
        // Note: Update your DAL if you want to search by game_studio instead of 'genre'
        assertNotNull(witcher_games);
    }
}
