package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Map;

import hevs.it.SkiRunV2.models.Discipline;


public class DisciplineEntity implements Discipline {

    @NonNull
    @Exclude
    private String name;
    private Map<String, Object> missions;


    public DisciplineEntity(){}

    public DisciplineEntity(Discipline discipline){

        name = discipline.getName();
        missions = discipline.getMissions();

    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getMissions() {
        return null;
    }

    public void setName(String name){
        this.name = name;
    }

}
