package com.dojo.lucene.common;

import org.apache.lucene.search.Query;

import java.io.Closeable;
import java.util.List;

public interface Reader<T> extends Closeable {

    List<T> find(Query query);
    List<T> find(Query query, int numberOfResults);
}
