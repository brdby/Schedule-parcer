import parcer.dbModule.DBHandler;
import parcer.exelModule.Core;
import parcer.exelModule.entity.Group;
import parcer.exelModule.entity.Study;
import parcer.exelModule.exeptions.parsingExeption;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try {
            Core schedule = new Core("r.xlsx");
            schedule.findStudies();
            DBHandler db = DBHandler.getInstance();

            for (Group  g: schedule.getGroups() ){
                for (Study s : g.getStudies()){
                    db.insertStudy(g.getName(), s.getName(), s.getDates());
                }
                System.out.println("Внесена группа " + g.getName());
            }
            schedule.close();
        } catch (parsingExeption | SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
