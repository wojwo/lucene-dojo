package com.dojo.lucene.examples.addresses

import com.dojo.lucene.common.BaseIndexer
import com.dojo.lucene.common.BaseReader
import com.dojo.lucene.common.Indexer
import com.dojo.lucene.common.Reader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.store.RAMDirectory
import spock.lang.Specification

class ReaderSpecBase extends Specification {

    Indexer<Address> indexer
    Reader<Address> reader
    Analyzer analyzer

    def setup() {
        def directory = new RAMDirectory()
        def commentMapper = new AddressMapper()
        analyzer = new AddressAnalyzer()
        indexer = new BaseIndexer(directory, analyzer, commentMapper)
        reader = new BaseReader(directory, commentMapper)
    }
}
