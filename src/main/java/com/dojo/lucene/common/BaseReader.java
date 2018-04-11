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

    public BaseReader(Directory directory, ObjectMapper<T> objectMapper) {
        this.directory = directory;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<T> find(Query query) {
        return find(query, DEFAULT_NUMBER_OF_RESULTS);
    }

    @Override
    public List<T> find(Query query, int size) {
        try {
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            final TopDocs topDocs = indexSearcher.search(query, size);

            final List<T> results = Stream.of(topDocs.scoreDocs)
                    .map(scoreDoc -> getDocument(indexSearcher, scoreDoc))
                    .map(objectMapper::from)
                    .collect(Collectors.toList());

            indexReader.close();
            return results;
        } catch (IOException e) {
            log.error("Failed to search documents");
            throw new RuntimeException(e);
        }
    }

    private Document getDocument(IndexSearcher indexSearcher, ScoreDoc scoreDoc) {
        try {
            return indexSearcher.doc(scoreDoc.doc);
        } catch (IOException e) {
            log.error("Failed to get document {}", scoreDoc.doc);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        try {
            IOUtils.close(directory);
            log.debug("Reader closed");
        } catch (IOException e) {
            log.error("Failed closing reader");
            throw new RuntimeException(e);
        }
    }
}
