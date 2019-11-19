package parcer.exelModule.entity;

import java.util.HashSet;

public class Group {

    private String name;
    private HashSet<Study> studies;

    public Group(String name){
        this.name = name;
        studies = new HashSet<>();
    }

    public void addStudy(Study study){
        studies.add(study);
    }

    public HashSet<Study> getStudies(){
        return studies;
    }

    public String getName() {
        return name;
    }
}
