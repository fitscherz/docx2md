package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.inline.RunStyle
import org.docx4j.XmlUtils

class RunContentReaderService {

    private final List<RunContentReader<?>> readers

    RunContentReaderService(List<RunContentReader<?>> readers) {
        this.readers = readers
    }

    void read(Object content, def target, RunStyle style) {
        def value = XmlUtils.unwrap(content)

        RunContentReader<?> reader = readers.find {
            it.supports().isAssignableFrom(value.class)
        }

        reader?.read(value, target, style)
    }
}