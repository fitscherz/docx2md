package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.RunStyle
import org.docx4j.wml.Br

class BreakReader implements RunContentReader<Br> {

    @Override
    Class<Br> supports() {
        return Br
    }

    @Override
    void read(Br br, InlineBlock block, RunStyle style) {
        block.add(new LineBreak())
    }
}