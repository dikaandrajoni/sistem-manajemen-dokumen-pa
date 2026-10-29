package com.proyekakhir.manajemendokumen.core.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public abstract class ExcelWriter<E> {
    protected List<E> entities;
    public ExcelWriter(List<E> entities){
        this.entities = entities;
    }

    public ExcelWriter() {
    }

    public abstract List<String> getHeaders();

    public abstract void getRows(Row row, E entity);

    public OutputStream export(OutputStream outputStream) {
        Row header;
        CellStyle headerStyle;
        XSSFFont font;
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet(this.getClass().getSimpleName());
            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 4000);

            header = sheet.createRow(0);

            headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            int i = 0;
            for(String headerTitle : this.getHeaders()) {
                Cell headerCell = header.createCell(i++);
                headerCell.setCellValue(headerTitle);
                headerCell.setCellStyle(headerStyle);
            }
            CellStyle style = workbook.createCellStyle();
            style.setWrapText(true);

            int j = 0;
            for(E entity : this.entities) {
                Row row = sheet.createRow(2 + j++);
                this.getRows(row, entity);
            }

            workbook.write(outputStream);
            return outputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
