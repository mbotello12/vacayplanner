package com.example.d308vacationplan.Repository;

import android.app.Application;
import android.widget.Toast;

import com.example.d308vacationplan.DAO.ExcursionDAO;
import com.example.d308vacationplan.DAO.VacationDAO;
import com.example.d308vacationplan.Database.VacationDatabase;
import com.example.d308vacationplan.Entities.Excursion;
import com.example.d308vacationplan.Entities.Vacation;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VacationRepository {
    private VacationDAO mVacationDAO;
    private ExcursionDAO mExcursionDAO;


    private List<Vacation> mAllVacation;
    private List<Excursion> mAllExcursions;


    private static int NUMBER_OF_THREADS=4;

    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public VacationRepository(Application application) {
        VacationDatabase db = VacationDatabase.getDatabase(application);
        mVacationDAO = db.vacationDAO();
        mExcursionDAO = db.excursionDAO();
    }



    public List<Vacation> getmAllVacation() {
        databaseExecutor.execute(() -> {
            mAllVacation=mVacationDAO.getAllVacation();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return  mAllVacation;
    }

    public List<Excursion> getmAllExcursions() {
        databaseExecutor.execute(() -> {
            mAllExcursions=mExcursionDAO.getAllExcursion();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return  mAllExcursions;
    }

    public List<Excursion> getExcursionsFromVacation(int vacationID) {
        databaseExecutor.execute(() -> {
            mAllExcursions=mExcursionDAO.getExcursionsFromVacation(vacationID);
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return  mAllExcursions;
    }
    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //        3h. Add, update, and delete as many excursions as needed.
    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //        3h. Add, update, and delete as many excursions as needed.
    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public boolean canDelete(int vacationid){
        final int[] count = {0};
        databaseExecutor.execute(() -> {
            count[0] = mExcursionDAO.countExcursionsFromVacation(vacationid);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return count[0] == 0;
    }
    public boolean delete(Vacation vacation) {
        if (!canDelete(vacation.getVacationid())) {
            return false;
        }
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });
        return true;
    }
    //        3h. Add, update, and delete as many excursions as needed.
    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Vacation getVacationFromId(int vacationId){
        final Vacation[] res = new Vacation[1];
        databaseExecutor.execute(() -> {
            res[0] = mVacationDAO.getVacationFromId(vacationId);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return res[0];
    }
}
