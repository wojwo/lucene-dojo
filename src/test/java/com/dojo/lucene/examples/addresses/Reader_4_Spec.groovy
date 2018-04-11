package com.dojo.lucene.examples.addresses

import com.google.common.collect.Lists
import org.apache.lucene.index.Term
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.Query
import org.apache.lucene.search.TermQuery
import spock.lang.Unroll

import static com.dojo.lucene.examples.addresses.AddressMapper.*

class Reader_4_Spec extends ReaderSpecBase {

    def "should not retrieve any addresses by html tag"() {
        given: 'Indexed addresses'
        def htmlTag = 'strong'
        def addresses = Lists.newArrayList(
                Address.of("CR5 1SR", "Dukes House, 202 Maidstone Rd, <$htmlTag>Coulsdon</$htmlTag>", true),
                Address.of("BS23 3TP", "Warne Park, 6 Warne Rd, Weston-super-Mare", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        def results = reader.find(new TermQuery(new Term(ADDRESS_LINE_FIELD, htmlTag)))

        then:
        results.size() == 0
    }
}
