package com.de.sookie.docx2md.renderer.block

import com.de.sookie.docx2md.model.Table
import com.de.sookie.docx2md.model.TableCell
import com.de.sookie.docx2md.model.TableRow

class TableRenderer implements BlockRenderer<Table> {

    private final BlockRendererService blockRendererService

    TableRenderer(BlockRendererService blockRendererService) {
        this.blockRendererService = blockRendererService
    }

    @Override
    Class<Table> supports() {
        return Table
    }

    @Override
    void render(StringBuilder md, Table table) {
        if (table.rows.isEmpty()) {
            return
        }

        renderRow(md, table.rows.first())

        md.append('\n|')

        table.rows.first().cells.each {
            md.append(' --- |')
        }

        table.rows.drop(1).each {
            md.append('\n')
            renderRow(md, it)
        }
    }

    private void renderRow(StringBuilder md, TableRow row) {
        md.append('|')

        row.cells.each {
            md.append(' ')
            renderCell(md, it)
            md.append(' ')
            md.append('|')
        }
    }

    private void renderCell(StringBuilder md, TableCell cell) {
        StringBuilder content = new StringBuilder()

        cell.blocks.eachWithIndex { block, index ->
            if (index > 0) {
                content.append("<br>")
            }

            blockRendererService.render(content, block)
        }

        md.append(
                content.toString()
                        .replace('|', '\\|')
                        .trim()
        )
    }
}