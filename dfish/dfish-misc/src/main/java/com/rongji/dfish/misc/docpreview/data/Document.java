package com.rongji.dfish.misc.docpreview.data;

import java.util.List;

public class Document {
    public List<DocumentElement> getBody() {
        return body;
    }

    public void setBody(List<DocumentElement> body) {
        this.body = body;
    }

    List<DocumentElement> body;
}
