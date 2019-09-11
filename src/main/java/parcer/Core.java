package parcer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import parcer.util.CloseFail;
import parcer.util.OpenFail;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Core {
    private Workbook book;
    private Sheet sheet;

    public Core(String file) throws OpenFail {
        book = openBook(file);
        sheet = openScheet();
    }

    private Workbook openBook(final String path) throws OpenFail {
        try {
            File file = new File(path);
            return (XSSFWorkbook) XSSFWorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new OpenFail("Can't open the shedule");
        }
    }



    public void close() throws CloseFail {
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CloseFail("Can't close the shedule");
        }
    }

    public Sheet openScheet() throws OpenFail {
        Sheet sheet = book.getSheetAt(0);
        if (sheet == null) throw new OpenFail("Can't open the scheet");
        return sheet;
    }

    public void findClasses() {
        Iterator<Cell> ic = sheet.getRow(2).cellIterator();
        while (ic.hasNext()){
            Cell tc = ic.next();
            if (tc.getStringCellValue().equals("Предмет")){
                int colInd = tc.getColumnIndex();
                for (int i = 3; i < 75; i++){
                    System.out.println(sheet.getRow(i).getCell(colInd));
                }
            }
        }
    }


}
