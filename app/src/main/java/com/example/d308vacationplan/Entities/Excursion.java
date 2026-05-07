package com.example.d308vacationplan.Entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


//  4. Included the Excursion Details, Title and Date
@Entity(tableName = "excursions",
        foreignKeys = @ForeignKey(
                entity = Vacation.class,
                parentColumns = "vacationid",
                childColumns = "vacationId",
                onDelete = ForeignKey.RESTRICT
        ))
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionId;
    private String title;
    private String date;
    private int vacationId;

    public Excursion(String title, String date) {
        this.title = title;
        this.date = date;
    }

    public int getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(int excursionId) {
        this.excursionId = excursionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationId() {
        return vacationId;
    }

    public void setVacationId(int vacationId) {
        this.vacationId = vacationId;
    }
}
