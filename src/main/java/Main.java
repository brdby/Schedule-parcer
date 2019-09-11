import parcer.Core;
import parcer.util.OpenFail;

public class Main {
    public static void main(String[] args){
        try {
            Core schedule = new Core("r.xlsx");
            schedule.findClasses();
        } catch (OpenFail openFail) {
            openFail.getMessage();
        }
    }
}
