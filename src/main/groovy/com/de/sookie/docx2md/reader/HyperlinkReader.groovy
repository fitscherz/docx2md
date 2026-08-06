package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock
import org.docx4j.XmlUtils
import org.docx4j.wml.P.Hyperlink

class HyperlinkReader implements InlineReader<Hyperlink> {

    private final RunContentReaderService runContentReaderService

    HyperlinkReader(RunContentReaderService runContentReaderService) {
        this.runContentReaderService = runContentReaderService
    }

    @Override
    Class<Hyperlink> supports() {
        return Hyperlink
    }

    @Override
    void read(Hyperlink hyperlink, InlineBlock block) {
        hyperlink.content.each {
            runContentReaderService.read(XmlUtils.unwrap(it), block)
        }
    }
}