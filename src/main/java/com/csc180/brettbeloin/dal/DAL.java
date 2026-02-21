package com.csc180.brettbeloin.dal;

import java.util.HashMap;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoDatabase;

public interface DAL {
    MongoDatabase connect();

    List<Document> get_all_documents(MongoDatabase conn, String collection_name);

    Document get_document_by_id(MongoDatabase conn, String string_id, String collection_name);

    HashMap<ObjectId, String> insert_documents(MongoDatabase conn, String collection_name, List<Document> documents);

    List<Document> get_questions_by_genre(MongoDatabase conn, String genre);

    String ping_database(MongoDatabase conn);
}
