package com.vishav.barcode.Database.Api;

import android.app.Application;
import android.util.Log;

import androidx.camera.core.impl.MutableTagBundle;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;
import com.vishav.barcode.Database.Repo.CheckingTicketListTableRepo;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckingListApi {
    private DataService service;
    private MutableLiveData<List<CheckingTicketListTableRelationship>> list
            = new MutableLiveData<>();
    private CheckingTicketListTableRepo repo;
    public CheckingListApi(Application application){
        repo = new CheckingTicketListTableRepo(application);
        service = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public LiveData<List<CheckingTicketListTableRelationship>> getAllCheckingsList()
    {
        Call<List<CheckingTicketListTableRelationship>> call = service.getCheckingTicketList();
        call.enqueue(new Callback<List<CheckingTicketListTableRelationship>>() {
            @Override
            public void onResponse(Call<List<CheckingTicketListTableRelationship>> call,
                                   Response<List<CheckingTicketListTableRelationship>> response) {
                list.setValue(response.body());
                List<CheckingTicketListTableRelationship> list = response.body();
                if(list != null && !list.isEmpty())
                    repo.insert(list);
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<CheckingTicketListTableRelationship>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
        return list;
    }

    public void insert(CheckingTicketListTableRelationship checkingTable)
    {
        Call<CheckingTicketListTableRelationship> call = service
                .createCheckingTicketList(checkingTable);
        call.enqueue(new Callback<CheckingTicketListTableRelationship>() {
            @Override
            public void onResponse(Call<CheckingTicketListTableRelationship> call,
                                   Response<CheckingTicketListTableRelationship> response) {
                Log.d("ResponseBody", "onRes: " + response.body());
                repo.insert(checkingTable);
            }

            @Override
            public void onFailure(Call<CheckingTicketListTableRelationship> call, Throwable t) {
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
    }
}
