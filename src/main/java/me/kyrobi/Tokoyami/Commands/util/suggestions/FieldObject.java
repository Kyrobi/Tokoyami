package me.kyrobi.Tokoyami.Commands.util.suggestions;

public class FieldObject {

    private long userID = 0;
    private boolean userAcceptedDisclaimer = false;
    StringBuilder userSummary = new StringBuilder();
    StringBuilder userPositive = new StringBuilder();
    StringBuilder userNegative = new StringBuilder();


    public void setUserID(long userID){
        this.userID = userID;
    }

    public long getUserID(){
        return this.userID;
    }

    public void setUserAcceptedDisclaimer(boolean accepted){
        this.userAcceptedDisclaimer = accepted;
    }

    public void setUserSummary(String summary){
        userSummary.append(summary);
    }

    public void setUserNegative(String input){
        userNegative.append(input);
    }

    public void setUserPositive(String input){
        userNegative.append(input);
    }

    public String getUserSummary(String summary){
        return userPositive.toString();
    }

    public String getUserNegative(String input){
        return userPositive.toString();
    }

    public String getUserPositive(String input){
        return userPositive.toString();
    }
}
