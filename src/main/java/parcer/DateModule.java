package parcer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateModule {
    private Calendar point = Calendar.getInstance();
    private int weekDay;

    public DateModule(int weekDay){
        this.weekDay = weekDay;
        LocalDate d = LocalDate.now();
        getFirstDay(d);
    }

    public HashSet<LocalDate> getDates(String s){
        boolean reverse = false;
        Pattern p = Pattern.compile("[кК][рР]");
        Matcher m = p.matcher(s);
        if (m.find()) reverse = true;
        p = Pattern.compile("[0-9][0-9]{0,1}");
        m = p.matcher(s);
        while (m.find()) {
            int weekNum = Integer.getInteger(m.group());
            if (!reverse) {
                LocalDate localDate = LocalDateTime.ofInstant(point.toInstant(),
                        point.getTimeZone().toZoneId()).toLocalDate();

            }
        }
        return null; //TODO
    }

    private void getFirstDay(LocalDate date){
        if (date.getMonth().getValue() > 7) {
            point.set(date.getYear(), Calendar.SEPTEMBER, 1);
            if (date.getDayOfWeek().getValue() == 7){
                point.set(Calendar.DATE, 2);
            }
        }
    }
}
