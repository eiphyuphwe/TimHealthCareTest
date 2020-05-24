package eh.com.timhealthcaretest.ui.appointment.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import eh.com.timhealthcaretest.db.AppDatabase;
import eh.com.timhealthcaretest.db.PatientDao;
import eh.com.timhealthcaretest.model.Patient;
import eh.com.timhealthcaretest.ui.appointment.Resource;
import eh.com.timhealthcaretest.ui.appointment.reposistory.PatientReposistory;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class PatientViewModel extends AndroidViewModel {

    private PatientReposistory patientReposistory;
    private PatientDao patientDao;
    private MediatorLiveData< Resource< List< Patient > > > mPatients = new MediatorLiveData<> ();

    public PatientViewModel (Application application) {
        super ( application );
        AppDatabase db = AppDatabase.getDatabase ( application );
        patientDao = db.patientDao ();
        patientReposistory = new PatientReposistory ( patientDao );
    }


    public LiveData< Resource< List< Patient > > > loadPatients ( ) {
        final LiveData< Resource< List< Patient > > > source = LiveDataReactiveStreams.fromPublisher ( getAllPatients () );
        mPatients.addSource ( source, new Observer< Resource< List< Patient > > > () {
            @Override
            public void onChanged (Resource< List< Patient > > listResource) {
                mPatients.setValue ( listResource );
                mPatients.removeSource ( source );
            }
        } );
        return mPatients;
    }

    public Single< Long > insertUser (Patient patient) {
        return patientReposistory.insertPatient ( patient );
    }

    private Flowable< Resource< List< Patient > > > getAllPatients ( ) {
        return patientReposistory.getAllPatients ()
                .onErrorReturn ( new Function< Throwable, List< Patient > > () {
                    @Override
                    public List< Patient > apply (Throwable throwable) throws Exception {
                        Patient patient = new Patient ();
                        patient.setId ( -1 );
                        ArrayList< Patient > patients = new ArrayList<> ();
                        patients.add ( patient );
                        return patients;
                    }
                } )
                .map ( new Function< List< Patient >, Resource< List< Patient > > > () {
                    @Override
                    public Resource< List< Patient > > apply (List< Patient > patients) throws Exception {
                        if (patients.size () > 0) {
                            if (patients.get ( 0 ).getId () == -1) {
                                return Resource.error ( null, "Something Went Wrong!" );
                            }

                        }
                        return Resource.success ( patients );
                    }
                } )
                .subscribeOn ( Schedulers.io () );
    }


}
