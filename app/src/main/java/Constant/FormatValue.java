package Constant;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by USER on 23/4/2016.
 */
public class FormatValue {

    public static String getDistance(double distance){
        String rs = "";
        if(distance < 1000) {
           rs = String.format("%.02f", (float) (distance)) + "m";
        }else {
            distance = distance/1000;
            rs = String.format("%.02f", (float) (distance)) + "km";
        }

        return rs;
    }

    public static String getTimeComment(double dif, String create){
        String rs = "";
        String date= create.split(" ")[0];

        if(dif< 6){
            return "just now";
        }else {
            if(dif < 60){

                return (int) dif +" giây trước";
            }
            else {
                if(dif < 3600){
                   return (int) dif/60 +"phút trước";
                }
                else {
                    if(dif< 24*3600){
                        return (int) dif/3600  +"giờ trước";
                    }
                    else {
                        return date;
                    }
                }
            }
        }

    }
}
