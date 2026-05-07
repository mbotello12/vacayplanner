package com.example.d308vacationplan.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308vacationplan.Adapter.ExcursionAdapter;
import com.example.d308vacationplan.Adapter.VacationAdapter;
import com.example.d308vacationplan.DAO.ExcursionDAO;
import com.example.d308vacationplan.Entities.Vacation;
import com.example.d308vacationplan.R;
import com.example.d308vacationplan.Repository.VacationRepository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetailsActivity extends AppCompatActivity {

    ExcursionDAO excursionDAO;
    private VacationRepository vacationRepository;
    static int notificationId;
    private int vacationId;
    private EditText vacationTitle;
    private EditText hotelName;
    private EditText startDate;
    private EditText endDate;
    private String vacayStart;
    private String vacayEnd;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);

        vacationRepository = new VacationRepository(getApplication());

        vacationTitle = findViewById(R.id.titleText);
        hotelName = findViewById((R.id.hotelText));
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);



        vacationId = getIntent().getIntExtra("vacationId", -1);

        vacationTitle.setText(getIntent().getStringExtra("title"));
        hotelName.setText(getIntent().getStringExtra("hotel"));
        startDate.setText(getIntent().getStringExtra("startDate"));
        endDate.setText(getIntent().getStringExtra("endDate"));

        vacayStart = startDate.getText().toString().trim();
        vacayEnd = endDate.getText().toString().trim();

        Button saveButton = findViewById(R.id.saveVacationButton);
        saveButton.setOnClickListener(view -> saveVacation());

        TextView deleteButton = findViewById(R.id.deleteVacation);
        deleteButton.setOnClickListener(view -> deleteVacation());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
;

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(v -> {
                if (vacationId == -1){
                    Toast.makeText(this, "Please save vacation first.", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent exIntent = new Intent(this, ExcursionDetailActivity.class);
                exIntent.putExtra("vacationId", vacationId);
                exIntent.putExtra("startDate", vacayStart);
                exIntent.putExtra("endDate", vacayEnd);
                startActivity(exIntent);
        });
//        3g. Display a list of excursions associated with each vacation
        RecyclerView recyclerView = findViewById(R.id.excursionrycylerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        excursionAdapter.setExcursions(vacationRepository.getExcursionsFromVacation(vacationId));
    }
    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrycylerview);
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        excursionAdapter.setExcursions(vacationRepository.getExcursionsFromVacation(vacationId));
    }


    private void vacationAlert(AlarmManager alarmManager, long trigger, String message, int notificationId){

        Intent intent = new Intent(VacationDetailsActivity.this, MyVacationReceiver.class);
        intent.putExtra("key", message);
        intent.putExtra("notification_id", notificationId);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetailsActivity.this, notificationId, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Vacation: " + getIntent().getStringExtra("title"));
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hotel/Lodging: " + getIntent().getStringExtra("hotel") + "\n" +"Start Date: " + getIntent().getStringExtra("startDate" ) + "\n" + "End Date: " + getIntent().getStringExtra("endDate"));
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent,null);
            startActivity(shareIntent);
            return true;

        }
        if (item.getItemId() == R.id.startAlert){
            String startDateFromScreen = startDate.getText().toString();

            Date myStartDate = null;
            try {
                myStartDate = dateFormat.parse(startDateFromScreen);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                long startDateTrigger = myStartDate.getTime();

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                vacationAlert(alarmManager, startDateTrigger, "Vacation starting: " + getIntent().getStringExtra("title"), notificationId++);

            } catch (Exception e){
                    e.printStackTrace();
            }
        }
        if (item.getItemId() == R.id.endAlert){
            String endDateFromScreen = endDate.getText().toString();

            Date myEndDate = null;
            try {
                myEndDate = dateFormat.parse(endDateFromScreen);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                long endDateTrigger = myEndDate.getTime();

                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);


                vacationAlert(alarmManager, endDateTrigger, "Vacation Ending: " + getIntent().getStringExtra("title"), notificationId++);

            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }

    private void saveVacation(){

        String title = vacationTitle.getText().toString();
        String hotel = hotelName.getText().toString();
        String startVacation = startDate.getText().toString().trim();
        String endVacation = endDate.getText().toString().trim();

        if (title.isEmpty() || hotel.isEmpty() || startVacation.isEmpty() || endVacation.isEmpty()){
            Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!startVacation.matches("\\d{2}/\\d{2}/\\d{4}") || !endVacation.matches("\\d{2}/\\d{2}/\\d{4}")){
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Date start = dateFormat.parse(startVacation);
            Date end = dateFormat.parse(endVacation);

            if (start == null || end == null) {
                Toast.makeText(this, "Invalid Entry", Toast.LENGTH_LONG).show();
            }
            if (end.before(start)) {
                Toast.makeText(this, "Start Date must be before End Date.", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format", Toast.LENGTH_LONG).show();
            return;
        }


        Vacation vacation = new Vacation(title, hotel, startVacation, endVacation);
        if (vacationId == -1){
            vacationRepository.insert(vacation);
            Toast.makeText(this, "Vacation saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            vacation.setVacationid(vacationId);
            vacationRepository.update(vacation);
            Toast.makeText(this, "Vacation Updated!", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
    private void deleteVacation() {
        if (vacationId == -1){
            Toast.makeText(this, "No Vacation Saved.", Toast.LENGTH_LONG).show();
            return;
        }

        String title = vacationTitle.getText().toString();
        String hotel = hotelName.getText().toString();
        String startVacation = startDate.getText().toString();
        String endVacation = endDate.getText().toString();

        Vacation vacation = new Vacation(title, hotel, startVacation, endVacation);
        vacation.setVacationid(vacationId);
        if (vacationRepository.delete(vacation)) {
            Toast.makeText(this, "Vacation Deleted!", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Vacation cannot be deleted with excursions!", Toast.LENGTH_SHORT).show();
        }


    }

}