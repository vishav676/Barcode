package com.vishav.barcode.Database.Api;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.Database.Repo.ScanningTableRepo;
import com.vishav.barcode.Database.Repo.TicketTableRepo;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanningApi {

    private DataService dataService;
    private MutableLiveData<List<ScanningTable>> allTickets = new MutableLiveData<>();
    private ScanningTableRepo repo;
    public ScanningApi(Application application){
        repo = new ScanningTableRepo(application);
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public void insert(ScanningTable scanningTable)
    {
        Call<ScanningTable> call = dataService.createScanning(scanningTable);
        call.enqueue(new Callback<ScanningTable>() {
            @Override
            public void onResponse(Call<ScanningTable> call, Response<ScanningTable> response) {
                try {
                    repo.insert(scanningTable);
                }catch (Exception e)
                {
                    Log.e("Exception", "error: " + e.getMessage());
                }
                Log.e("InsertionError", "onResponse: " + response.errorBody());
            }

            @Override
            public void onFailure(Call<ScanningTable> call, Throwable t) {

            }
        });
    }

    public void getScanning()
    {
        Call<List<ScanningTable>> call = dataService.getScannings();

        call.enqueue(new Callback<List<ScanningTable>>() {
            @Override
            public void onResponse(Call<List<ScanningTable>> call, Response<List<ScanningTable>> response) {
                if(response.body() != null)
                    repo.insert(response.body());

            }

            @Override
            public void onFailure(Call<List<ScanningTable>> call, Throwable t) {

            }
        });

    }

}
