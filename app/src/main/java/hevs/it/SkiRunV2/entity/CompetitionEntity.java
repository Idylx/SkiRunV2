package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.Map;

import hevs.it.SkiRunV2.models.Competition;


public class CompetitionEntity implements Competition {

    @NonNull
    @Exclude
    private String name;
    private Long startDate;
    private Long endDate;
    private String refApi;
    private Map<String, Object> guestsClub;
    private Map<String, Object> disciplines;


    public CompetitionEntity(){}


    public CompetitionEntity(Competition competition){
        this.name = competition.getName();
        this.startDate = competition.getStartDate();
        this.endDate = competition.getEndDate();
        this.guestsClub = competition.getGuestsClub();
        this.disciplines = competition.getDisciplines();
    }




    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public Long getStartDate() {
        return startDate;
    }

    @Override
    public Long getEndDate() {
        return null;
    }

    public void setStartDate(Long startDate){
        this.startDate = startDate;
    }

    @Override
    public Map<String, Object> getGuestsClub() {
        return guestsClub;
    }

    public void setGuestsClub(Map<String, Object> guestsClub){
        this.guestsClub = guestsClub;
    }


    @Override
    public Map<String, Object> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(Map<String, Object> trials){
        this.disciplines = trials;
    }

    public String getRefApi() {
        return refApi;
    }
}
