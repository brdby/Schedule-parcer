package parcer.exelModule.entity;

public class Study {

    private String row;
    private String name;
    private boolean even;
    private String dates;
    private int weekDay;
    private String professor;

    public Study(String fullName) {
        this.row = fullName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " : (" + even +") " + dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getDates(){
        return dates;
    }

    public int getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(int weekDay) {
        this.weekDay = weekDay;
    }

    public boolean isEven() {
        return even;
    }

    public void setEven(boolean even) {
        this.even = even;
    }
}
