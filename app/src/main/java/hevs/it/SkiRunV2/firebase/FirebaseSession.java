package hevs.it.SkiRunV2.firebase;

import com.google.firebase.auth.FirebaseAuth;

public class FirebaseSession {

    public static final String UID_USER = FirebaseAuth.getInstance().getUid();

    public static final String NODE_USERS = "usersV2";
    public static final String NODE_CLUBS = "clubsV2";
    public static final String NODE_TYPEJOBS = "typeJobsV2";
    public static final String NODE_COMPETITIONS = "competitionsV2";

    //Competiontion nodes
    public static final String NODE_ENDDATE = "endDate";
    public static final String NODE_REFAPI = "refAPI";
    public static final String NODE_STARTDATE = "startDate";
    public static final String NODE_GUESTCLUBS = "guestClubs";
    public static final String NODE_DISCIPLINES = "disciplines";

    //Mission nodes
    public static final String NODE_DESCRIPTION = "description";
    public static final String NODE_DOOR = "door";
    public static final String NODE_ENDDT = "endDateTime";
    public static final String NODE_STARTDT = "startDateTime";
    public static final String NODE_NB = "nbrPeople";
    public static final String NODE_TYPEOFJOB = "typeJob";
    public static final String NODE_SUBSCRIBED = "subscribed";
    public static final String NODE_SELECTED = "selected";



}
