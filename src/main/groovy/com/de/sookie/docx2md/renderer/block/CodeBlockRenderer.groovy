package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text
import com.de.sookie.docx2md.renderer.inline.InlineRendererService

class CodeBlockRenderer implements BlockRenderer<CodeBlock> {

    private final InlineRendererService inlineRendererService

    CodeBlockRenderer(InlineRendererService inlineRendererService) {
        this.inlineRendererService = inlineRendererService
    }

    @Override
    Class<CodeBlock> supports() {
        return CodeBlock
    }

    @Override
    void render(StringBuilder md, CodeBlock block) {
        md.append("```")
        md.append(block.language ?: "text")
        md.append("\n")

        block.inlines.each { inline ->
            if (inline instanceof LineBreak) {
                md.append("\n")
            } else {
                if (inline instanceof Text && inline.code) {
                    md.append(inline.value)
                } else {
                    inlineRendererService.render(md, inline)
                }
            }
        }

        md.append("\n```\n\n")
    }
}