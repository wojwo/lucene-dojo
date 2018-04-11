package com.dojo.lucene.examples.addresses;

import com.google.common.collect.Lists;
import org.apache.lucene.analysis.*;
import org.apache.lucene.analysis.charfilter.HTMLStripCharFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;

import java.io.Reader;
import java.util.List;

class AddressAnalyzer extends Analyzer {

    final static List<String> customForbiddenWords = Lists.newArrayList(
            "st", "rd"
    );

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        throw new UnsupportedOperationException("Implement me");

        // Solution 1
        // TODO: Implement tokenizer
        // TIP: Check StandardTokenizer
//        final Tokenizer tokenizer = new StandardTokenizer();
//        return new TokenStreamComponents(tokenizer);

        // Solution 2
        // TODO: Add required filtering
        // TIP: StopFilter and LowerCaseFilter
//        final Tokenizer tokenizer = new StandardTokenizer();
//        TokenStream filter = new LowerCaseFilter(tokenizer);
//        filter = new StopFilter(filter, forbidenCharSet());
//        return new TokenStreamComponents(tokenizer, filter);
    }

    // Solution 4
    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        // TODO: Implement me
        // TIP: you can use HTMLStripCharFilter
//        return super.initReader(fieldName, new HTMLStripCharFilter(reader));
        return super.initReader(fieldName, reader);
    }

    private CharArraySet forbidenCharSet() {
        final CharArraySet charArraySet = new CharArraySet(customForbiddenWords, false);
        charArraySet.addAll(StandardAnalyzer.STOP_WORDS_SET);
        return charArraySet;
    }
}
