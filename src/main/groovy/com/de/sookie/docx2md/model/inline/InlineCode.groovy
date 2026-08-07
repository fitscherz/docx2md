package com.de.sookie.docx2md.model.inline

class InlineCode extends Inline {

    List<Inline> inlines = []

    void add(Inline inline) {
        inlines << inline
    }

    void addAll(Collection<Inline> values) {
        inlines.addAll(values)
    }
}