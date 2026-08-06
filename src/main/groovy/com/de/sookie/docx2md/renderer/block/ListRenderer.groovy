package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.renderer.inline.InlineRendererService

class ListRenderer implements BlockRenderer<Paragraph> {

    private final InlineRendererService inlineRendererService

    ListRenderer(InlineRendererService inlineRendererService) {
        this.inlineRendererService = inlineRendererService
    }

    @Override
    Class<Paragraph> supports() {
        return Paragraph
    }

    @Override
    void render(StringBuilder md, Paragraph paragraph) {
        if (!paragraph.listId) {
            return
        }

        String indent = "    " * paragraph.listLevel

        md.append(indent)
        md.append("- ")

        paragraph.inlines.each {
            inlineRendererService.render(md, it)
        }

        md.append("\n")
    }
}