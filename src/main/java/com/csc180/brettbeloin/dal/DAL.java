package com.csc180.brettbeloin.dal;

import java.net.http.HttpResponse;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public interface DAL {
    void connect(HttpResponse<String> response);

    MongoCollection<Document> get_documents(String name);

    void insert_documents(String collection_name, List<Document> questions);

    List<Document> get_questions_by_genre(String genre);
}
