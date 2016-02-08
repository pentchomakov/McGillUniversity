package ca.mcgill.ecse321.eventregistration;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

import ca.mcgill.ecse321.eventregistration.controller.EventRegistrationController;
import ca.mcgill.ecse321.eventregistration.controller.InvalidInputException;
import ca.mcgill.ecse321.eventregistration.model.Event;
import ca.mcgill.ecse321.eventregistration.model.Participant;
import ca.mcgill.ecse321.eventregistration.model.RegistrationManager;
import ca.mcgill.ecse321.eventregistration.persistence.PersistenceEventRegistration;

public class MainActivity extends AppCompatActivity {

    // declaration of variables
    RegistrationManager rm;
    HashMap participants;
    HashMap events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        PersistenceEventRegistration.setFileName(getFilesDir().getAbsolutePath() + "eventregistration.xml");
        PersistenceEventRegistration.loadEventRegistrationModel();
        rm = RegistrationManager.getInstance();
        refreshData();
    }

    private void refreshData() {
        TextView tv = (TextView) findViewById(R.id.newparticipant_name);
        tv.setText("");
        // Initialize the data in the participant spinner
        Spinner participantSpinner = (Spinner) findViewById(R.id.participantspinner);
        ArrayAdapter<CharSequence> participantAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        participantAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Hashmap to store participant spinner items
        this.participants = new HashMap<Integer, Participant>();
        int i = 0;
        for (Iterator<Participant> participants = rm.getParticipants().iterator(); participants.hasNext(); i++) {
            Participant p = participants.next();
            participantAdapter.add(p.getName());
            this.participants.put(i, p);
        }
        participantSpinner.setAdapter(participantAdapter);

        // Initialize the data in the event spinner
        Spinner eventSpinner = (Spinner) findViewById(R.id.eventspinner);
        ArrayAdapter<CharSequence> eventAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item);
        eventAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Hashmap to store event spinner items
        this.events = new HashMap<Integer, Event>();
        int j = 0;
        for (Iterator<Event> events = rm.getEvents().iterator(); events.hasNext(); j++) {
            Event p = events.next();
            eventAdapter.add(p.getName());
            this.events.put(j, p);
        }
        eventSpinner.setAdapter(eventAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // method to add a participant
    public void addParticipant (View v){
        TextView tv = (TextView) findViewById(R.id.newparticipant_name);
        TextView error = (TextView) findViewById(R.id.errorText);
        EventRegistrationController pc = new EventRegistrationController();
        try{
            pc.createParticipant(tv.getText().toString());

        // Throws error message if you try to add a participant without specifying a name
        } catch (InvalidInputException e) {
            error.setText(e.toString());
        }
        refreshData();
    }

    public void addEvent (View v){
        TextView tv = (TextView) findViewById(R.id.newevent_name);
        TextView tv2 = (TextView) findViewById(R.id.newevent_date);
        TextView tv3 = (TextView) findViewById(R.id.newevent_starttime);
        TextView tv4 = (TextView) findViewById(R.id.newevent_endtime);
        TextView error = (TextView) findViewById(R.id.errorText);

        // make sure both time and date formats are recognized
        DateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        DateFormat format2 = new SimpleDateFormat("hh:mm", Locale.ENGLISH);
        EventRegistrationController pc = new EventRegistrationController();
        try{
            // the following 3 lines are to make sure that the time and date formats (seen above) are
            // properly parsed
            java.sql.Date date = new java.sql.Date(format.parse(tv2.getText().toString()).getTime());
            java.sql.Time starttime = new java.sql.Time(format2.parse(tv3.getText().toString()).getTime());
            java.sql.Time endtime = new java.sql.Time(format2.parse(tv4.getText().toString()).getTime());
            pc.createEvent(tv.getText().toString(), date, starttime, endtime);

        // Throws error message if you try to add an event without specifying a name
        } catch (InvalidInputException e) {
            error.setText(e.toString());
        // Throws error message if time and date are not properly parsed
        } catch (ParseException e) {
            e.printStackTrace();
        }
        refreshData();
    }

    public void register (View v) throws InvalidInputException {
        Spinner participant = (Spinner) findViewById(R.id.participantspinner);
        Spinner event = (Spinner) findViewById(R.id.eventspinner);
        TextView error = (TextView) findViewById(R.id.errorText);
        EventRegistrationController pc = new EventRegistrationController();
        // get the proper index from participant spinner
        int participantindex = participant.getSelectedItemPosition();
        // fetch the correct data from the participant Hashmap with the participant index
        Participant part = (Participant) participants.get(participantindex);
        // get the proper index from event spinner
        int eventindex = event.getSelectedItemPosition();
        // fetch the correct data from the event Hashmap with the event index
        Event even = (Event) events.get(eventindex);

        try{
            pc.register(part, even);

        // Throws error message if you try to register without specifying a participant
        // and event from the spinners
        } catch (InvalidInputException e) {
            error.setText(e.toString());
        }
        refreshData();
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText());
        args.putInt("id", v.getId());
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText());
        args.putInt("id", v.getId());
        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private Bundle getTimeFromLabel(CharSequence text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;
        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }
        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);
        return rtn;
    }
    private Bundle getDateFromLabel(CharSequence text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;
        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }
        rtn.putInt("day", day);
        rtn.putInt("month", month - 1);
        rtn.putInt("year", year);
        return rtn;
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }
    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }
}
