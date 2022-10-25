package me.kyrobi.Tokoyami.Auto.VCRooms;

public class ChannelPairing {

    private long vcID;
    private long txtID;
    private long creatorID;

    public void setVcID(long vcID){
        this.vcID = vcID;
    }

    public void setTxtID(long txtID){
        this.txtID = txtID;
    }

    public void setCreatorID(long creatorID){
        this.creatorID = creatorID;
    }

    public long getVcID(){
        return this.vcID;
    }

    public long getTxtID(){
        return this.txtID;
    }

    public long getCreatorID(){
        return this.creatorID;
    }
}
