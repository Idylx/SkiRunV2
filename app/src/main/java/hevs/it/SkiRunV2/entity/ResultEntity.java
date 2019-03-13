package hevs.it.SkiRunV2.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import hevs.it.SkiRunV2.models.Result;

public class ResultEntity implements Result {

    @NonNull
    @Exclude
    private int bibNumber;
    private String status;
    private String cameraLink;
    private Long result;

    public ResultEntity(){}


    public ResultEntity(Result result){

        this.bibNumber = result.getBibNumber();
        this.status = result.getStatus();
        this.cameraLink = result.getCameraLink();
        this.result = result.getResult();

    }


    @Override
    public int getBibNumber() {
        return bibNumber;
    }
    public void setBibNumber(int bibNumber){
        this.bibNumber = bibNumber;
    }

    @Override
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getCameraLink() {
        return cameraLink;
    }
    public void setCameraLink(String cameraLink) {
        this.cameraLink = cameraLink;
    }


    @Override
    public Long getResult() {
        return result;
    }
    public void setResult(Long result) {
        this.result = result;
    }
}
