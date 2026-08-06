package com.de.sookie.docx2md.model

class CodeBlock extends InlineBlock {

    String language = "text"

    String listId
    int listLevel = 0
    ParagraphType type = ParagraphType.NORMAL
}