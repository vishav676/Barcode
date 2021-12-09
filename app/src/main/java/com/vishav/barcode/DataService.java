package com.vishav.barcode;

import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataService {

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @GET("ticketLists/")
    Call<List<TicketListTable>> getTicketLists();

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @POST("ticketLists/")
    Call<TicketListTable> createTicketList(@Body TicketListTable ticketListTable);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @POST("ticket/")
    Call<TicketTable> createTicket(@Body TicketTable ticketTable);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @GET("ticket/")
    Call<List<TicketTable>> getTickets();

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @PUT("ticket/{id}")
    Call<TicketTable> updateTicket(@Path("id") long id, @Body TicketTable ticketTable);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @POST("scanning/")
    Call<ScanningTable> createScanning(@Body ScanningTable scanningTable);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @GET("scanning/")
    Call<List<ScanningTable>> getScannings();

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @PUT("scanning/{id}")
    Call<ScanningTable> updateScanning(@Path("id") long id, @Body ScanningTable scanningTable);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @POST("checking/")
    Call<CheckingTable> createChecking(@Body CheckingTable checkingTable);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @GET("checking/")
    Call<List<CheckingTable>> getCheckings();

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @PUT("checking/{id}")
    Call<CheckingTable> updateChecking(@Path("id") long id, @Body CheckingTable checkingTable);


    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @POST("checkingTicket/")
    Call<CheckingTicketListTableRelationship> createCheckingTicketList(
            @Body CheckingTicketListTableRelationship checkingTicketListTableRelationship);

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @GET("checkingTicket/")
    Call<List<CheckingTicketListTableRelationship>> getCheckingTicketList();

    @Headers({"Authorization: Token " + BuildConfig.API_KEY})
    @PUT("checkingTicket/{id}")
    Call<CheckingTicketListTableRelationship> updatesCheckingTicketList(@Path("id") long id, @Body
    CheckingTicketListTableRelationship checking);


}
