package eh.com.timhealthcaretest.ui.appointment.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import eh.com.timhealthcaretest.R;
import eh.com.timhealthcaretest.model.Patient;
import eh.com.timhealthcaretest.utils.Utils;


public class PatientRecyclerAdapter extends RecyclerView.Adapter< RecyclerView.ViewHolder > {

    List< Patient > patients = new ArrayList<> ();


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from ( parent.getContext () ).inflate ( R.layout.list_apptform_item, parent, false );

        return new PatientViewHolder ( view );
    }

    @Override
    public void onBindViewHolder (@NonNull RecyclerView.ViewHolder holder, int position) {

        if(patients.get ( position )!=null)
        ((PatientViewHolder) holder).bind ( patients.get ( position ) );
    }

    @Override
    public int getItemCount ( ) {
        return patients.size ();
    }

    public void setPatients (List< Patient > patients) {
        this.patients = patients;
        notifyDataSetChanged ();
    }

    public class PatientViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDob, txtGender, txtApptDate;

        public PatientViewHolder (@NonNull View itemView) {
            super ( itemView );
            txtName = itemView.findViewById ( R.id.txt_name );
            txtDob = itemView.findViewById ( R.id.txt_dob );
            txtGender = itemView.findViewById ( R.id.txt_gender );
            txtApptDate = itemView.findViewById ( R.id.txt_appt_date );

        }

        public void bind (Patient patient) {
            txtName.setText ( patient.getName () );
            txtDob.setText ( Utils.convertFormattedDate ( patient.getBirthDate ().toString () ) );
            txtGender.setText ( patient.getGender ().toString () );
            txtApptDate.setText ( Utils.convertFormattedDate ( patient.getAppointmentDate ().toString () ) );

        }

    }


}
