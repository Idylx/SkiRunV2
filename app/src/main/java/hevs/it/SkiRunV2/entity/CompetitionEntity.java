package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.List;

import hevs.it.SkiRunV2.models.Competition;


public class CompetitionEntity implements Competition {

    @NonNull
    @Exclude
    private String name;
    private long startDate;
    private long endDate;
    private String refApi;
    private List<ClubEntity> guestsClub;
    private List<DisciplineEntity> disciplines;


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
    public long getStartDate() {
        return startDate;
    }

    @Override
    public long getEndDate() {
        return endDate;
    }

    public void setStartDate(Long startDate){
        this.startDate = startDate;
    }

    public void setEndDate(Long endDate){
        this.endDate = endDate;
    }


    @Override
    public List<ClubEntity> getGuestsClub() {
        return guestsClub;
    }

    public void setGuestsClub(List<ClubEntity> guestsClub){
        this.guestsClub = guestsClub;
    }


    @Override
    public List<DisciplineEntity> getDisciplines() {
        return disciplines;
    }

    public void setDisciplines(List<DisciplineEntity> disciplines){
        this.disciplines = disciplines;
    }

    public String getRefApi() {
        return refApi;
    }

    public void setRefApi(String refApi){this.refApi = refApi;}


    @Override
    public String toString() {
        return "CompetitionEntity{" +
                "name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", refApi='" + refApi + '\'' +
                ", guestsClub=" + guestsClub +
                ", disciplines=" + disciplines +
                '}';
    }
}
