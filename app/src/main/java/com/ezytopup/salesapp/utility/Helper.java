package com.ezytopup.salesapp.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by indraaguslesmana on 4/1/17.
 */

public class Helper {

    public static String deviceId(){
        //register device_id
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String getDeviceDate = dateFormatter.format(today);

        Random r = new Random();
        int getDeviceNum = r.nextInt(99999999 - 10000000) + 10000000;
        return getDeviceDate + String.valueOf(getDeviceNum);
    }
}
