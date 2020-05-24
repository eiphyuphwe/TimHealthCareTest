package eh.com.timhealthcaretest.ui.appointment.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import eh.com.timhealthcaretest.R;
import eh.com.timhealthcaretest.db.Converters;
import eh.com.timhealthcaretest.model.Patient;
import eh.com.timhealthcaretest.ui.appointment.RefreshEvent;
import eh.com.timhealthcaretest.ui.appointment.viewmodel.PatientViewModel;
import eh.com.timhealthcaretest.utils.Utils;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PatientAptFormDialogFragment extends DialogFragment {

    TextInputEditText edtName;
    TextInputLayout txtLayName;
    AutoCompleteTextView edtGender;
    TextInputEditText edtDob;
    TextInputEditText edtApptDate;
    MaterialButton btnSubmit;
    ImageView imgDob;
    ImageView imgApptDate;
    String[] genderList;
    private long today;
    TextView txtShowError;
    Date dobDate;
    Date apptDate;
    String gender = "";


    public PatientAptFormDialogFragment ( ) {

    }

    private Dialog mDialog;
    private String noti_error_text = "";
    PatientViewModel patientViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {
        mDialog = new Dialog ( getActivity () );
        // mDialog = new Dialog(getActivity(),R.style.DialogTheme);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams ();
        layoutParams.copyFrom ( mDialog.getWindow ().getAttributes () );
        // layoutParams.windowAnimations = R.style.SlideUpDownDialog;

        final View view = View.inflate ( getActivity (), R.layout.fragment_patient_appt_form, null );
        mDialog.getWindow ().setAttributes ( layoutParams );
        mDialog.setContentView ( view );

        return mDialog;
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate ( R.layout.fragment_patient_appt_form, container, false );
        mDialog.getWindow ().setLayout ( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT );
        return view;
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );
        declare ( view );
        initSetting ();
        prepareGenderList ();
        catchEvents ();
    }


    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated ( savedInstanceState );
        patientViewModel = ViewModelProviders.of ( this ).get ( PatientViewModel.class );
    }

    private void declare (View view) {
        txtLayName = view.findViewById ( R.id.txt_lay_name );
        edtGender = view.findViewById ( R.id.edt_gender );
        edtName = view.findViewById ( R.id.edt_name );
        edtDob = view.findViewById ( R.id.edt_dob );
        edtApptDate = view.findViewById ( R.id.edt_appt_date );
        btnSubmit = view.findViewById ( R.id.btn_submit );
        imgDob = view.findViewById ( R.id.img_dob );
        imgApptDate = view.findViewById ( R.id.img_appt_date );
        txtShowError = view.findViewById ( R.id.txt_show_error );
        txtShowError.setVisibility ( View.GONE );
    }

    private void prepareGenderList ( ) {
        Resources res = getResources ();
        genderList = res.getStringArray ( R.array.gender );
        ArrayAdapter< String > adapter = new ArrayAdapter<> ( getActivity (), R.layout.list_gender_item, genderList );
        edtGender.setAdapter ( adapter );
    }

    private void catchEvents ( ) {
        edtGender.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick (AdapterView< ? > parent, View view, int position, long id) {

                gender = genderList[position];
            }
        } );
        MaterialDatePicker.Builder< Long > dobBuilder = MaterialDatePicker.Builder.datePicker ();
        dobBuilder.setTitleText ( "Select Birth Date" );
        //set constraint = end today
        CalendarConstraints.Builder dobConstraintsBuilder = new CalendarConstraints.Builder ();
        dobConstraintsBuilder.setEnd ( today );
        CalendarConstraints dobCalendarConstraints = dobConstraintsBuilder.build ();
        dobBuilder.setCalendarConstraints ( dobCalendarConstraints );

        final MaterialDatePicker< Long > dobDatePicker = dobBuilder.build ();

        imgDob.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View v) {


                dobDatePicker.show ( getFragmentManager (), "DATE_PICKER" );

            }
        } );
        dobDatePicker.addOnPositiveButtonClickListener ( new MaterialPickerOnPositiveButtonClickListener () {
            @Override
            public void onPositiveButtonClick (Object selection) {

                String formattedDobStr = Utils.convertFormattedDate ( Converters.fromTimestamp ( dobDatePicker.getSelection () ).toString () );
                dobDate = Converters.fromTimestamp ( dobDatePicker.getSelection () );
                Date todayDate = Converters.fromTimestamp ( today );
                if (Utils.checkBirthDate ( todayDate, dobDate )) {
                    edtDob.setText ( formattedDobStr );
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder ( getActivity () );
                    builder.setMessage ( "Date of birth is not correct" )
                            .setTitle ( "Error" );
                    builder.setPositiveButton ( "OK", new DialogInterface.OnClickListener () {
                        @Override
                        public void onClick (DialogInterface dialog, int which) {
                            EventBus.getDefault ().post ( new RefreshEvent () );
                            dialog.dismiss ();
                            dobDatePicker.show ( getFragmentManager (), "DATE_PICKER" );

                        }
                    } );
                    AlertDialog alertDialog = builder.create ();
                    alertDialog.show ();
                }
            }
        } );

        MaterialDatePicker.Builder< Long > apptBuilder = MaterialDatePicker.Builder.datePicker ();
        apptBuilder.setTitleText ( "Select Appointment Date" );
        //set constraint = start from today
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder ();
        constraintsBuilder.setValidator ( DateValidatorPointForward.now () );
        CalendarConstraints calendarConstraints = constraintsBuilder.build ();
        apptBuilder.setCalendarConstraints ( calendarConstraints );

        final MaterialDatePicker< Long > apptDatePicker = apptBuilder.build ();

        imgApptDate.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                apptDatePicker.show ( getFragmentManager (), "DATE_PICKER1" );
            }
        } );

        apptDatePicker.addOnPositiveButtonClickListener ( new MaterialPickerOnPositiveButtonClickListener< Long > () {
            @Override
            public void onPositiveButtonClick (Long selection) {


                String formattedApptDateStr = Utils.convertFormattedDate ( Converters.fromTimestamp ( apptDatePicker.getSelection () ).toString () );
                apptDate = Converters.fromTimestamp ( apptDatePicker.getSelection () );
                edtApptDate.setText ( formattedApptDateStr );
            }
        } );


        btnSubmit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View v) {

                if (checkAppointForm ()) {
                    txtShowError.setVisibility ( View.GONE );
                    Patient patient = new Patient ();
                    patient.setName ( edtName.getText ().toString () );
                    if (dobDate != null) {
                        patient.setBirthDate ( dobDate );
                    }
                    if (apptDate != null)
                        patient.setAppointmentDate ( apptDate );
                    if (!gender.equalsIgnoreCase ( "" ))
                        patient.setGender ( Utils.getGenderfromString ( gender ) );
                    patientViewModel.insertUser ( patient )
                            .subscribeOn ( Schedulers.io () )
                            .observeOn ( AndroidSchedulers.mainThread () )
                            .subscribe ( new SingleObserver () {
                                @Override
                                public void onSubscribe (Disposable d) {

                                }

                                @Override
                                public void onSuccess (Object o) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder ( getActivity () );
                                    builder.setMessage ( "Submission was successful!" )
                                            .setTitle ( "Success" );
                                    builder.setPositiveButton ( "OK", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick (DialogInterface dialog, int which) {
                                            EventBus.getDefault ().post ( new RefreshEvent () );
                                            dialog.dismiss ();
                                            mDialog.dismiss ();

                                        }
                                    } );
                                    AlertDialog alertDialog = builder.create ();
                                    alertDialog.show ();


                                }

                                @Override
                                public void onError (Throwable e) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder ( getActivity () );
                                    builder.setMessage ( "Submission is not successful,please resend again!" )
                                            .setTitle ( "Submission Error" );
                                    builder.setPositiveButton ( "OK", new DialogInterface.OnClickListener () {
                                        @Override
                                        public void onClick (DialogInterface dialog, int which) {
                                            dialog.dismiss ();

                                        }
                                    } );
                                    AlertDialog alertDialog = builder.create ();
                                    alertDialog.show ();

                                }
                            } );

                } else {

                    txtShowError.setText ( noti_error_text );
                    txtShowError.setVisibility ( View.VISIBLE );
                }
            }
        } );
    }


    private void initSetting ( ) {
        today = MaterialDatePicker.todayInUtcMilliseconds ();

    }

    private boolean checkAppointForm ( ) {
        noti_error_text = "";

        if (edtName.getText ().toString ().trim ().equalsIgnoreCase ( "" )) {
            noti_error_text = "Name";
        }

        if (edtDob.getText ().toString ().trim ().equalsIgnoreCase ( "" )) {
            if (!noti_error_text.equalsIgnoreCase ( "" ))
                noti_error_text += ", ";
            noti_error_text += "Date of Birth";
        }

        if (edtGender.getText ().toString ().trim ().equalsIgnoreCase ( "" )) {
            if (!noti_error_text.equalsIgnoreCase ( "" ))
                noti_error_text += ", ";
            noti_error_text += "Gender";
        }

        if (edtApptDate.getText ().toString ().trim ().equalsIgnoreCase ( "" )) {
            if (!noti_error_text.equalsIgnoreCase ( "" ))
                noti_error_text += ", ";
            noti_error_text += "Appointment Date";
        }


        if ((edtName.getText ().toString ().trim ().equalsIgnoreCase ( "" )) ||
                (edtDob.getText ().toString ().trim ().equalsIgnoreCase ( "" )) ||
                (edtGender.getText ().toString ().equalsIgnoreCase ( "" ))
                || (edtApptDate.getText ().toString ().trim ().equalsIgnoreCase ( "" ))) {
            noti_error_text += " cannot be blank! Please fill all information.";
            return false;
        } else return true;


    }


}
