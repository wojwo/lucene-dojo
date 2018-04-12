package com.dojo.lucene.examples.addresses;

import com.google.common.collect.Lists;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.Reader;
import java.util.List;

class AddressAnalyzer extends Analyzer {

    final static List<String> customForbiddenWords = Lists.newArrayList(
            "st", "rd"
    );

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        // TODO: Implement tokenizer
        throw new UnsupportedOperationException("Implement me");
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        return super.initReader(fieldName, reader);
    }

    private CharArraySet forbidenCharSet() {
        final CharArraySet charArraySet = new CharArraySet(customForbiddenWords, false);
        charArraySet.addAll(StandardAnalyzer.STOP_WORDS_SET);
        return charArraySet;
    }
}
