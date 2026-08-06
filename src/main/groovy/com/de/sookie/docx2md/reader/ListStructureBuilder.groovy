package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.CodeBlock
import com.de.sookie.docx2md.model.ListItem
import com.de.sookie.docx2md.model.Paragraph

class ListStructureBuilder {

    List<Block> build(List<Block> blocks) {
        List<Block> result = []
        List<ListItem> stack = []

        blocks.each { block ->

            if (block instanceof Paragraph && block.listId) {
                while (!stack.isEmpty() &&
                        (stack.last().listId != block.listId ||
                                stack.last().level >= block.listLevel)) {
                    stack.remove(stack.size() - 1)
                }

                ListItem item = new ListItem(
                        listId: block.listId,
                        level: block.listLevel
                )

                item.add(block)

                if (stack.isEmpty()) {
                    result.add(item)
                } else {
                    stack.last().add(item)
                }

                stack.add(item)
                return
            }

            if (block instanceof CodeBlock &&
                    block.listId &&
                    !stack.isEmpty()) {
                stack.last().add(block)
                return
            }

            if (block instanceof Paragraph &&
                    !block.listId &&
                    !stack.isEmpty()) {
                stack.last().add(block)
                return
            }

            stack.clear()
            result.add(block)
        }

        return result
    }
}