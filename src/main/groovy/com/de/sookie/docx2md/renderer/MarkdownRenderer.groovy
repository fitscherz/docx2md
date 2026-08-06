package com.de.sookie.docx2md.renderer

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.Document
import com.de.sookie.docx2md.renderer.block.BlockRendererService

class MarkdownRenderer {

    private final BlockRendererService blockRendererService

    MarkdownRenderer(BlockRendererService blockRendererService) {
        this.blockRendererService = blockRendererService
    }

    String render(Document document) {
        StringBuilder md = new StringBuilder()

        //println "BLOCKS=${document.blocks.size()}"

        document.blocks.each { block ->
            //printBlock(block, 0)

            blockRendererService.render(md, block)
        }

        return md.toString().replaceAll(/\n{3,}/, "\n\n")
    }

    private void printBlock(Block block, int indent) {
        println("${"  " * indent}${block.class.simpleName}")

        if (block instanceof com.de.sookie.docx2md.model.ListItem) {
            block.children.each { child ->
                printBlock(child, indent + 1)
            }
        }
    }
}