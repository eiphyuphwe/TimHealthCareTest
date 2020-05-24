package eh.com.timhealthcaretest;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import eh.com.timhealthcaretest.db.Converters;
import eh.com.timhealthcaretest.db.GenderStatusTypeConveter;
import eh.com.timhealthcaretest.model.Patient;
import eh.com.timhealthcaretest.utils.Utils;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UtilsUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none ();

    @Before
    public void setup ( ) {
        MockitoAnnotations.initMocks ( this );
    }

    @Test
    public void test_correct_convertFormattedDate ( ) {
        String datestr = "Sat May 23 08:00:30 GMT+08:00 2020";
        String formattedDateStr = "";
        String expectedDateStr = "23/05/2020";
        formattedDateStr = Utils.convertFormattedDate ( datestr );
        Assert.assertEquals ( formattedDateStr, expectedDateStr );
    }

    @Test
    public void test_incorrectformat_convertFormattedDate ( ) {
        String datestr = "Sat May 23 2020";
        ParseException expected = new ParseException ( "Unparseable date: \"Sat May 23 2020\"", 15 );
        try {
            Utils.convertFormattedDate ( datestr );
        } catch (Exception e) {
            if (e instanceof ParseException) {
            }
            Assert.assertEquals ( e, expected );
        }
    }

    @Test
    public void test_getGenderfromString ( ) {
        Patient.Gender expectedGender = Patient.Gender.MALE;
        Assert.assertEquals ( Utils.getGenderfromString ( "Male" ), expectedGender );

    }

    @Test
    public void test_invalidtest_getGenderFromString ( ) {
        Patient.Gender expectedGender = Patient.Gender.UNKNOWN;
        Assert.assertEquals ( Utils.getGenderfromString ( "Hello" ), expectedGender );

    }

    @Test
    public void test_dateConvertor ( ) {
        String datestr = "Sat May 23 08:00:30 GMT+08:00 2020";
        SimpleDateFormat sdf = new SimpleDateFormat ( "EEE MMM dd hh:mm:ss zzz yyyy" );
        try {

            Date expDate = sdf.parse ( datestr );
            Long dateLong = Converters.dateToTimestamp ( expDate );
            Date actualDate = Converters.fromTimestamp ( dateLong );
            Assert.assertEquals ( actualDate, expDate );

        } catch (ParseException e) {

        }
    }

    @Test
    public void test_nulldateConvertor ( ) {
        String datestr = "Sat May 23 08:00:30 GMT+08:00 2020";

        Long dateLong = Converters.dateToTimestamp ( null );
        Date actualDate = Converters.fromTimestamp ( null );
        Assert.assertNull ( dateLong );
        Assert.assertNull ( actualDate );

    }

    @Test
    public void test_genderConverter ( ) {
        String testStr = "Male";
        Patient.Gender expectedGender = Patient.Gender.MALE;
        Patient.Gender actualGender = GenderStatusTypeConveter.fromStringtoGenderEnumType ( testStr );
        Assert.assertEquals ( actualGender, expectedGender );

        String expectedStr = "Female";
        Assert.assertEquals ( GenderStatusTypeConveter.fromGenderEnumTypeToString ( Patient.Gender.FEMALE ), expectedStr );


    }

    @Test
    public void test_invalidDob ( ) {

        Long today = MaterialDatePicker.todayInUtcMilliseconds ();

        String birthdateStr = "Sat May 29 08:00:30 GMT+08:00 2020";

        SimpleDateFormat sdf = new SimpleDateFormat ( "EEE MMM dd hh:mm:ss zzz yyyy" );
        try {

            Date dobDate = sdf.parse ( birthdateStr );
            Date todayDate = Converters.fromTimestamp ( today );
            Assert.assertFalse ( Utils.checkBirthDate ( todayDate,dobDate));
        }catch (ParseException e)
        {

        }
    }

    @Test
    public void test_validDob ( ) {

        Long today = MaterialDatePicker.todayInUtcMilliseconds ();

        String birthdateStr = "Sat May 29 08:00:30 GMT+08:00 1989";

        SimpleDateFormat sdf = new SimpleDateFormat ( "EEE MMM dd hh:mm:ss zzz yyyy" );
        try {

            Date dobDate = sdf.parse ( birthdateStr );
            Date todayDate = Converters.fromTimestamp ( today );
            Assert.assertTrue ( Utils.checkBirthDate ( todayDate,dobDate));
        }catch (ParseException e)
        {

        }
    }


}