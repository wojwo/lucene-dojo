package com.dojo.lucene.common;

import java.io.Closeable;
import java.util.List;

public interface Indexer<T> extends Closeable {
    void index(List<T> objects);
}
