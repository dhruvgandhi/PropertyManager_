package com.wordpress.fruitything.propertymanager;

public class Payment {
    int txid;
    int amount;
    String tenant;
    String date;
    String upi_id;
    String upi_name;
    int isSuccessful;
    int propertyid;
    Payment(int txid, int amount, String tenant, String date, String upi_id, String upi_name, int isSuccessful, int propertyid)
    {
        this.txid = txid;
        this.amount = amount;
        this.tenant = tenant;
        this.date = date;
        this.upi_id = upi_id;
        this.upi_name = upi_name;
        this.isSuccessful = isSuccessful;
        this.propertyid = propertyid;
    }
}
