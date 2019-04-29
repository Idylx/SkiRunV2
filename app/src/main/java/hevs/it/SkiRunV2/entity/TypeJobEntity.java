package hevs.it.SkiRunV2.entity;
import android.support.annotation.NonNull;
import com.google.firebase.database.Exclude;
import hevs.it.SkiRunV2.models.TypeJob;

public class TypeJobEntity implements TypeJob {

    @NonNull
    @Exclude
    private String name;

    public TypeJobEntity(){}

    public TypeJobEntity(TypeJob job){
        this.name = job.getName();
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
