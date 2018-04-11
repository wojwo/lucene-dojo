package com.dojo.lucene;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
class Address {

    public static final String POSTCODE_FIELD = "postcode";
    public static final String ADDRESS_LINE_FIELD = "address_line";
    public static final String IS_ACTIVE_FIELD = "is_active";

    Postcode postcode;
    String addressLine;
    Boolean isActive;
}
