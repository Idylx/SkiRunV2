package hevs.it.SkiRunV2.models;

import java.util.List;
import java.util.Map;

import hevs.it.SkiRunV2.entity.MissionEntity;

/**
 * Created by Mike Jason on 13.07.2018.
 *
 * Discipline Model
 *
 */

public interface Discipline {
    String getName();
    List<MissionEntity> getMissions();

}
