package eh.com.timhealthcaretest.ui.appointment.reposistory;

import java.util.List;

import eh.com.timhealthcaretest.db.PatientDao;
import eh.com.timhealthcaretest.model.Patient;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class PatientReposistory {

    private final PatientDao patientDao;


    public PatientReposistory (PatientDao patientDao) {
        this.patientDao = patientDao;
    }

    //insert
    public Single< Long > insertPatient (Patient patient) {

        return patientDao.insertPatient ( patient );

    }

    //get all patients
    public Flowable< List< Patient > > getAllPatients ( ) {
        return patientDao.getAllPatients ();
    }


}
