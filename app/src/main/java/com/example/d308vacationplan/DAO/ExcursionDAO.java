package com.example.d308vacationplan.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308vacationplan.Entities.Excursion;

import java.util.List;

@Dao
public interface ExcursionDAO {
    @Insert
    void insert(Excursion excursion);

    @Update
    void update(Excursion excursion);

    @Delete
    void delete(Excursion excursion);

    @Query("SELECT * FROM excursions ORDER BY excursionId")
    List<Excursion> getAllExcursion();
    @Query("SELECT * FROM excursions WHERE vacationId = :vacationId")
    List<Excursion> getExcursionsFromVacation(int vacationId);


    //        3g. Display a list of excursions associated with each vacation
    @Query("SELECT COUNT(*) FROM excursions WHERE vacationId = :vacationID")
    int countExcursionsFromVacation(int vacationID);
}
