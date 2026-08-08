package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.model.inline.Text
import com.de.sookie.docx2md.model.inline.InlineCode
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
                return
            }

            if (inline instanceof Text) {
                md.append(inline.value)
                return
            }

            if (inline instanceof InlineCode) {
                inline.inlines.each { codeInline ->
                    if (codeInline instanceof Text) {
                        md.append(codeInline.value)
                    } else if (codeInline instanceof LineBreak) {
                        md.append("\n")
                    }
                }
                return
            }

            inlineRendererService.render(md, inline)
        }

        md.append("\n```\n\n")
    }
}