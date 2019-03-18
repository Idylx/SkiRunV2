package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

import hevs.it.SkiRunV2.models.Discipline;
import hevs.it.SkiRunV2.models.Mission;


public class DisciplineEntity implements Discipline {

    @NonNull
    @Exclude
    private String name;
    private List<MissionEntity> missions;


    public DisciplineEntity(){}

    public DisciplineEntity(Discipline discipline){

        name = discipline.getName();
        missions = discipline.getMissions();

    }
    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }


    @Override
    public List<MissionEntity> getMissions() {
        return missions;
    }

    public void setMissions(List<MissionEntity> missions){
        this.missions = missions;
    }

    @Override
    public String toString() {
        return name;
    }
}
