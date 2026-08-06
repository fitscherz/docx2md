package com.de.sookie.docx2md.model

class ListItem extends Block {

    String listId
    int level

    final List<Block> children = []

    void add(Block block) {
        children.add(block)
    }
}