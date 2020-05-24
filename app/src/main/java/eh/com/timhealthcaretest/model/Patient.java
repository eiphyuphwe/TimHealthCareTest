package eh.com.timhealthcaretest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patient")
public class Patient {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("birthDate")
    @Expose
    private Date birthDate;

    @SerializedName("gender")
    @Expose
    private Gender gender;

    @SerializedName("appointmentDate")
    @Expose
    private Date appointmentDate;


    public int getId ( ) {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName ( ) {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public Date getBirthDate ( ) {
        return birthDate;
    }

    public void setBirthDate (Date birthDate) {
        this.birthDate = birthDate;
    }

    public Gender getGender ( ) {
        return gender;
    }

    public void setGender (Gender gender) {
        this.gender = gender;
    }

    public Date getAppointmentDate ( ) {
        return appointmentDate;
    }

    public void setAppointmentDate (Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }


    @Override
    public String toString ( ) {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", gender=" + gender +
                ", appointmentDate=" + appointmentDate +
                '}';
    }

    //gender (type: Enum) – example: { MALE, FEMALE, OTHER, UNKNOWN }
    public enum Gender {
        MALE,
        FEMALE,
        OTHER,
        UNKNOWN

    }

}
