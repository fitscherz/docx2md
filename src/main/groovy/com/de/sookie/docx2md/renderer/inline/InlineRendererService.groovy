package com.de.sookie.docx2md.renderer.inline

import com.de.sookie.docx2md.model.inline.Inline

class InlineRendererService {

    final List<InlineRenderer<?>> renderers

    InlineRendererService(List<InlineRenderer<?>> renderers) {
        this.renderers = renderers
    }

    void render(StringBuilder md, Inline inline) {
        render(md, inline, false)
    }

    void render(StringBuilder md, Inline inline, boolean codeBlock) {
        if (inline == null) {
            return
        }

        InlineRenderer renderer = renderers.find {
            it.supports().isAssignableFrom(inline.class)
        }

        renderer?.render(md, inline, codeBlock)
    }
}