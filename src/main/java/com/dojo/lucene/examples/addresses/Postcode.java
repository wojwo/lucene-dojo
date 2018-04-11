package com.dojo.lucene.examples.addresses;

import lombok.AllArgsConstructor;

@AllArgsConstructor(staticName = "of")
class Postcode {

    String value;

    @Override
    public String toString() {
        return value.trim().replaceAll(" ", "");
    }
}
