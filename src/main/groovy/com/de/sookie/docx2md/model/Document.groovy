package com.de.sookie.docx2md.model

class Document {

    final List<Block> blocks = []

    void add(Block block) {
        if (block != null) {
            blocks.add(block)
        }
    }
}
