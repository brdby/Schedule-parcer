import parcer.dbModule.DBHandler;
import parcer.exelModule.Core;
import parcer.exelModule.entity.Group;
import parcer.exelModule.entity.Study;
import parcer.exelModule.exeptions.exelExeption;
import parcer.exelModule.exeptions.parcingExeption;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args){
        try {
            Core schedule = new Core("r.xlsx");
            schedule.findStudies();

            DBHandler db = DBHandler.getInstance();

            for (Group  g: schedule.getGroups() ){
                db.insertGroup(g.getName());
                for (Study s : g.getStudies()){
                    db.insertStudy(g.getName(), s.getName());
                }
                System.out.println("Внесена группа " + g.getName());
            }
            schedule.close();
        } catch (parcingExeption | exelExeption | SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
