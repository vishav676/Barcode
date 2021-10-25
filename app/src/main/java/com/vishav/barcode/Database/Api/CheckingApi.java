package com.vishav.barcode.Database.Api;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.Database.Repo.CheckingTableRepo;
import com.vishav.barcode.Database.Repo.ScanningTableRepo;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckingApi {
    private DataService dataService;
    private MutableLiveData<List<CheckingTable>> allCheckings = new MutableLiveData<>();
    private CheckingTableRepo repo;
    public CheckingApi(Application application){
        repo = new CheckingTableRepo(application);
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public LiveData<List<CheckingTable>> getAllCheckings()
    {
        Call<List<CheckingTable>> call = dataService.getCheckings();
        call.enqueue(new Callback<List<CheckingTable>>() {
            @Override
            public void onResponse(Call<List<CheckingTable>> call, Response<List<CheckingTable>> response) {
                allCheckings.setValue(response.body());
                List<CheckingTable> list = response.body();
                repo.insert(list);
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<CheckingTable>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
        return allCheckings;
    }

    public long insert(CheckingTable checkingTable)
    {
        Call<CheckingTable> call =dataService.createChecking(checkingTable);
        call.enqueue(new Callback<CheckingTable>() {
            @Override
            public void onResponse(Call<CheckingTable> call, Response<CheckingTable> response) {
                Log.d("ResponseBody", "onRes: " + response.body());
            }

            @Override
            public void onFailure(Call<CheckingTable> call, Throwable t) {
                Log.d("ResponseBody", "onRes: " + t.getMessage());
            }
        });

        while(true)
        {
            if(call.isExecuted())
            {
                break;
            }
        }
        return repo.insert(checkingTable);
    }
}
