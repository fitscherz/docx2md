package com.de.sookie.docx2md.renderer.inline

import com.de.sookie.docx2md.model.inline.InlineCode

class InlineCodeRenderer implements InlineRenderer<InlineCode> {

    private final InlineRendererService inlineRendererService

    InlineCodeRenderer(InlineRendererService inlineRendererService) {
        this.inlineRendererService = inlineRendererService
    }

    @Override
    Class<InlineCode> supports() {
        return InlineCode
    }

    @Override
    void render(StringBuilder md, InlineCode inlineCode, boolean codeBlock) {
        md.append("`")

        inlineCode.inlines.each {
            inlineRendererService.render(md, it, true)
        }

        md.append("`")
    }
}