package com.de.sookie.docx2md.model

import com.de.sookie.docx2md.model.inline.Inline

class Heading extends InlineBlock {

    int level
    List<Inline> inlines = []

    void add(Inline inline) {
        inlines.add(inline)
    }
}