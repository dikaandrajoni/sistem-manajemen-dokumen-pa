package com.proyekakhir.manajemendokumen.core.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public abstract class ExcelReader<E> {
    public List<E> read(InputStream file) throws IOException, InvalidFormatException {
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);
        List<E> result = new ArrayList<>();
        int i = 0;
        for(Row row : sheet){
            if(i>this.skippedRow()){
                ArrayList<Object> columns = new ArrayList<>();
                for(Cell cell : row){
                    switch (cell.getCellType().name()){
                        case "NUMERIC":
                            columns.add(cell.getNumericCellValue());
                            break;
                        case "BOOLEAN":
                            columns.add(cell.getBooleanCellValue());
                            break;
                        default:
                            columns.add(cell.getStringCellValue());
                            break;
                    }

                }
                result.add(this.perLine(columns.toArray()));
            }
            i++;
        }
        return result;
    }
    public  abstract int skippedRow();
    public abstract E perLine(Object[] columns);
}
