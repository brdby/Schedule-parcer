package parcer.exelModule;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import parcer.exelModule.entity.Group;
import parcer.exelModule.entity.Study;
import parcer.exelModule.exeptions.exelExeption;
import parcer.exelModule.exeptions.parcingExeption;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Core {
    private Workbook book;
    private Sheet sheet;
    private HashSet<Group> groups;
    private int counter;

    private final Map<String, Integer> WEEKDAYS = Map.of("ПОНЕДЕЛЬНИК", 2, "ВТОРНИК", 3,
            "СРЕДА", 4, "ЧЕТВЕРГ", 5, "ПЯТНИЦА", 6, "СУББОТА",7, "ВОСКРЕСЕНЬЕ", 1);

    public Core(String file) throws exelExeption {
        book = openBook(file);
        sheet = openScheet();
        groups = new HashSet<>();
    }

    private Workbook openBook(final String path) throws exelExeption {
        try {
            File file = new File(path);
            return XSSFWorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new exelExeption("Can't open the shedule");
        }
    }



    public void close() throws exelExeption {
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new exelExeption("Can't close the shedule");
        }
    }

    private Sheet openScheet() throws exelExeption {
        Sheet sheet = book.getSheetAt(0);
        if (sheet == null) throw new exelExeption("Can't open the scheet");
        return sheet;
    }

    public void findStudies() throws parcingExeption {
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
                    String StRegex  = "([крКР. ]*[0-9, ]+[нН]+[ ,]*[^0-9]+)|([а-яА-Я][^0-9]+)"; //Разделяем предметы
                    //String StRegex  = ".+";
                    Pattern sp = Pattern.compile(StRegex);
                    Matcher sm = sp.matcher(studyName);

                    //Когда разные предметы в одной ячейке
                    while (sm.find()){
                        //Оделяем недели от названия предмета
                        String found = sm.group();
                        Study study = new Study(found);

                        //Ищем название предмета
                        String nameRegex = "[^0-9 ]{3}[^0-9]+[^0-9 ]";
                        Pattern namePattern = Pattern.compile(nameRegex);
                        Matcher nameMatcher = namePattern.matcher(found);
                        if(nameMatcher.find()) study.setName(nameMatcher.group());

                        //Получаем строки с номерами недель, указателем четной (нечетной) недели и днем недели
                        String dateID = searchDateID(found);
                        String weekID = sheet.getRow(((i-3)/12)*12+3).getCell(0).getStringCellValue();
                        String evenID = sheet.getRow(i).getCell(4).getStringCellValue();

                        //Парсим день недели
                        study.setWeekDay(getWeekDay(weekID));

                        //Ставим дни недели
                        study.setDates(dateID);

                        //Ставим указатель четности недель
                        study.setEven(searchEven(evenID));

                        group.addStudy(study);
                        counter++;
                    }
                }
                groups.add(group);
            }
        }
        System.out.println("Расписание прочитано");
    }

    private boolean searchEven(String s) throws parcingExeption {
        if (s.equals("I")) return false;
        if (s.equals("II")) return true;
        else throw new parcingExeption("Can't find even pointer");
    }

    private String searchDateID(String s){
        String reg  = "[крКР. ]*[0-9, ]+[нН]+[ ,]*";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(s);
        if (!m.find()) return null;
        return m.group();
    }

    private int getWeekDay(String s) throws parcingExeption {
        Integer ans = WEEKDAYS.get(s);
        if (s == null || s.equals("") || ans == null) {
            throw new parcingExeption("Can't find week pointer");
        }
        return ans;
    }

    public HashSet<Group> getGroups(){
        return groups;
    }
}
