package hevs.it.SkiRunV2.models;

import java.util.List;
import java.util.Map;


public interface Mission {

    String getMissionName();
    String getDescription();
    String getLocation();
    String getTypeJob();
    Long getStartTime();
    Long getEndTime();
    Long getNbrPeople();
    List<String> getSubscribed();
    List<String> getSelecteds();
    Map<String,Object> getResults();

}
