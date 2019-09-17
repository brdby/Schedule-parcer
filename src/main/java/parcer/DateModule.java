package parcer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateModule {
    private Calendar point;
    private final int MAXWEEK = 17;

    public DateModule(){
        LocalDate d = LocalDate.now();
        getFirstDay(d);
    }

    public HashSet<LocalDate> getDates(String s, int weekDay){
        HashSet<LocalDate> ld = new HashSet<>();

        point.set(Calendar.DAY_OF_WEEK, weekDay);

        boolean reverse = false;
        Pattern p = Pattern.compile("[кК][рР]");
        Matcher m = p.matcher(s);

        if (m.find()) {
            reverse = true;
        }
        ArrayList<Integer> reverseWeeks = new ArrayList<>();

        p = Pattern.compile("[0-9]+");
        m = p.matcher(s);

        while (m.find()) {
            String aaa = m.group();
            int weekNum = Integer.parseInt(aaa);
            if (!reverse) {
                LocalDate localDate = LocalDateTime.ofInstant(point.toInstant(),
                        point.getTimeZone().toZoneId()).toLocalDate();
                localDate = localDate.plusWeeks(weekNum - 1);
                ld.add(localDate);
            }
            else {
                reverseWeeks.add(weekNum);
            }
        }

        if (reverse){
            for (int i = 0; i < MAXWEEK; i++){
                if (!reverseWeeks.contains(i)) {
                    LocalDate localDate = LocalDateTime.ofInstant(point.toInstant(),
                            point.getTimeZone().toZoneId()).toLocalDate();
                    localDate = localDate.plusWeeks(i);
                    ld.add(localDate);
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
}
