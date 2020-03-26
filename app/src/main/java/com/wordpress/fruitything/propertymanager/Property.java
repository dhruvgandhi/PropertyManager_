package com.wordpress.fruitything.propertymanager;

public class Property {
    String username, short_location, address,tenant ;
    int propertyid, rent, area, beds;
    Property(String username,String short_location, String address, int propertyid, int rent, int area)
    {
        this.username = username;
        this.short_location = short_location;
        this.address = address;
        this.propertyid = propertyid;
        this.rent = rent;
        this.area = area;
    }
    Property(String username,String short_location, String address, int propertyid, int rent, int area,String tenant, int beds)
    {
        this.username = username;
        this.short_location = short_location;
        this.address = address;
        this.propertyid = propertyid;
        this.rent = rent;
        this.area = area;
        this.tenant = tenant;
        this.beds = beds;
    }

    String display()
    {
          return short_location+" "+username+" "+address+" "+rent;
    }

}
