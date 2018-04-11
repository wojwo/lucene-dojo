package com.dojo.lucene.examples._1;

import com.dojo.lucene.common.ObjectMapper;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

class CommentMapper implements ObjectMapper<Comment> {
    public static final String USERNAME_FIELD = "user_name";
    public static final String COMMENT_FIELD = "comment";

    public Document toDocument(Comment comment) {
        Document document = new Document();
        document.add(new StringField(USERNAME_FIELD, comment.getUserName(), Field.Store.YES));
        document.add(new TextField(COMMENT_FIELD, comment.getComment(), Field.Store.YES));
        return document;
    }

    public Comment from(Document document) {
        return Comment.of(
                document.get(USERNAME_FIELD),
                document.get(COMMENT_FIELD)
        );
    }
}
