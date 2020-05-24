package eh.com.timhealthcaretest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import eh.com.timhealthcaretest.db.PatientDao;
import eh.com.timhealthcaretest.model.Patient;
import eh.com.timhealthcaretest.ui.appointment.reposistory.PatientReposistory;
import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.subscribers.TestSubscriber;

public class PatientReposistoryTest {


    PatientReposistory reposistory;
    @Mock
    PatientDao patientDao;
    @Rule
    public InstantTaskExecutorRule executorRule = new InstantTaskExecutorRule ();

    @Before
    public void setup ( ) {
        MockitoAnnotations.initMocks ( this );
        reposistory = new PatientReposistory ( patientDao );

    }

    @Test
    public void test_insertPatientSuccess ( ) {
        Patient patient = new Patient ();
        patient.setName ( "Aster" );
        String datestr = "Sat May 23 08:00:30 GMT+08:00 2020";
        SimpleDateFormat sdf = new SimpleDateFormat ( "EEE MMM dd hh:mm:ss zzz yyyy" );
        try {

            Date testDate = sdf.parse ( datestr );
            patient.setBirthDate ( testDate );
            patient.setAppointmentDate ( testDate );
        } catch (ParseException e) {

        }

        patient.setGender ( Patient.Gender.FEMALE );
        Long explong = 1L;

        //Mockito.doReturn ( Single.just ( along ) ).when(reposistory.insertPatient ( patient ));

        Mockito.when ( reposistory.insertPatient ( patient ) ).thenReturn ( Single.just ( explong ) );
        reposistory.insertPatient ( patient ).toObservable ().subscribe ( new Observer< Long > () {
            @Override
            public void onSubscribe (Disposable d) {

            }

            @Override
            public void onNext (Long aLong) {
                Long expLo = 1L;
                Assert.assertEquals ( aLong, expLo );

            }

            @Override
            public void onError (Throwable e) {

            }

            @Override
            public void onComplete ( ) {

            }
        } );

    }

    @Test
    public void test_getPatientsApptRecordsSuccess ( ) throws Exception {
        TestSubscriber< List< Patient > > testSubscriber = new TestSubscriber<> ();
        List< Patient > patients = new ArrayList<> ();
        Patient patient = new Patient ();
        patient.setName ( "Aster" );
        String datestr = "Sat May 23 08:00:30 GMT+08:00 2020";
        SimpleDateFormat sdf = new SimpleDateFormat ( "EEE MMM dd hh:mm:ss zzz yyyy" );
        try {

            Date testDate = sdf.parse ( datestr );
            patient.setBirthDate ( testDate );
            patient.setAppointmentDate ( testDate );
        } catch (ParseException e) {

        }

        patient.setGender ( Patient.Gender.FEMALE );

        patients.add ( patient );

        //Mockito.doReturn( Flowable.just(patients)).when(reposistory.getAllPatients ();
        Mockito.when ( reposistory.getAllPatients () ).thenReturn ( Flowable.just ( patients ) );

        //trigger respone

        reposistory.getAllPatients ().subscribe ( testSubscriber );

        if (testSubscriber.values ().size () > 0) {
            List< Patient > result = testSubscriber.values ().get ( 0 );
            testSubscriber.assertComplete ();
            testSubscriber.assertNoErrors ();

            testSubscriber.assertValue ( result );
        }


    }


    @Test
    public void test_getPatientsApptRecordsFialure ( ) throws Exception {

        Throwable exception = new IOException ();
        TestSubscriber< List< Patient > > testSubscriber = new TestSubscriber<> ();
        //Mockito.doReturn( Flowable.just(patients)).when(reposistory.getAllPatients ();
        Mockito.when ( reposistory.getAllPatients () ).thenReturn ( Flowable.< List< Patient > >error ( exception ) );
        reposistory.getAllPatients ().subscribe ( testSubscriber );
        testSubscriber.assertError ( exception );

    }


}
