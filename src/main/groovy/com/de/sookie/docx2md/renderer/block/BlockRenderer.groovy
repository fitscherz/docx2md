package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.Block

interface BlockRenderer<T extends Block> {

    Class<T> supports()

    void render(StringBuilder md, T block)
}