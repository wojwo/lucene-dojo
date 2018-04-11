package com.dojo.lucene.common;

import org.apache.lucene.search.Query;

import java.util.List;

public interface Searcher<T> {

    void index(List<T> objects);
    List<T> find(Query query);
    List<T> find(Query query, int size);
}
