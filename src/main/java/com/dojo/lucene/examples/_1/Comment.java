package com.dojo.lucene.examples._1;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
class Comment {
    String userName;
    String comment;
}
