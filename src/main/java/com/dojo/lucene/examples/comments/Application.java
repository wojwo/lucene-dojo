package com.dojo.lucene.examples.comments;

import com.dojo.lucene.common.BaseIndexer;
import com.dojo.lucene.common.Indexer;
import com.google.common.collect.Lists;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

class Application {

    private static final String INDEX_DIR = "/Users/Wojtek/Documents/dojo/basic";

    public static void main(String[] args) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));

        Indexer<Comment> indexer = new BaseIndexer<>(
                directory,
                new CommentAnalyzer(),
                new CommentMapper()
        );

        List<Comment> comments = Lists.newArrayList(
                Comment.of("Wojtek", "Dojo time"),
                Comment.of("Jack", "Let's practice Lucene"),
                Comment.of("Wojtek", "Lucene is cool"),
                Comment.of("Mike", "Lucene best practices"),
                Comment.of("Jack", "Lucene in Action")
        );

        indexer.index(comments);
    }
}
