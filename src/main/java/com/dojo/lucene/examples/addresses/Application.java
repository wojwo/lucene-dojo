package com.dojo.lucene.examples.addresses;

import com.dojo.lucene.common.BaseIndexer;
import com.dojo.lucene.common.Indexer;
import com.google.common.collect.Lists;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

class Application {

    private static final String INDEX_DIR = "/Users/Wojtek/Documents/dojo/addresses";

    public static void main(String[] args) throws IOException {
        Directory directory = FSDirectory.open(Paths.get(INDEX_DIR));

        Indexer<Address> indexer = new BaseIndexer<>(
                directory,
                new AddressAnalyzer(),
                new AddressMapper()
        );

        List<Address> comments = Lists.newArrayList(
                Address.of("BA8 0PS", "Templecombe", "17 Vale View, Henstridge, Templecombe", true),
                Address.of("CR5 1SR", "Coulsdon", "33 The Glade", true),
                Address.of("KY5 0JJ", "79 Derran Dr, Cardenden, Lochgelly", false),
                Address.of("KY5 HP2", "3 Station Rd, Long Marston, Tring", true),
                Address.of("MK9 3NZ", "Milton Keynes", "500 Marlborough Gate", true)
        );

        indexer.index(comments);
    }
}
