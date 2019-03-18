package hevs.it.SkiRunV2.models;

import java.util.List;
import java.util.Map;


public interface Mission {

    String getMissionName();
    String getDescription();
    String getDoors();
    String getTypeJob();
    Long getStartTime();
    Long getEndTime();
    Long getNbrPeople();
    List getSubscribed();
    List getSelecteds();
    Map<String,Object> getResults();

}
