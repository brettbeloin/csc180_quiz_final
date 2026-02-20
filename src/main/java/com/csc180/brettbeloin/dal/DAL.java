package com.csc180.brettbeloin.dal;

import java.net.http.HttpResponse;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoDatabase;

public interface DAL {
    MongoDatabase connect(HttpResponse<String> response);

    List<Document> get_documents(MongoDatabase conn, String collection_name);

    Document get_document_by_id(MongoDatabase conn, String string_id, String collection_name);

    void insert_documents(MongoDatabase conn, String collection_name, List<Document> documents);

    List<Document> get_questions_by_genre(MongoDatabase conn, String genre);
}
