package com.de.sookie.docx2md

class Main {

    static void main(String[] args) {
        def reader = Docx2MdFactory.createDocumentReader()
        def renderer = Docx2MdFactory.createMarkdownRenderer()

        def document = reader.read(new File("/Users/sookie/Dokumente local/dcx_zu_md/docxTest.docx"))
        String markdown = renderer.render(document)

        new File("/Users/sookie/Develop/Projekte/Pharmlog/GroovyTests/src/main/resources/docx2md/output/document.md").text = markdown
    }

}
