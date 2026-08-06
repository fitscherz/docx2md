package com.de.sookie.docx2md.renderer.inline

import com.de.sookie.docx2md.model.inline.Text

class TextRenderer implements InlineRenderer<Text> {

    @Override
    Class<Text> supports() {
        return Text
    }

    @Override
    void render(StringBuilder md, Text text, boolean codeBlock) {
        String value = text.value

        if (!codeBlock && text.code) {
            md.append("`")
            md.append(value)
            md.append("`")
            return
        }

        if (text.bold) {
            value = "**${value}**"
        }

        if (text.italic) {
            value = "*${value}*"
        }

        if (text.underline) {
            value = "<u>${value}</u>"
        }

        if (text.strike) {
            value = "~~${value}~~"
        }

        md.append(value)
    }
}