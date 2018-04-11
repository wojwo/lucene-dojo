package com.dojo.lucene.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class BaseReader<T> implements Reader<T> {

    private final int DEFAULT_NUMBER_OF_RESULTS = 10;

    private final Directory directory;
    private final ObjectMapper<T> objectMapper;
    private final IndexReader indexReader;
    private final IndexSearcher indexSearcher;

    public BaseReader(Directory directory, ObjectMapper<T> objectMapper) {
        this.directory = directory;
        this.objectMapper = objectMapper;
        try {
            this.indexReader = DirectoryReader.open(directory);
        } catch (IOException e) {
            log.error("Failed to create index reader");
            throw new RuntimeException(e);
        }
        this.indexSearcher = new IndexSearcher(indexReader);
    }

    @Override
    public List<T> find(Query query) {
        return find(query, DEFAULT_NUMBER_OF_RESULTS);
    }

    @Override
    public List<T> find(Query query, int numberOfResults) {
        try {
            final TopDocs topDocs = indexSearcher.search(query, numberOfResults);
            return Stream.of(topDocs.scoreDocs)
                    .map(this::getDocument)
                    .map(objectMapper::from)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Failed to search documents");
            throw new RuntimeException(e);
        }
    }

    private Document getDocument(ScoreDoc scoreDoc) {
        try {
            return indexSearcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            log.error("Failed to get document {}", scoreDoc.doc);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws IOException {
        IOUtils.close(directory, indexReader);
        log.debug("Reader closed");
    }
}
