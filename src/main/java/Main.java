import parcer.Core;
import parcer.util.OpenFail;

public class Main {
    public static void main(String[] args){
        try {
            Core schedule = new Core("r.xlsx");
            schedule.findStudies();
            schedule.allStudies();
        } catch (OpenFail openFail) {
            openFail.getMessage();
        }
    }
}
