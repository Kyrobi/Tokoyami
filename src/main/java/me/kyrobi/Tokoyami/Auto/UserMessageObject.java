package me.kyrobi.Tokoyami.Auto;

public class UserMessageObject {

    long ID;
    long time;
    String user;
    String message;


    UserMessageObject(long ID, String user, String message){
        time = System.currentTimeMillis();
        this.ID = ID;
        this.user = user;
        this.message = message;
    }

    public long getID(){
        return ID;
    }

    public long getTime(){
        return time;
    }

    public String getUsername(){
        return user;
    }

    public String getUserMessage(){
        return message;
    }

}
