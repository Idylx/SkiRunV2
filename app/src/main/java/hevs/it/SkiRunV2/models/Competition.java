package hevs.it.SkiRunV2.models;

import java.util.Date;
import java.util.Map;


public interface Competition {
    String getName();
    Long getStartDate();
    Long getEndDate();
    Map<String, Object> getGuestsClub();
    Map<String, Object> getDisciplines();
}
