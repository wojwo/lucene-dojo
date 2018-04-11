package com.dojo.lucene.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class BaseIndexer<T> implements Indexer<T> {

    private final Directory directory;
    private final Analyzer analyzer;
    private final ObjectMapper<T> mapper;

    public BaseIndexer(Directory directory, Analyzer analyzer, ObjectMapper<T> mapper) {
        this.directory = directory;
        this.analyzer = analyzer;
        this.mapper = mapper;
    }

    public void index(List<T> objects) {
        final List<Document> documents = objects.stream()
                .map(mapper::toDocument)
                .collect(Collectors.toList());
        writeToIndex(documents);
    }

    private void writeToIndex(List<Document> documents) {
        try {
            IndexWriter indexWriter = indexWriter();
            indexWriter.addDocuments(documents);
            indexWriter.commit();
            indexWriter.close();
        } catch (IOException e) {
            log.error("Failed writing to index");
            throw new RuntimeException(e);
        }
    }

    private IndexWriter indexWriter() {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        try {
            return new IndexWriter(directory, indexWriterConfig);
        } catch (IOException e) {
            log.error("Failed to create index writer");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            IOUtils.close(directory, analyzer);
            log.debug("Indexer closed");
        } catch (IOException e) {
            log.error("Failed closing indexer");
            throw new RuntimeException(e);
        }
    }
}
