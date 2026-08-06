package com.de.sookie.docx2md.model

import com.de.sookie.docx2md.model.inline.Inline

class Paragraph extends InlineBlock {

    ParagraphType type = ParagraphType.NORMAL
    int listLevel = 0
    String listId

}