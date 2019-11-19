package parcer.exelModule.util;

import java.util.Calendar;

public class DateModule {
    private Calendar point;
    private final int MAXWEEK = 17;

    /*
    public DateModule(){
        LocalDate d = LocalDate.now();
        getFirstDay(d);
    }


    public HashSet<Calendar> getDates(int weekDay, boolean even){
        //Хэшсет с датами этого предмета
        HashSet<Calendar> ld = new HashSet<>();

        //Для указателя ставим нужный день недели
        point.set(Calendar.DAY_OF_WEEK, weekDay);

        //Нужны четные или нечетные недели
        int ev = 0;
        if (even) ev ++;
        for (int i = ev; i < MAXWEEK; i = i+2){
            Calendar studyDay = (GregorianCalendar) point.clone();
            studyDay.add(Calendar.DATE, i*7);
            ld.add(studyDay);
        }

        return ld;
    }

    public HashSet<Calendar> getDates(String s, int weekDay, boolean even){
        //Хэшсет с датами этого предмета
        HashSet<Calendar> ld = new HashSet<>();

        //Для указателя ставим нужный день недели
        point.set(Calendar.DAY_OF_WEEK, weekDay);

        //Провряем на "кроме"
        boolean reverse = false;
        Pattern p = Pattern.compile("[кК][рР]");
        Matcher m = p.matcher(s);
        if (m.find()) {
            reverse = true;
        }
        ArrayList<Integer> reverseWeeks = new ArrayList<>();

        //Ищем номера нужных недель
        p = Pattern.compile("[0-9]+");
        m = p.matcher(s);

        //Если нашли, добавляем нужную дату в сет
        while (m.find()) {
            String aaa = m.group();
            int weekNum = Integer.parseInt(aaa);
            if (!reverse) {
                Calendar studyDay = (GregorianCalendar) point.clone();
                studyDay.add(Calendar.DATE, 7*(weekNum-1));
                ld.add(studyDay);
            }
            else {
                //Если "кр" то сохраняем номера этих недель для будущего их исключения
                reverseWeeks.add(weekNum);
            }
        }

        //Если "кр" добавляем все даты кроме исключенных
        if (reverse){
            //Нужны четные или нечетные недели
            int ev = 0;
            if (even) ev ++;
            for (int i = ev; i < MAXWEEK; i = i+2){
                if (!reverseWeeks.contains(i)) {
                    Calendar studyDay = (GregorianCalendar) point.clone();
                    studyDay.add(Calendar.DATE, i*7);
                    ld.add(studyDay);
                }
            }
        }

        return ld;
    }

    private void getFirstDay(LocalDate date){
        if (date.getMonth().getValue() > 7) {
            point = new GregorianCalendar(date.getYear(), Calendar.SEPTEMBER, 1);
            if (point.get(Calendar.DAY_OF_WEEK) == 1){
                point = new GregorianCalendar( date.getYear(), Calendar.SEPTEMBER, 2);
            }
        }
    }
    */
}
