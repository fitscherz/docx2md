package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.ListItem
import com.de.sookie.docx2md.model.Paragraph
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
                if (firstParagraph) {
                    md.append(indent)
                    md.append("- ")
                    firstParagraph = false
                } else {
                    md.append(indent)
                    md.append("  ")
                }

                child.inlines.each { inline ->
                    inlineRendererService.render(md, inline)
                }

                md.append("\n")
                return
            }

            if (child instanceof ListItem) {
                md.append("\n")
                renderItem(md, child, depth + 1)
                return
            }

            // CodeBlock oder andere Blocktypen innerhalb eines Listenpunktes
            md.append("\n")

            StringBuilder blockMd = new StringBuilder()
            blockRendererService.render(blockMd, child)

            blockMd.eachLine { line ->
                md.append(indent)
                md.append("    ")
                md.append(line)
                md.append("\n")
            }

            md.append("\n")
        }

        if (depth == 0) {
            md.append("\n")
        }
    }
}