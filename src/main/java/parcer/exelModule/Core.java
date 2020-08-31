package parcer.exelModule;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;
import parcer.exelModule.entity.Group;
import parcer.exelModule.entity.Study;
import parcer.exelModule.exeptions.parsingExeption;

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

    private final Map<String, Integer> WEEKDAYS = Map.of("ПОНЕДЕЛЬНИК", 1, "ВТОРНИК", 2,
            "СРЕДА", 3, "ЧЕТВЕРГ", 4, "ПЯТНИЦА", 5, "СУББОТА",6, "ВОСКРЕСЕНЬЕ", 7);

    public Core(String file) throws parsingExeption {
        book = openBook(file);
        sheet = openScheet();
        groups = new HashSet<>();
    }

    private Workbook openBook(final String path) throws parsingExeption {
        try {
            File file = new File(path);
            return XSSFWorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new parsingExeption("Can't open the shedule");
        }
    }



    public void close() throws parsingExeption {
        try {
            book.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new parsingExeption("Can't close the shedule");
        }
    }

    private Sheet openScheet() throws parsingExeption {
        Sheet sheet = book.getSheetAt(0);
        if (sheet == null) throw new parsingExeption("Can't open the scheet");
        return sheet;
    }

    public void findStudies() throws parsingExeption {
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
                if (!gm.find()) throw new parsingExeption("Не нашли имя группы");
                Group group = new Group(gm.group());

                //Ищем предметы этой группы
                for (int i = 3; i < 75; i++){
                    Cell studyNameCell = sheet.getRow(i).getCell(colInd);
                    if (studyNameCell == null) continue;

                    String studyName =  studyNameCell.getStringCellValue();
                    if (studyName.equals("")) continue; //Нет предмета

                    studyName = studyName.replaceAll("\n", ""); //Убираем все переносы строки
                    studyName = studyName.replaceAll("([^а-яА-Я]нед[^а-яА-Я])|([^а-яА-Я]нед$)|(^нед[^а-яА-Я])", " "); //костыль
                    studyName = studyName.replaceAll("([^а-яА-Я]н[^а-яА-Я])|([^а-яА-Я]н$)|(^н[^а-яА-Я])", " "); //костыль
                    String StRegex  = "(([крКР. ]*[0-9][0-9,. ]+[нН]*[ ,]*[^0-9]+)|([^0-9]+[крКР. ]*[0-9][0-9,. ]+[нН]*[ ,]*))|([а-яА-Я][^0-9]+)"; //Разделяем предметы
                    Pattern sp = Pattern.compile(StRegex);
                    Matcher sm = sp.matcher(studyName);

                    //Когда разные предметы в одной ячейке
                    while (sm.find()){
                        //Оделяем недели от названия предмета
                        String found = sm.group();

                        Study study = new Study(found);

                        //Ищем название предмета
                        String name = searchName(found);
                        if (name == null) {
                            System.out.println("Skipped " + studyName);
                            continue;
                        }
                        else study.setName(name);

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
                    }
                }
                groups.add(group);
            }
        }
        System.out.println("Расписание прочитано");
    }

    private String searchName(String s){
        String nameRegex = "[^0-9,. ][^0-9]+[^0-9, ]";
        Pattern p = Pattern.compile(nameRegex);
        Matcher m = p.matcher(s);
        if (!m.find()) return null;
        return m.group();
    }

    private boolean searchEven(String s) throws parsingExeption {
        if (s.equals("I")) return false;
        if (s.equals("II")) return true;
        else throw new parsingExeption("Can't find even pointer");
    }

    private String searchDateID(String s){
        String reg  = "([кК][рР][. ]*[0-9][0-9, ]+[ ,]+)|([0-9][0-9, ]+[ ,]+)";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(s);
        if (!m.find()) return null;
        return m.group();
    }

    private int getWeekDay(String s) throws parsingExeption {
        Integer ans = WEEKDAYS.get(s);
        if (s == null || s.equals("") || ans == null) {
            throw new parsingExeption("Can't find week pointer");
        }
        return ans;
    }

    public HashSet<Group> getGroups(){
        return groups;
    }
}
