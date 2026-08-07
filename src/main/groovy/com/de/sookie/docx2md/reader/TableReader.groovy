package com.de.sookie.docx2md.reader

import com.de.sookie.docx2md.model.Block
import com.de.sookie.docx2md.model.Table
import com.de.sookie.docx2md.model.TableCell
import com.de.sookie.docx2md.model.TableRow
import org.docx4j.XmlUtils
import org.docx4j.wml.Tbl
import org.docx4j.wml.Tc
import org.docx4j.wml.Tr

class TableReader implements BlockReader<Tbl> {

    private final BlockReaderService blockReaderService

    TableReader(BlockReaderService blockReaderService) {
        this.blockReaderService = blockReaderService
    }

    @Override
    Class<Tbl> supports() {
        return Tbl
    }

    @Override
    List<Block> read(Tbl table) {
        Table result = new Table()

        table.content.each {
            Tr row = XmlUtils.unwrap(it) as Tr

            if (row != null) {
                result.add(readRow(row))
            }
        }

        return [result]
    }

    private TableRow readRow(Tr row) {
        TableRow result = new TableRow()

        row.content.each {
            Tc cell = XmlUtils.unwrap(it) as Tc

            if (cell != null) {
                result.add(readCell(cell))
            }
        }

        return result
    }

    private TableCell readCell(Tc cell) {
        TableCell result = new TableCell()

        cell.content.each {
            List<Block> blocks = blockReaderService.read(XmlUtils.unwrap(it))

            blocks.each { block ->
                result.add(block)
            }
        }

        return result
    }
}