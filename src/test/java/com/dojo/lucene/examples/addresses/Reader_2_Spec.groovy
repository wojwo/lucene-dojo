package com.dojo.lucene.examples.addresses

import com.google.common.collect.Lists
import org.apache.lucene.index.Term
import org.apache.lucene.search.TermQuery

import static com.dojo.lucene.examples.addresses.AddressAnalyzer.customForbiddenWords

class Reader_2_Spec extends ReaderSpecBase {

    def "should retrieve address using lower case term"() {
        given: 'Indexed addresses'
        def word = 'GLADE'
        def addresses = Lists.newArrayList(
                Address.of("CR5 1SR", "Coulsdon", "33 The $word", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        def results = reader.find(new TermQuery(new Term(AddressMapper.ADDRESS_LINE_FIELD, word.toLowerCase())))

        then:
        results.size() == 1
    }

    def "should not retrieve any address using english stop word as term"() {
        given: 'Indexed addresses'
        def stopWord = 'THE'
        def addresses = Lists.newArrayList(
                Address.of("CR5 1SR", "Coulsdon", "33 $stopWord Glade", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        def results = reader.find(new TermQuery(new Term(AddressMapper.ADDRESS_LINE_FIELD, stopWord)))

        then:
        results.size() == 0
    }

    def "should not retrieve any address using one of forbidden word as term"() {
        given: 'Indexed addresses'
        Random random = new Random()
        def forbiddenWord = customForbiddenWords.get(random.nextInt(customForbiddenWords.size()))
        def addresses = Lists.newArrayList(
                Address.of("BA8 0PS", "17 Vale View, $forbiddenWord Templecombe", true),
                Address.of("CR5 1SR", "Coulsdon", "33 The Glade", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        def results = reader.find(new TermQuery(new Term(AddressMapper.ADDRESS_LINE_FIELD, forbiddenWord)))

        then:
        results.size() == 0
    }
}
