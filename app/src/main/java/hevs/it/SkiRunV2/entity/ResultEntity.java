package hevs.it.SkiRunV2.entity;

import com.google.firebase.database.Exclude;

import hevs.it.SkiRunV2.models.Result;

public class ResultEntity implements Result {

    @Exclude
    private int bibNumber;

    private String result;
    private String unit;

    private String cameraLink;

    public ResultEntity(){}


    public ResultEntity(Result result){
        this.bibNumber = result.getBibNumber();
        this.result = result.getResult();
        this.unit = result.getUnit();

    }

    @Exclude
    @Override
    public int getBibNumber() {
        return bibNumber;
    }
    public void setBibNumber(int bibNumber){
        this.bibNumber = bibNumber;
    }

    @Override
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }


    @Override
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String getCameraLink() { return cameraLink; }
    public void setCameraLink(String cameraLink) { this.cameraLink = cameraLink; }


}
