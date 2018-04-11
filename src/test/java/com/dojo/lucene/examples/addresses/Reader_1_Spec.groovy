package com.dojo.lucene.examples.addresses

import com.google.common.collect.Lists
import org.apache.lucene.index.Term
import org.apache.lucene.search.MatchAllDocsQuery
import org.apache.lucene.search.TermQuery

import static com.dojo.lucene.examples.addresses.AddressAnalyzer.*

class Reader_1_Spec extends ReaderSpecBase {

    def "should retrieve all addresses"() {
        given: 'Indexed addresses'
        def addresses = Lists.newArrayList(
                Address.of("BA8 0PS", "Templecombe", "17 Vale View, Henstridge, Templecombe", true),
                Address.of("CR5 1SR", "Coulsdon", "33 The Glade", true),
                Address.of("KY5 HP2", "3 Station Rd, Long Marston, Tring", true),
                Address.of("MK9 3NZ", "Milton Keynes", "500 Marlborough Gate", true)
        )
        indexer.index(addresses)

        when: 'retrieving addresses'
        def results = reader.find(new MatchAllDocsQuery(), addresses.size())

        then:
        results.size() == addresses.size()
    }

    def "should not retrieve any addresses using term with comma"() {
        given: 'Indexed addresses'
        def term = 'henstridge,'
        def addresses = Lists.newArrayList(
                Address.of("BA8 0PS", "17 Vale View, $term Templecombe", true),
                Address.of("CR5 1SR", "Coulsdon", "33 The Glade", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        def results = reader.find(new TermQuery(new Term(AddressMapper.ADDRESS_LINE_FIELD, term)))

        then:
        results.size() == 0
    }

    def "should retrieve address using term without comma"() {
        given: 'Indexed addresses'
        def term = 'henstridge'
        def addresses = Lists.newArrayList(
                Address.of("BA8 0PS", "17 Vale View, $term, Templecombe", true),
                Address.of("CR5 1SR", "Coulsdon", "33 The Glade", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        def results = reader.find(new TermQuery(new Term(AddressMapper.ADDRESS_LINE_FIELD, term)))

        then:
        results.size() == 1
    }
}
