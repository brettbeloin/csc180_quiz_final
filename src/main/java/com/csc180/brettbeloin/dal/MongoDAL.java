package com.csc180.brettbeloin.dal;

import java.net.http.HttpResponse;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MongoDAL implements DAL {
    private MongoDatabase database;

    @Override
    public void connect(HttpResponse<String> response) {
        final String connection_string = "mongodb+srv://Dev:password}@neumont.pjdf2lr.mongodb.net/?appName=neumont";
        final String db = "csc180_quiz_final";
        final String db_coll = "quiz";

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connection_string))
                .serverApi(serverApi)
                .build();

        // Create a new client and connect to the server
        try (MongoClient mongo_client = MongoClients.create(settings)) {
            try {
                // Send a ping to confirm a successful connection
                MongoDatabase database = mongo_client.getDatabase("admin");
                database.runCommand(new Document("ping", 1));
                System.out.println("Pinged your deployment. You successfully connected to MongoDB!");
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MongoCollection<Document> get_documents(String name) {
        return database.getCollection(name);
    }

    @Override
    public void insert_documents(String collection_name, List<Document> questions) {
        database.getCollection(collection_name).insertMany(questions);
    }

    @Override
    public List<Document> get_questions_by_genre(String genre) {
        return database.getCollection("questions")
                .find(new Document("genre", genre))
                .into(new ArrayList<>());
    }

}
