package parcer;

import java.util.ArrayList;
import java.util.HashSet;

public class Group {
    private String name;
    private HashSet<Study> studies;

    Group(String name){
        this.name = name;
        studies = new HashSet<>();
    }

    public boolean addStudy(Study study){
       return studies.add(study);
    }

    public String toString(){
        return name + " - " + studies.toString();
    }
}
