package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.inline.RunStyle
import com.de.sookie.docx2md.model.inline.Text
import org.docx4j.wml.Text as DocxText

class TextReader implements RunContentReader<DocxText> {

    @Override
    Class<DocxText> supports() {
        return DocxText
    }

    @Override
    void read(DocxText text, InlineBlock block, RunStyle style) {
        block.add(new Text(
                value: text.value,
                fontFamily: style.fontFamily,
                fontSize: style.fontSize,
                bold: style.bold,
                italic: style.italic,
                underline: style.underline,
                strike: style.strike
        ))
    }
}