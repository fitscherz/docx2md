package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.ListItem
import com.de.sookie.docx2md.model.Paragraph
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.inline.LineBreak
import com.de.sookie.docx2md.renderer.inline.InlineRendererService

class ListItemRenderer implements BlockRenderer<ListItem> {

    private final BlockRendererService blockRendererService
    private final InlineRendererService inlineRendererService

    ListItemRenderer(
            BlockRendererService blockRendererService,
            InlineRendererService inlineRendererService
    ) {
        this.blockRendererService = blockRendererService
        this.inlineRendererService = inlineRendererService
    }

    @Override
    Class<ListItem> supports() {
        return ListItem
    }

    @Override
    void render(StringBuilder md, ListItem item) {
        renderItem(md, item, 0)
    }

    private void renderItem(StringBuilder md, ListItem item, int depth) {
        String indent = "    " * depth
        boolean firstParagraph = true

        item.children.each { child ->

            if (child instanceof Paragraph) {
                renderParagraph(md, child, indent, firstParagraph)
                firstParagraph = false
                return
            }

            if (child instanceof CodeBlock) {
                renderCodeBlock(md, child, indent)
                return
            }

            if (child instanceof ListItem) {
                md.append("\n")
                renderItem(md, child, depth + 1)
                return
            }

            renderBlock(md, child, indent)
        }

        if (depth == 0) {
            md.append("\n")
        }
    }

    private void renderParagraph(
            StringBuilder md,
            Paragraph paragraph,
            String indent,
            boolean first
    ) {
        if (first) {
            md.append(indent)
            md.append("- ")
        } else {
            md.append("\n")
            md.append(indent)
            md.append("  ")
            md.append("\n")
            md.append(indent)
            md.append("  ")
        }

        boolean afterLineBreak = false

        paragraph.inlines.each { inline ->
            if (inline instanceof LineBreak) {
                md.append("\n")
                md.append("\n")
                md.append(indent)
                md.append("  ")
                afterLineBreak = true
            } else {
                inlineRendererService.render(md, inline)
            }
        }

        md.append("\n")
    }

    private void renderCodeBlock(
            StringBuilder md,
            CodeBlock block,
            String indent
    ) {
        md.append("\n")

        StringBuilder blockMd = new StringBuilder()
        blockRendererService.render(blockMd, block)

        blockMd.eachLine { line ->
            md.append(indent)
            md.append("  ")
            md.append(line)
            md.append("\n")
        }
    }

    private void renderBlock(
            StringBuilder md,
            Object block,
            String indent
    ) {
        StringBuilder blockMd = new StringBuilder()
        blockRendererService.render(blockMd, block)

        blockMd.eachLine { line ->
            md.append(indent)
            md.append("  ")
            md.append(line)
            md.append("\n")
        }
    }
}