package com.dojo.lucene.examples.comments;

import lombok.AllArgsConstructor;
import lombok.Value;

@AllArgsConstructor(staticName = "of")
@Value
class Comment {
    String userName;
    String comment;
}
