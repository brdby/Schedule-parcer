package parcer;

import java.time.LocalDate;
import java.util.HashSet;

public class Study {

    private String row;
    private String name;
    private HashSet<LocalDate> dates;
    String dateID;

    Study(String fullName) {
        this.row = fullName;
        name = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return row;
    }

    public void setDates(HashSet<LocalDate> h) {
        dates = h;
    }
}
