package hevs.it.SkiRunV2.models;

import java.util.List;
import hevs.it.SkiRunV2.entity.ClubEntity;
import hevs.it.SkiRunV2.entity.DisciplineEntity;


public interface Competition {
    String getName();
    long getStartDate();
    long getEndDate();
    List<ClubEntity> getGuestsClub();
    List<DisciplineEntity> getDisciplines();
}
