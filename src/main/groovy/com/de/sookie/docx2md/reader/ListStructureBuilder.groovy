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
                handleParagraph(block, result, stack)
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

    private void handleParagraph(
            Paragraph paragraph,
            List<Block> result,
            List<ListItem> stack
    ) {
        if (stack.isEmpty()) {
            createListItem(paragraph, result, stack)
            return
        }

        ListItem current = stack.last()

        if (current.listId != paragraph.listId) {
            stack.clear()
            createListItem(paragraph, result, stack)
            return
        }

        if (current.level < paragraph.listLevel) {
            ListItem child = new ListItem()
            child.listId = paragraph.listId
            child.level = paragraph.listLevel
            child.add(paragraph)
            current.add(child)
            stack.add(child)
            return
        }

        if (current.level == paragraph.listLevel) {
            createListItem(paragraph, result, stack)
            return
        }

        while (!stack.isEmpty() &&
                stack.last().level >= paragraph.listLevel) {
            stack.remove(stack.size() - 1)
        }

        createListItem(paragraph, result, stack)
    }

    private void createListItem(
            Paragraph paragraph,
            List<Block> result,
            List<ListItem> stack
    ) {
        ListItem item = new ListItem()
        item.listId = paragraph.listId
        item.level = paragraph.listLevel
        item.add(paragraph)

        if (stack.isEmpty()) {
            result << item
        } else {
            stack.last().add(item)
        }

        stack.add(item)
    }
}