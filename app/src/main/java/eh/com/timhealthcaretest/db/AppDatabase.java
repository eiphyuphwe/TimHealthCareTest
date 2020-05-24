package eh.com.timhealthcaretest.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import eh.com.timhealthcaretest.model.Patient;

@Database(entities = {Patient.class}, version = 1)
@TypeConverters({Converters.class, GenderStatusTypeConveter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract PatientDao patientDao ( );

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase (final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder ( context.getApplicationContext (),
                            AppDatabase.class, "timhealthcare_database" )
                            // Wipes and rebuilds instead of migrating
                            .fallbackToDestructiveMigration ()
                            .build ();
                }
            }
        }
        return INSTANCE;
    }
}

