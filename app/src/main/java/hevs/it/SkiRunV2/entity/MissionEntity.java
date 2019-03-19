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
    private Long nbrPeople;
    private List<String> subscribeds;
    private List<String> selecteds;
    private Map<String,Object> results;



    public MissionEntity(){}

    public MissionEntity(Mission mission){

        this.missionName = mission.getMissionName();
        this.description = mission.getDescription();
        this.typeJob= mission.getTypeJob();
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
    public Long getNbrPeople() {
        return nbrPeople;
    }

    @Override
    public List <String> getSubscribed() {
        return subscribeds;
    }

    @Override
    public List<String> getSelecteds() {
        return selecteds;
    }

    @Override
    public Map<String, Object> getResults() {
        return results;
    }

    public void setEndTime(Long endTime){
        this.endTime = endTime;
    }


    public void setSubscribeds(List<String> subscribeds) {
        this.subscribeds = subscribeds;
    }

    public void setSelecteds(List<String> selecteds) {
        this.selecteds = selecteds;
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
    public void setNbrPeople(Long nbrPeople) {
        this.nbrPeople = nbrPeople;
    }


    @Override
    public String toString() {
        return  missionName + " ";
    }

}


