package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.Block

class BlockRendererService {

    private final List<BlockRenderer<?>> renderers

    BlockRendererService(List<BlockRenderer<?>> renderers) {
        this.renderers = renderers
    }

    void render(StringBuilder md, Block block) {
        if (block == null) {
            return
        }

        BlockRenderer renderer = renderers.find {
            it.supports().isAssignableFrom(block.class)
        }

        renderer?.render(md, block)
    }
}