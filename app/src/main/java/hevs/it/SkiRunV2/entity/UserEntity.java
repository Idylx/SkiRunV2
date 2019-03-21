package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import hevs.it.SkiRunV2.models.User;


public class UserEntity implements User {

    @NonNull
    @Exclude
    private String uid;
    private String email;
    private String firstname;
    private String lastname;
    private String phone;
    private String role;
    private String club;
    private String jobPreference;


    public UserEntity(){}

    public UserEntity(User user){
        this.uid=user.getUid();
        this.email = user.getEmail();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.phone = user.getPhone();
        this.role = user.getRole();
        this.club = user.getClub();
        this.jobPreference = user.getJobPreference();


    }


    @Override
    public String getUid() { return null; }

    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    @Override
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname){
        this.firstname = firstname;
    }

    @Override
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname){
        this.lastname = lastname;
    }

    @Override
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public String getJobPreference() {
        return jobPreference;
    }

    public void setJobPreference(String jobPreference) {
        this.jobPreference = jobPreference;
    }

    @Override
    public String getClub() {
        return club;
    }

    public void setRole(String role){
        this.role = role;
    }

    public String toString(){
        return getFirstname() + " " + getLastname();
    }
}
