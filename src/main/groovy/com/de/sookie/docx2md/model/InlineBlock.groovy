package com.de.sookie.docx2md.model

import com.de.sookie.docx2md.model.inline.Inline

abstract class InlineBlock extends Block {

    final List<Inline> inlines = []
    final List<Block> children = []

    void add(Inline inline) {
        inlines.add(inline)
    }

    void addChild(Block block) {
        children.add(block)
    }
}