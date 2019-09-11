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
import java.lang.reflect.GenericArrayType;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Core {
    private Workbook book;
    private Sheet sheet;
    private HashSet<Group> groups;

    public Core(String file) throws OpenFail {
        book = openBook(file);
        sheet = openScheet();
        groups = new HashSet<>();
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

    public void findStudies() {
        //Ищем колонки с предметами
        Iterator<Cell> ic = sheet.getRow(2).cellIterator();
        while (ic.hasNext()){
            Cell tc = ic.next();
            if (tc.getStringCellValue().equals("Предмет")){

                //Получаем имя группы и создаем ее объект
                int colInd = tc.getColumnIndex();
                String GrRegex = "[А-Я]{4}-[0-9]{2}-[0-9]{2}";
                Pattern gp = Pattern.compile(GrRegex);
                Matcher gm = gp.matcher(sheet.getRow(1).getCell(colInd).getStringCellValue());
                if (!gm.find()) break; //Не нашли имя группы
                Group group = new Group(gm.group());

                //Ищем предметы этой группы
                for (int i = 3; i < 75; i++){
                    String studyName =  sheet.getRow(i).getCell(colInd).getStringCellValue();

                    if (studyName.equals("")) continue; //Нет предмета

                    studyName = studyName.replaceAll("\n", ""); //Убираем все переносы строки
                    String StRegex  = "[,0-9]*[^0-9]+[а-я]"; //Разделяем предметы
                    //String StRegex  = ".*";
                    Pattern sp = Pattern.compile(StRegex);
                    Matcher sm = sp.matcher(studyName);
                    while (sm.find()){
                        Study study = new Study(studyName);
                        group.addStudy(study);
                    }
                }
                groups.add(group);
            }
        }
    }

    public void allStudies(){
        groups.forEach(group -> {
            System.out.println(group.toString() + "\n");
            System.out.println("---------------------------------\n");
        });
    }


}
