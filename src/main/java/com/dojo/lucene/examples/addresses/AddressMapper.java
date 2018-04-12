package com.dojo.lucene.examples.addresses;

import com.dojo.lucene.common.ObjectMapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

class AddressMapper implements ObjectMapper<Address> {
    public static final String POSTCODE_FIELD = "postcode";
    public static final String POSTTOWN_FIELD = "posttown";
    public static final String ADDRESS_LINE_FIELD = "address_line";
    public static final String IS_ACTIVE_FIELD = "is_active";


    @Override
    public Document toDocument(Address address) {
        // TODO: Implement me
        throw new UnsupportedOperationException("Implement me");
    }

    @Override
    public Address from(Document document) {
        return Address.of(
                document.get(POSTCODE_FIELD),
                document.get(POSTTOWN_FIELD),
                document.get(ADDRESS_LINE_FIELD),
                Boolean.valueOf(document.get(IS_ACTIVE_FIELD))
        );
    }
}
