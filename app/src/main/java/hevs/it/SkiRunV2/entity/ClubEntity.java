package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import hevs.it.SkiRunV2.models.Club;

// Club entity
public class ClubEntity implements Club {

    @NonNull
    @Exclude
    private String name;

    public ClubEntity(){}

    public ClubEntity(Club club){
        name = club.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
