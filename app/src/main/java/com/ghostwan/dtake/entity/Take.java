package com.ghostwan.dtake.entity;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by erwan on 20/11/2016.
 */
public class Take extends SugarRecord implements Serializable{

    private Date takeDate;
    private Boolean isOk;

    public Take(Date takeDate, Long time) {
        this.takeDate = takeDate;
    }

    public Take(Boolean isOk) {
        this.takeDate = new Date();
        this.isOk = isOk;
    }

    public Take() {
    }

    public Date getTakeDate() {
        return takeDate;
    }

    public void setTakeDate(Date takeDate) {
        this.takeDate = takeDate;
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }

    public static String getWhereTakeDate(Long timestamp){
        return "date("+ (timestamp == null ? "TAKE_DATE" : timestamp) +"/1000, 'unixepoch', 'localtime')";
    }
    public static int countTakeToday(){
        return (int)Take.count(Take.class, getWhereTakeDate(null)+" = date('now','localtime')", null);
    }
}
