package com.dojo.lucene;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.util.IOUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class AddressReader implements AutoCloseable {

    private final IndexReader indexReader;
    private final IndexSearcher indexSearcher;

    AddressReader(Directory directory) throws IOException {
        indexReader = DirectoryReader.open(directory);
        indexSearcher = new IndexSearcher(indexReader);

    }

    public List<Address> read() throws IOException {
        final TopDocs topDocs = indexSearcher.search(new MatchAllDocsQuery(), 10);
        return Stream.of(topDocs.scoreDocs)
                .map(scoreDoc -> {
                    try {
                        final Document document = indexSearcher.doc(scoreDoc.doc);
                        return Address.of(
                                Postcode.of(document.get(Address.POSTCODE_FIELD)),
                                document.get(Address.ADDRESS_LINE_FIELD),
                                Boolean.valueOf(document.get(Address.IS_ACTIVE_FIELD))
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    public void close() throws Exception {
        IOUtils.close(indexReader);
    }
}
