package eh.com.timhealthcaretest.ui.appointment.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import eh.com.timhealthcaretest.R;
import eh.com.timhealthcaretest.model.Patient;
import eh.com.timhealthcaretest.ui.appointment.RefreshEvent;
import eh.com.timhealthcaretest.ui.appointment.Resource;
import eh.com.timhealthcaretest.ui.appointment.view.adapter.PatientRecyclerAdapter;
import eh.com.timhealthcaretest.ui.appointment.view.fragments.PatientAptFormDialogFragment;
import eh.com.timhealthcaretest.ui.appointment.viewmodel.PatientViewModel;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rcy_patient)
    RecyclerView rcyPatient;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    PatientViewModel viewModel;
    PatientRecyclerAdapter adapter;
    List< Patient > patientList;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        ButterKnife.bind ( this );
        toolbar.setTitleTextColor ( Color.WHITE );
        setSupportActionBar ( toolbar );
        declare ();
        initRecyclerView ();
        viewModel = ViewModelProviders.of ( this ).get ( PatientViewModel.class );
        catchEvents ();


    }

    private void declare ( ) {
        adapter = new PatientRecyclerAdapter ();
        patientList = new ArrayList<> ();
        adapter.setPatients ( patientList );
    }

    private void initRecyclerView ( ) {

        LinearLayoutManager layoutManager = new LinearLayoutManager ( this );
        layoutManager.setOrientation ( RecyclerView.VERTICAL );
        rcyPatient.setLayoutManager ( layoutManager );
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration ( rcyPatient.getContext (),
                layoutManager.getOrientation () );
        rcyPatient.addItemDecoration ( dividerItemDecoration );
        rcyPatient.setAdapter ( adapter );

    }

    @Override
    protected void onStart ( ) {
        super.onStart ();
        EventBus.getDefault ().register ( this );
    }


    private void catchEvents ( ) {
        fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick (View view) {

                showPatientAppointmentForm ();
            }
        } );

        observeApptRecords ();

    }

    private void observeApptRecords ( ) {
        viewModel.loadPatients ().removeObservers ( this );
        viewModel.loadPatients ().observe ( this, new Observer< Resource< List< Patient > > > () {
            @Override
            public void onChanged (Resource< List< Patient > > listResource) {
                if (listResource != null) {
                    switch (listResource.status) {
                        case SUCCESS: {
                            adapter.setPatients ( listResource.data );
                        }
                        break;
                        case ERROR: {

                            AlertDialog.Builder builder = new AlertDialog.Builder ( MainActivity.this );
                            builder.setMessage ( "Sorry,we are facing temporary problem. We will get back you soon." )
                                    .setTitle ( "Error" );
                            builder.setPositiveButton ( "OK", new DialogInterface.OnClickListener () {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    dialog.dismiss ();

                                }
                            } );
                            AlertDialog alertDialog = builder.create ();
                            alertDialog.show ();
                        }
                        break;

                    }
                }
            }
        } );
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent (RefreshEvent event) {
        observeApptRecords ();
    }


    @Override
    protected void onStop ( ) {
        super.onStop ();
        EventBus.getDefault ().unregister ( this );
    }

    private void showPatientAppointmentForm ( ) {
        FragmentManager fragmentManager = getSupportFragmentManager ();
        PatientAptFormDialogFragment patientAptFormDialogFragment = new PatientAptFormDialogFragment ();
        patientAptFormDialogFragment.show ( fragmentManager, "patient_appt_form" );
    }


}
