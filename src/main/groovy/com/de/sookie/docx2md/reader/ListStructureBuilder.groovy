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
                ListItem item = createItem(block)

                while (!stack.isEmpty() &&
                        stack.last().level >= item.level) {
                    stack.remove(stack.size() - 1)
                }

                if (stack.isEmpty()) {
                    result << item
                } else {
                    stack.last().add(item)
                }

                stack << item
                return
            }

            if (block instanceof CodeBlock && block.listId) {
                if (!stack.isEmpty()) {
                    stack.last().add(block)
                } else {
                    result << block
                }
                return
            }

            stack.clear()
            result << block
        }

        return result
    }

    private ListItem createItem(Paragraph paragraph) {
        ListItem item = new ListItem(
                listId: paragraph.listId,
                level: paragraph.listLevel
        )

        item.add(paragraph)
        return item
    }
}