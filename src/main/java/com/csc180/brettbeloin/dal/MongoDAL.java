package com.csc180.brettbeloin.dal;

import com.mongodb.ConnectionString;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertManyResult;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import static com.mongodb.client.model.Filters.eq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MongoDAL implements DAL {

    @Override
    public MongoDatabase connect() {
        final String connection_string = "mongodb+srv://Dev:password}@neumont.pjdf2lr.mongodb.net/?appName=neumont";
        final String db = "csc180_quiz_final";
        final MongoDatabase database;
        final MongoClient mongo_client;

        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();

        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connection_string))
                .serverApi(serverApi)
                .build();

        try {
            mongo_client = MongoClients.create(settings);
            return database = mongo_client.getDatabase(db);
        } catch (MongoException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String ping_database(MongoDatabase conn) {
        conn.runCommand(new Document("ping", 1));
        return "you phoned home";
    }

    @Override
    public List<Document> get_all_documents(MongoDatabase conn, String collection_name) {
        if (conn == null) {
            return new ArrayList<>();
        }

        return conn.getCollection(collection_name)
                .find()
                .into(new ArrayList<>());
    }

    @Override
    public Document get_document_by_id(MongoDatabase conn, String string_id, String collection_name) {
        try {
            ObjectId obj_id = new ObjectId(string_id);

            return conn.getCollection(collection_name)
                    .find(eq("_id", obj_id))
                    .first();

        } catch (IllegalArgumentException e) {
            System.err.println("Invalid ObjectId format: " + string_id);
            return null;
        }
    }

    @Override
    public HashMap<ObjectId, String> insert_documents(MongoDatabase conn, String collection_name,
            List<Document> documents) {
        HashMap<ObjectId, String> insertedIds = new HashMap<>();

        // debug(String.format("documents: %s", documents.toString()));

        try {
            InsertManyResult result = conn.getCollection(collection_name).insertMany(documents);
            // debug(String.format("results: %s", result.toString()));

            result.getInsertedIds().forEach((index, value) -> {
                insertedIds.put(value.asObjectId().getValue(), documents.get(index).getString("question"));
            });

            System.out.println("Inserted documents with the following id and name: " + insertedIds);

            return insertedIds;
        } catch (MongoBulkWriteException exception) {

            exception.getWriteResult().getInserts().forEach(upsert -> {
                int index = upsert.getIndex();
                ObjectId id = upsert.getId().asObjectId().getValue();
                insertedIds.put(id, documents.get(index).getString("question"));
            });

            System.out.println("A MongoBulkWriteException occurred, but there are " +
                    "successfully processed documents with the following id and name: " + insertedIds);

            return insertedIds;
        }

    }

    @Override
    public List<Document> get_questions_by_genre(MongoDatabase conn, String collenction_name, String difficulty,
            String category) {
        Bson filter = Filters.and(Filters.eq("difficulty", difficulty), Filters.eq("category", category));

        List<Document> matched_docs = conn.getCollection(collenction_name)
                .find(filter)
                .into(new ArrayList<Document>());

        return matched_docs;
    }

    private void debug(String problem) {
        System.out.println(String.format("[DEBUG] %s", problem));
    }
}
