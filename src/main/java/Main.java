import parcer.Core;
import parcer.util.exeptions.CloseFail;
import parcer.util.exeptions.NoEvenPointer;
import parcer.util.exeptions.NoWeekPointer;
import parcer.util.exeptions.OpenFail;

public class Main {
    public static void main(String[] args){
        try {
            Core schedule = new Core("r.xlsx");
            schedule.findStudies();
            schedule.allStudies();
            schedule.close();
        } catch (OpenFail | NoWeekPointer | NoEvenPointer | CloseFail e) {
            System.out.println(e.getMessage());
        }
    }
}
