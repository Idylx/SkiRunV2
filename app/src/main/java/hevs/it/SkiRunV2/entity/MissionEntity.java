package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.util.List;
import java.util.Map;

import hevs.it.SkiRunV2.models.Mission;


public class MissionEntity implements Mission {

    @NonNull
    @Exclude
    private String missionName;
    private String description;
    private String typeJob;
    private String doors;
    private Long startTime;
    private Long endTime;
    private int nbrPeople;
    private List subscribeds;
    private List selecteds;
    private Map<String,Object> results;



    public MissionEntity(){}

    public MissionEntity(Mission mission){

        this.missionName = mission.getMissionName();
        this.description = mission.getDescription();
        this.startTime = mission.getStartTime();
        this.endTime = mission.getEndTime();
        this.doors = mission.getDoors();
        this.nbrPeople = mission.getNbrPeople();
        this.subscribeds = mission.getSubscribed();
        this.selecteds = mission.getSelecteds();
        this.results = mission.getResults();
    }


    @Override
    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String name){
        this.missionName = name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    @Override
    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime){
        this.startTime = startTime;
    }

    @Override
    public Long getEndTime() {
        return endTime;
    }

    @Override
    public int getNbrPeople() {
        return nbrPeople;
    }

    @Override
    public List getSubscribed() {
        return subscribeds;
    }

    @Override
    public List getSelecteds() {
        return selecteds;
    }

    @Override
    public Map<String, Object> getResults() {
        return results;
    }

    public void setEndTime(Long endTime){
        this.endTime = endTime;
    }



    @Override
    public String getDoors() {
        return doors;
    }

    @Override
    public String getTypeJob() {
        return typeJob;
    }

    public void setTypeJob(String typeJob) {
        this.typeJob = typeJob;
    }

    public void setDoors(String doors){
        this.doors = doors;
    }

    /*
    public String toString(){
        if(getDoors() == null)
            return getHourAndMinutes(fromTimestamp(getStartTime())) + " - "
                    + getHourAndMinutes(fromTimestamp(getEndTime())) + " "
                    + getMissionName();
        else
            return getHourAndMinutes(fromTimestamp(getStartTime())) + " - "
                    + getHourAndMinutes(fromTimestamp(getEndTime())) + " "
                    + getMissionName() + " " + getDoors();
    }
*/
    public void setNbrPeople(int nbrPeople) {
        this.nbrPeople = nbrPeople;
    }

    @Override
    public String toString() {
        return "MissionEntity{" +
                "missionName='" + missionName + '\'' +
                '}';
    }
}

