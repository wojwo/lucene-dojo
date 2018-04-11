package com.dojo.lucene.examples.comments;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceTokenizer;

class CommentAnalyzer extends Analyzer {
    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return new TokenStreamComponents(
                new WhitespaceTokenizer()
        );
    }
}
