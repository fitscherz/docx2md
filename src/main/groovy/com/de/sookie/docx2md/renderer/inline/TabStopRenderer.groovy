package com.de.sookie.docx2md.renderer.inline

import com.de.sookie.docx2md.model.inline.TabStop

class TabStopRenderer implements InlineRenderer<TabStop> {

    @Override
    Class<TabStop> supports() {
        return TabStop
    }

    @Override
    void render(StringBuilder md, TabStop tab, boolean codeBlock) {
        md.append('\t')
    }
}