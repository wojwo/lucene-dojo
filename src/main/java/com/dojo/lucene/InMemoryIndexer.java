package com.dojo.lucene;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.IOUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.stream.Stream;

@Slf4j
abstract class InMemoryIndexer implements Closeable {

    private final Directory directory;
    private final Analyzer analyzer;
    private final IndexWriter indexWriter;

    InMemoryIndexer(Directory directory, Analyzer analyzer) throws IOException {
        log.info("Creating indexer");
        this.directory = directory;
        this.analyzer = analyzer;
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        indexWriter = new IndexWriter(directory, indexWriterConfig);
    }

    public void index(Stream<Address> addresses) {
        addresses
                .map(this::toDocument)
                .forEach(this::writeToIndex);
    }

    private Document toDocument(Address address) {
        Document document = new Document();
        document.add(new StringField(Address.POSTCODE_FIELD, address.postcode.toString(), Field.Store.YES));
        document.add(new TextField(Address.ADDRESS_LINE_FIELD, address.addressLine, Field.Store.YES));
        document.add(new StringField(Address.IS_ACTIVE_FIELD, address.isActive.toString(), Field.Store.YES));

        return document;
    }

    private void writeToIndex(Document document) {
        try {
            indexWriter.addDocument(document);
        } catch (IOException e) {
            log.error("Error writing to index");
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        IOUtils.close(directory, analyzer, indexWriter);
        log.info("Index closed");
    }
}
