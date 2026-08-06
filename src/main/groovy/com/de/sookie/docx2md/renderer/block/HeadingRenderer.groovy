package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.Heading
import com.de.sookie.docx2md.renderer.inline.InlineRendererService

class HeadingRenderer implements BlockRenderer<Heading> {

    private final InlineRendererService inlineRendererService

    HeadingRenderer(InlineRendererService inlineRendererService) {
        this.inlineRendererService = inlineRendererService
    }

    @Override
    Class<Heading> supports() {
        return Heading
    }

    @Override
    void render(StringBuilder md, Heading heading) {
        md.append('#' * heading.level)
        md.append(' ')

        heading.inlines.each {
            inlineRendererService.render(md, it)
        }

        md.append('\n\n')
    }
}