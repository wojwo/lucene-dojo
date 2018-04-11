package com.dojo.lucene.examples.comments

import com.dojo.lucene.common.BaseIndexer
import com.dojo.lucene.common.BaseReader
import com.dojo.lucene.common.Indexer
import com.dojo.lucene.common.Reader
import com.google.common.collect.Lists
import org.apache.lucene.index.Term
import org.apache.lucene.search.MatchAllDocsQuery
import org.apache.lucene.search.TermQuery
import org.apache.lucene.store.RAMDirectory
import spock.lang.Specification

import static com.dojo.lucene.examples.comments.CommentMapper.COMMENT_FIELD
import static com.dojo.lucene.examples.comments.CommentMapper.USERNAME_FIELD

class ReaderSpec extends Specification {

    Indexer indexer
    Reader reader

    def setup() {
        def directory = new RAMDirectory()
        def commentMapper = new CommentMapper()
        indexer = new BaseIndexer(directory, new CommentAnalyzer(), commentMapper)
        reader = new BaseReader(directory, commentMapper)
    }

    def "should retrieve all comments"() {
        given: 'Indexed comments'
        def comments = Lists.newArrayList(
                Comment.of("Wojtek", "Dojo time"),
                Comment.of("Jack", "Let's practice Lucene"),
                Comment.of("Wojtek", "Lucene is cool")
        )
        indexer.index(comments)

        when: 'retrieving all comments'
        def results = reader.find(new MatchAllDocsQuery(), comments.size())

        then:
        results.size() == comments.size()
    }

    def "Should retrieve comments by user name"() {
        given: 'Indexed comments'
        def userName = "John"
        def comments = Lists.newArrayList(
                Comment.of(userName, "New comment from John"),
                Comment.of(userName, "Another comment from John"),
                Comment.of("Wojtek", "Dojo time"),
                Comment.of("Jack", "Let's practice Lucene"),
                Comment.of("Wojtek", "Lucene is cool")
        )
        indexer.index(comments)

        when: 'retrieving comments by user name'
        def results = reader.find(
                new TermQuery(
                        new Term(USERNAME_FIELD, userName)
                )
        )

        then:
        results.size() == 2
    }

    def "should retrieve comments by term in comment field"() {
        given: 'Indexed comments'
        def term = 'Lucene'
        def comments = Lists.newArrayList(
                Comment.of("John", "New comment from John"),
                Comment.of("Mike", "Another comment from John"),
                Comment.of("Wojtek", "$term dojo time"),
                Comment.of("Jack", "Let's practice $term"),
                Comment.of("Wojtek", "$term is cool")
        )
        indexer.index(comments)

        when: 'retrieving comments by term'
        def results = reader.find(new TermQuery(
                new Term(COMMENT_FIELD, term)
        ))

        then:
        results.size() == 3
    }
}
