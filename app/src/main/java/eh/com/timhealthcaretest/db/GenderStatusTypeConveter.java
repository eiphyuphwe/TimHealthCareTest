package eh.com.timhealthcaretest.db;

import androidx.room.TypeConverter;
import eh.com.timhealthcaretest.model.Patient;

public class GenderStatusTypeConveter {
    @TypeConverter
    public static Patient.Gender fromStringtoGenderEnumType (String type) {
        switch (type) {
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

    @TypeConverter
    public static String fromGenderEnumTypeToString (Patient.Gender gender) {
        switch (gender) {
            case MALE:
                return "Male";
            case FEMALE:
                return "Female";
            case OTHER:
                return "Other";
            case UNKNOWN:
                return "Unknown";
        }
        return "Unknown";
    }

}
