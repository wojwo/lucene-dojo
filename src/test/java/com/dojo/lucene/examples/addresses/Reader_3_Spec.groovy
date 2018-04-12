package com.dojo.lucene.examples.addresses

import com.google.common.collect.Lists
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.Query

import static com.dojo.lucene.examples.addresses.AddressMapper.*

class Reader_3_Spec extends ReaderSpecBase {

    def "should retrieve all addresses by postTown using MultiFieldQueryParser"() {
        given: 'Indexed addresses'
        def addresses = Lists.newArrayList(
                Address.of("CR5 1SR", "Coulsdon", "33 The glade", true),
                Address.of("CR5 1SR", "Dukes House, 202 Maidstone Rd, Coulsdon", true),
                Address.of("BS23 3TP", "Warne Park, 6 Warne Rd, Weston-super-Mare", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        QueryParser queryParser = new MultiFieldQueryParser([ADDRESS_LINE_FIELD, POSTTOWN_FIELD] as String[], analyzer)
        Query query = queryParser.parse("Coulsdon")

        def results = reader.find(query)

        then:
        results.size() == 2
    }

    def "should retrieve addresses by postTown and isActive"() {
        given: 'Indexed addresses'
        def postTown = 'Coulsdon'
        def isActive = false

        def addresses = Lists.newArrayList(
                Address.of("CR5 1SR", "Coulsdon", "33 The glade", !isActive),
                Address.of("AZ5 3CA", postTown,"Dukes House, 202 Maidstone Rd, Coulsdon", isActive),
                Address.of("BS23 3TP", "Weston-super-Mare","Warne Park, 6 Warne Rd, Weston-super-Mare", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        QueryParser queryParser = new MultiFieldQueryParser([IS_ACTIVE_FIELD, POSTTOWN_FIELD] as String[], analyzer)
        Query query = queryParser.parse("$IS_ACTIVE_FIELD:$isActive AND $POSTTOWN_FIELD:$postTown")
        def results = reader.find(query)

        then:
        results.size() == 1
        results.get(0).getIsActive() == isActive
        results.get(0).getPostTown().isPresent()
        results.get(0).getPostTown().get() == postTown
    }

    def "should retrieve addresses by postcode prefix"() {
        given: 'Indexed addresses'
        def postcodePrefix = 'AZ5'
        def addresses = Lists.newArrayList(
                Address.of("${postcodePrefix}1SR", "Coulsdon", "33 The glade", true),
                Address.of("${postcodePrefix}41A", "Dukes House, 202 Maidstone Rd, Coulsdon", true),
                Address.of("BS23 3TP", "Weston-super-Mare","Warne Park, 6 Warne Rd, Weston-super-Mare", true)
        )
        indexer.index(addresses)

        when: 'retrieving address'
        QueryParser queryParser = new QueryParser(POSTCODE_FIELD, analyzer)
        Query query = queryParser.parse("${postcodePrefix}*")
        def results = reader.find(query)

        then:
        results.size() == 2
    }
}
