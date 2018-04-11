package com.dojo.lucene.examples.addresses;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.util.Optional;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Value
class Address {
    Postcode postcode;
    String postTown;
    String addressLine;
    Boolean isActive;

    static Address of(String postcode, String postTown, String addressLine, Boolean isActive) {
        return new Address(Postcode.of(postcode), postTown, addressLine, isActive);
    }

    static Address of(String postcode, String addressLine, Boolean isActive) {
        return new Address(Postcode.of(postcode), null, addressLine, isActive);
    }

    Optional<String> getPostTown() {
        return Optional.ofNullable(postTown);
    }
}
