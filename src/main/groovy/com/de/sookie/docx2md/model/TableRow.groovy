package com.de.sookie.docx2md.model

class TableRow {

    final List<TableCell> cells = []

    void add(TableCell cell) {
        if (cell != null) {
            cells.add(cell)
        }
    }
}