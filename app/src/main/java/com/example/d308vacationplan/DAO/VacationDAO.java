package com.example.d308vacationplan.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationplan.Entities.Vacation;

import java.util.List;

@Dao
public interface VacationDAO {

    @Insert
    void insert(Vacation vacation);

    @Update
    void update(Vacation vacation);

    @Delete
    void delete(Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY vacationid")
    List<Vacation> getAllVacation();

    @Query("SELECT * FROM vacations WHERE vacationId = :vacationId LIMIT 1")
    Vacation getVacationFromId(int vacationId);
}
