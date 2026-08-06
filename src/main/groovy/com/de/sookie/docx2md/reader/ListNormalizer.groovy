package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.Paragraph

class ListNormalizer {

    List<Block> normalize(List<Block> blocks) {
        blocks.removeAll { block ->
            block instanceof Paragraph &&
                    block.inlines.isEmpty()
        }

        return blocks
    }
}