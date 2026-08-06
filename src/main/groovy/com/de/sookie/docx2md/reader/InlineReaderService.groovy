package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock

class InlineReaderService {

    private final List<InlineReader<?>> readers

    InlineReaderService(List<InlineReader<?>> readers) {
        this.readers = readers
    }

    void read(Object object, InlineBlock block) {
        if (object == null) {
            return
        }

        InlineReader reader = readers.find {
            it.supports().isAssignableFrom(object.class)
        }

        reader?.read(object, block)
    }
}