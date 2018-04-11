package com.dojo.lucene.examples._1;

import com.dojo.lucene.common.BaseIndexer;
import com.dojo.lucene.common.BaseReader;
import com.dojo.lucene.common.ObjectMapper;
import com.dojo.lucene.common.Searcher;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.util.List;

class CommentSearcher implements Searcher<Comment> {

    private final Directory directory;
    private final Analyzer analyzer;
    private final ObjectMapper<Comment> objectMapper;

    public CommentSearcher(Directory directory) {
        this.directory = directory;
        this.analyzer = new CommentAnalyzer();
        this.objectMapper = new CommentMapper();
    }

    public CommentSearcher() {
        this(new RAMDirectory());
    }

    @Override
    public void index(List<Comment> objects) {
        baseIndexer().index(objects);
    }

    @Override
    public List<Comment> find(Query query) {
        return baseReader().find(query);
    }

    @Override
    public List<Comment> find(Query query, int size) {
        return baseReader().find(query, size);
    }

    private BaseIndexer<Comment> baseIndexer() {
        return new BaseIndexer<>(
                directory,
                analyzer,
                objectMapper
        );
    }

    private BaseReader<Comment> baseReader() {
        return new BaseReader<>(
                directory,
                objectMapper
        );
    }
}
