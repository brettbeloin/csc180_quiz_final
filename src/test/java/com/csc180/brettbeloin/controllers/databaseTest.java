package com.csc180.brettbeloin.controllers;

import com.csc180.brettbeloin.dal.MongoDAL;
import com.mongodb.MongoClientException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import com.mongodb.client.model.Filters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;

public class databaseTest {
    private MongoDAL dal;
    private MongoDatabase db;
    private final String collection_name = "quiz";

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
        // joke should be: Unix Time is defined as the number of seconds that have
        // elapsed since when?
        String target_id = "699ae5f3453cc8c41dc1803c";
        Document game = dal.get_document_by_id(db, target_id, collection_name);

        assertNotNull(game);
        assertEquals("Midnight, January 1, 1970", game.getString("correct_answer"));
    }

    @Test
    void test_insert_documents_return_value() {
        String foo = "why did the chicken cross the road";
        Document new_game = new Document("type", "multiple")
                .append("difficulty", "easy")
                .append("question", foo);

        List<Document> to_insert = List.of(new_game);

        // Call the refactored method
        HashMap<ObjectId, String> results = dal.insert_documents(db, "test_collection", to_insert);

        // Assertions
        assertNotNull(results);
        assertEquals(1, results.size());
        assertTrue(results.containsValue(foo));
    }

    @Test
    void test_get_questions_by_genre() {
        String target_difficulty = "easy";
        String target_genre = "Mythology";

        long expected_count = db.getCollection(collection_name)
                .countDocuments(Filters.and(
                        Filters.eq("difficulty", target_difficulty),
                        Filters.eq("category", target_genre)));

        List<Document> result_list = dal.get_questions_by_genre(db, collection_name, target_difficulty, target_genre);
        assertNotNull(result_list);
        assertEquals(expected_count, (long) result_list.size());

        for (Document doc : result_list) {
            assertEquals(target_difficulty, doc.getString("difficulty"));
            assertEquals(target_genre, doc.getString("category"));
        }
    }
}
