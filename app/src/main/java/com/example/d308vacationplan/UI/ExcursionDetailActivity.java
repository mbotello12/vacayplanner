package com.example.d308vacationplan.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.d308vacationplan.Entities.Excursion;
import com.example.d308vacationplan.Entities.Vacation;
import com.example.d308vacationplan.R;
import com.example.d308vacationplan.Repository.VacationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetailActivity extends AppCompatActivity {


    private VacationRepository excursionRepository;
    static int notificationId;
    private int parentVacationId;
    private int excursionId;
    private EditText editTitle;
    private EditText editDate;
    String excursionTitle;
    String excursionDate;
    String vacationStart;
    String vacationEnd;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        excursionRepository = new VacationRepository(getApplication());
        //  5a. Display a detail view of the excursion
        excursionId = getIntent().getIntExtra("excursionId", -1);
        editTitle = findViewById(R.id.excursionText);
        editDate = findViewById(R.id.ExcursionDate);
        parentVacationId = getIntent().getIntExtra("vacationId", -1);

        editTitle.setText(getIntent().getStringExtra("title"));
        editDate.setText(getIntent().getStringExtra("date"));

//        Null check for vacation start, vacation end and excursion Id
//        Toast.makeText(this, vacationStart + vacationEnd + excursionId, Toast.LENGTH_LONG).show();

        //        5b. Enter, edit and delete excursion information
        Button saveButton = findViewById(R.id.saveExcursionButton);
        saveButton.setOnClickListener(view -> saveExcursion());

        TextView deleteButton = findViewById(R.id.deleteExcursion);
        deleteButton.setOnClickListener(view -> deleteExcursion());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_excursion_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.exAlert){
            String excursionDate = editDate.getText().toString().trim();
            Date alertDate = null;

            try {
                alertDate = dateFormat.parse(excursionDate);
            }catch (ParseException e){
                e.printStackTrace();
            }
            try {
                long excursionTrigger = alertDate.getTime();
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(ExcursionDetailActivity.this, MyExcursionReceiver.class);
                intent.putExtra("key", "Excursion today: " + getIntent().getStringExtra("title"));
                intent.putExtra("notification_id", notificationId);
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetailActivity.this, notificationId++, intent, PendingIntent.FLAG_IMMUTABLE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, excursionTrigger, sender);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return true;
    }


    //        3h. Add, update, and delete as many excursions as needed.
    //        5b. Enter, edit and delete excursion information
    private void saveExcursion(){
        dateFormat.setLenient(false);
        excursionTitle = editTitle.getText().toString();
        excursionDate = editDate.getText().toString();
        Vacation parentVacation = excursionRepository.getVacationFromId(parentVacationId);
        
        if (parentVacation == null){
            Toast.makeText(this, "Vacation Not Found", Toast.LENGTH_SHORT).show();
            return;
        }

        String parentStartDate = parentVacation.getStartDate();
        String parentEndDate = parentVacation.getEndDate();

        if (excursionTitle.isEmpty() || excursionDate.isEmpty()){
            Toast.makeText(this, "Please complete all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        //        5c. Include Validation that the input dates are formatted correctly
        if (!excursionDate.matches("\\d{2}/\\d{2}/\\d{4}")){
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            Date excursionStart = dateFormat.parse(excursionDate);
            Date vacayStart = dateFormat.parse(parentStartDate);
            Date vacayEnd = dateFormat.parse(parentEndDate);
            if (excursionStart == null || vacayStart == null || vacayEnd == null) {
                Toast.makeText(this, "Invalid Entry", Toast.LENGTH_LONG).show();
                return;
            }
            if (excursionStart.before(vacayStart) || excursionStart.after(vacayEnd)){
                Toast.makeText(this, "Excursion must be in Vacation Date Range.", Toast.LENGTH_LONG).show();
                return;
            }

        } catch (ParseException e) {
            Toast.makeText(this, "Dates must be in MM/dd/yyyy format", Toast.LENGTH_LONG).show();
            return;
        }


        Excursion excursion = new Excursion(excursionTitle, excursionDate);
        if (excursionId == -1) {
            excursion.setVacationId(parentVacationId);
            excursionRepository.insert(excursion);
            Toast.makeText(this, "Excursion Saved!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            excursion.setVacationId(parentVacationId);

            excursion.setExcursionId(excursionId);
            excursionRepository.update(excursion);
            Toast.makeText(this, "Excursion Updated!", Toast.LENGTH_LONG).show();
            finish();
        }
        }
    //        3h. Add, update, and delete as many excursions as needed.
    private void deleteExcursion() {
        if (excursionId == -1){
            Toast.makeText(this, "No Excursion Saved.", Toast.LENGTH_LONG).show();
            return;
        }

        String title = editTitle.getText().toString();
        String date = editDate.getText().toString();

        Excursion excursion = new Excursion(title, date);
        excursion.setExcursionId(excursionId);
        excursionRepository.delete(excursion);

        Toast.makeText(this, "Excursion Deleted!", Toast.LENGTH_SHORT).show();
        finish();
    }


    }



