package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.inline.RunStyle
import com.de.sookie.docx2md.model.inline.TabStop
import org.docx4j.wml.R.Tab

class TabReader implements RunContentReader<Tab> {

    @Override
    Class<Tab> supports() {
        return Tab
    }

    @Override
    void read(Tab tab, InlineBlock block, RunStyle style) {
        block.add(new TabStop())
    }
}