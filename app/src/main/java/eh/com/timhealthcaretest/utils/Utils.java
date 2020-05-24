package eh.com.timhealthcaretest.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eh.com.timhealthcaretest.model.Patient;

public class Utils {

    //Sat May 23 hr:mm:sec GMT+08:00 2020
    private static String ORIGINALPATTERN = "EEE MMM dd hh:mm:ss zzz yyyy";
    private static String PATTERN = "dd/MM/yyyy";

    public static String convertFormattedDate (String dateStr) {
        String formattedStr = "";

        SimpleDateFormat sdf = new SimpleDateFormat ( ORIGINALPATTERN );
        try {

            Date date = sdf.parse ( dateStr ); //convert str to date
            SimpleDateFormat formatter = new SimpleDateFormat ( PATTERN );

            formattedStr = formatter.format ( date );


        } catch (ParseException e) {
            e.printStackTrace ();
        }

        return formattedStr;

    }

    public static Patient.Gender getGenderfromString (String genderStr) {
        switch (genderStr) {
            case "Male":
                return Patient.Gender.MALE;
            case "Female":
                return Patient.Gender.FEMALE;
            case "Other":
                return Patient.Gender.OTHER;
            case "Unknown":
                return Patient.Gender.UNKNOWN;
        }
        return Patient.Gender.UNKNOWN;
    }


    public static boolean checkBirthDate(Date today,Date bdate)
    {
        if(today.before ( bdate))
        {
            return false;
        }
        else return true;
    }



}
