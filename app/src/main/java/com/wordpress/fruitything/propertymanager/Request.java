package com.wordpress.fruitything.propertymanager;

public class Request {
    int requestid,propertyid;
    boolean status;
    String owner, requester;

    public Request(int requestid,int propertyid, boolean status, String owner, String requester)
    {
        this.requestid = requestid;
        this.propertyid = propertyid;
        this.status = status;
        this.owner = owner;
        this.requester = requester;
    }

}
