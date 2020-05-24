package eh.com.timhealthcaretest.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import eh.com.timhealthcaretest.model.Patient;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface PatientDao {

    @Query("SELECT * FROM patient ORDER BY appointmentDate")
    Flowable< List< Patient > > getAllPatients ( );

    @Insert
    Single< Long > insertPatient (Patient patient);
}
