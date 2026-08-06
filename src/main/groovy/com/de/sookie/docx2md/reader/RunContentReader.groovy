package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.InlineBlock
import com.de.sookie.docx2md.model.inline.RunStyle

interface RunContentReader<T> {

    Class<T> supports()

    void read(T value, InlineBlock block, RunStyle style)
}