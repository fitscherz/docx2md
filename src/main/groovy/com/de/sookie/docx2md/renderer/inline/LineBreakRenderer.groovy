package com.de.sookie.docx2md.renderer.inline

import com.de.sookie.docx2md.model.inline.LineBreak

class LineBreakRenderer implements InlineRenderer<LineBreak> {

    @Override
    Class<LineBreak> supports() {
        return LineBreak
    }

    @Override
    void render(StringBuilder md, LineBreak breakLine, boolean codeBlock) {
        md.append('\n')
    }
}