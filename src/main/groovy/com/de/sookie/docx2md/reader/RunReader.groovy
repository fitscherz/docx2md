package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.inline.RunStyle
import org.docx4j.XmlUtils
import org.docx4j.wml.R

class RunReader implements InlineReader<R> {

    private final RunContentReaderService runContentReaderService

    RunReader(RunContentReaderService runContentReaderService) {
        this.runContentReaderService = runContentReaderService
    }

    @Override
    Class<R> supports() {
        return R
    }

    @Override
    void read(R run, InlineBlock block) {
        RunStyle style = readStyle(run)

        run.content.each {
            runContentReaderService.read(XmlUtils.unwrap(it), block, style)
        }
    }

    private RunStyle readStyle(R run) {
        def rPr = run.rPr

        new RunStyle(
                fontFamily: rPr?.rFonts?.ascii ?: rPr?.rFonts?.hAnsi,
                bold: rPr?.b != null,
                italic: rPr?.i != null,
                underline: rPr?.u != null,
                strike: rPr?.strike != null,
                fontSize: rPr?.sz?.val?.toInteger()
        )
    }
}