package com.dojo.lucene.examples._1

import com.dojo.lucene.common.Searcher
import com.dojo.lucene.examples._1.Comment
import com.dojo.lucene.examples._1.CommentSearcher
import com.google.common.collect.Lists
import org.apache.lucene.index.Term
import org.apache.lucene.search.MatchAllDocsQuery
import org.apache.lucene.search.TermQuery
import spock.lang.Ignore
import spock.lang.Specification

import static com.dojo.lucene.examples._1.CommentMapper.*

class CommentSearcherSpec extends Specification {

    final List<Comment> initialComments = Lists.newArrayList(
            Comment.of("Wojtek", "Dojo time"),
            Comment.of("Jack", "Let's practice Lucene"),
            Comment.of("Wojtek", "Lucene is cool"),
            Comment.of("Mike", "Lucene best practices"),
            Comment.of("Jack", "Lucene in Action")
    )

    Searcher searcher

    def setup() {
        searcher = new CommentSearcher()
        searcher.index(initialComments)
    }

    def "should retrieve all comments"() {
        when: 'searching for all comments'
        def results = searcher.find(new MatchAllDocsQuery(), initialComments.size())

        then:
        results.size() == initialComments.size()
    }

    def "should reindex new comments"() {
        given: 'Two new comments'
        def newComments = Lists.newArrayList(
                Comment.of("John", "New comment"),
                Comment.of("John", "Another comment")
        )

        when: 'indexing new comments'
        searcher.index(newComments)

        then: 'should find old and new comments'
        def numOfIndexedComments = initialComments.size() + newComments.size()
        def results = searcher.find(new MatchAllDocsQuery(), numOfIndexedComments)
        results.size() == numOfIndexedComments
    }

    def "should retrieve all user comments"() {
        given: 'Two comments for new user'
        def userName = "John"
        def comments = Lists.newArrayList(
                Comment.of(userName, "New comment from John"),
                Comment.of(userName, "Another comment from John")
        )
        searcher.index(comments)

        when: 'searching new user comments'
        def results = searcher.find(
                new TermQuery(
                        new Term(USERNAME_FIELD, userName)
                )
        )

        then:
        results.size() == comments.size()
    }

    @Ignore
    def "should retrieve comment when searching with term in lower case"() {
        given: 'Comment with word in upper case'
        def uppderCaseWord = 'WORD'
        def comment = Comment.of(
                "user",
                "Comment with upper case ${uppderCaseWord}"
        )
        searcher.index(Lists.newArrayList(comment))

        when: 'retrieve a comment using term in lower case'
        def results = searcher.find(
                new TermQuery(
                        new Term(COMMENT_FIELD, uppderCaseWord.toLowerCase())
                )
        )

        then:
        results.size() == 1

    }

    @Ignore
    def "should not retrieve any comment when searching with term in upper case"() {
        given: 'Comment with word in upper case'
        def uppderCaseWord = 'WORD'
        def comment = Comment.of(
                "user",
                "Comment with upper case ${uppderCaseWord}"
        )
        searcher.index(Lists.newArrayList(comment))

        when: 'retrieve a comment using term in upper case'
        def results = searcher.find(
                new TermQuery(
                        new Term(COMMENT_FIELD, uppderCaseWord.toUpperCase())
                )
        )

        then:
        results.size() == 0

    }
}
