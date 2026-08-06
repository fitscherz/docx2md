package com.de.sookie.docx2md.renderer.inline

interface InlineRenderer<T> {

    Class<T> supports()

    void render(StringBuilder md, T value, boolean codeBlock)
}