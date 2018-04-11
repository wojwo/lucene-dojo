package com.dojo.lucene.common;


import org.apache.lucene.document.Document;

public interface ObjectMapper<T> {

    Document toDocument(T obj);
    T from(Document document);
}
