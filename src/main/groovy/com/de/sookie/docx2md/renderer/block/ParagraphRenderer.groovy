package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.renderer.inline.InlineRendererService

class ParagraphRenderer implements BlockRenderer<Paragraph> {

    private final InlineRendererService inlineRendererService

    ParagraphRenderer(InlineRendererService inlineRendererService) {
        this.inlineRendererService = inlineRendererService
    }

    @Override
    Class<Paragraph> supports() {
        return Paragraph
    }

    @Override
    void render(StringBuilder md, Paragraph paragraph) {
        if (paragraph.listId) {
            renderList(md, paragraph)
            return
        }

        paragraph.inlines.each {
            inlineRendererService.render(md, it)
        }

        md.append("\n\n")
    }

    private void renderList(StringBuilder md, Paragraph paragraph) {
        String indent = "    " * paragraph.listLevel

        md.append(indent)
        md.append("- ")

        boolean first = true

        paragraph.inlines.each { inline ->
            if (inline instanceof LineBreak) {
                md.append("\n")
                md.append(indent)
                md.append("  ")
                first = false
            } else {
                inlineRendererService.render(md, inline)
            }
        }

        md.append("\n")
    }
}