package com.vishav.barcode.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;


import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Database.EventRepo;
import com.vishav.barcode.Database.HistoryRepo;
import com.vishav.barcode.Database.TicketRepo;
import com.vishav.barcode.Models.Event;
import com.vishav.barcode.Models.History;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.R;
import com.vishav.barcode.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {


    private static final String ErrorTAG = "Error";
    boolean isDetected = false;
    TextView ticketNum,ticketType;
    Switch flash;
    ImageAnalysis imageAnalysis;
    CardView cardView;
    TextView tvName, tvType, tvNo,tv_lastCheck, errorNum, issue, errorDetail;
    PreviewView cameraPreview;
    Camera camera;
    Calendar calendar;
    CardView error_cardView;
    ProcessCameraProvider cameraProvider;
    BarcodeScannerOptions options;
    BarcodeScanner detector;
    DatabaseHelper db;
    private TicketRepo ticketRepo;
    private EventRepo eventRepo;
    private HistoryRepo historyRepo;

    private int usedTicketsNumber = 0;
    private FragmentHomeBinding root;
    Event event;
    List<Ticket> ticketList = new ArrayList<>();
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("NewApi")
    //HashMap<String, String> result = new HashMap<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        db = new DatabaseHelper(getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        root = FragmentHomeBinding.inflate(inflater, container, false);
        cameraPreview = root.CameraViewid;
        cardView = root.getRoot().findViewById(R.id.barcode_result);
        error_cardView = root.getRoot().findViewById(R.id.barcode_error);
        tvName = cardView.findViewById(R.id.tvname);
        tvNo = cardView.findViewById(R.id.tvType);
        ticketNum = root.ticketNumber;
        ticketType = root.ticketType;
        tvType = cardView.findViewById(R.id.number);
        flash = root.toggleFlash;
        errorNum = root.getRoot().findViewById(R.id.errorNum);
        issue = root.getRoot().findViewById(R.id.issueTv);

        ticketRepo = new TicketRepo(getActivity());
        eventRepo = new EventRepo(getActivity());
        historyRepo = new HistoryRepo(getActivity());

        Bundle bundle = getArguments();
        if(bundle != null) {
            ticketList = (List<Ticket>) bundle.getSerializable("ticketList");
            event = (Event) bundle.getSerializable("event");
            if (event != null)
                ticketList = eventRepo.getEventTickets(event.getID());
        }
        usedTicketsNumber = (int) ticketList.stream().filter(x -> x.getUseable() == 0).count();
        tv_lastCheck = root.lastCheck;
        errorDetail = root.getRoot().findViewById(R.id.tvErrorDetail);
        calendar = Calendar.getInstance();
        root.checkedInCount.setText(usedTicketsNumber + "/" + ticketList.size());
        Dexter.withActivity(getActivity()).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @RequiresApi(api = Build.VERSION_CODES.P)
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        setupCamera();
                        boolean hasFlash = getContext().getPackageManager().
                                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                        flash.setOnCheckedChangeListener((compoundButton, b) -> {
                            if(hasFlash)
                                camera.getCameraControl().enableTorch(b);
                            else
                                Toast.makeText(getContext(),"Flash Not Available",
                                        Toast.LENGTH_SHORT).show();
                        });
                    }
                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }
                }).check();

        return root.getRoot();

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupCamera()
    {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(()->{
            try {
                cameraProvider = cameraProviderFuture.get();
                options = new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                        .build();
                detector = BarcodeScanning.getClient(options);
                bindPreview(cameraProvider);


            } catch (ExecutionException | InterruptedException e){
                Log.e(ErrorTAG, "setupCamera: ", e);
            }
        }, ContextCompat.getMainExecutor(getContext()));
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    void bindPreview(ProcessCameraProvider cameraProvider)
    {
        Preview preview = new Preview.Builder()
                .build();
        CameraSelector cameraSelector  = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        imageAnalysis = new ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        configImageAnalysis();

        preview.setSurfaceProvider(cameraPreview.createSurfaceProvider());

        camera = cameraProvider.bindToLifecycle((LifecycleOwner)this
                ,cameraSelector,preview,imageAnalysis);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void configImageAnalysis(){
        if(getActivity() != null) {
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getActivity()),
                    imageProxy -> {
                        int rotationDegrees = imageProxy.getImageInfo().getRotationDegrees();
                        @SuppressLint("UnsafeExperimentalUsageError") Image mediaImage = imageProxy.getImage();
                        if (mediaImage != null) {
                            InputImage image = InputImage.fromMediaImage(mediaImage,rotationDegrees);
                            BarcodeScannerOptions barcodeScannerOptions =
                                    new BarcodeScannerOptions.Builder()
                                            .setBarcodeFormats(
                                                    Barcode.FORMAT_CODE_39
                                            ).build();
                            BarcodeScanner scanner = BarcodeScanning.getClient(barcodeScannerOptions);

                            Task<List<Barcode>> result = scanner.process(image)
                                    .addOnSuccessListener(this::processResult)
                                    .addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_SHORT).show())
                                    .addOnCompleteListener(task -> {
                                        mediaImage.close();
                                        imageProxy.close();
                                    });

                        }
                    });
        }

    }



    private String trackHistory()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm:ss a",
                Locale.ENGLISH);
        return currentDate.format(date);

    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    private void processResult(List<Barcode> barcodes) {
        if (barcodes.size()>0)
        {
            isDetected = true;
            for (Barcode barcode : barcodes)
            {
                int value_type = barcode.getValueType();
                if (value_type == Barcode.TYPE_TEXT) {
                    imageAnalysis.clearAnalyzer();
                    if(ticketList.size() > 0){
                        validateTicket(barcode);
                    }
                    delay();

                }
            }
            isDetected = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void validateTicket(Barcode barcode){
        Ticket ticket = ticketList.stream().filter(x -> x.getTicketNumber().equals(barcode.getRawValue())).findAny().orElse(null);
        if(ticket != null && ticket.getUseable() > 0){
            validTicket(ticket);
        }
        else if(ticket == null){
            inValidMessageTicketNotFound(barcode);
        }
        else if(ticket.getUseable() <= 0)
        {
            inValidMessageAllTriesUsed(barcode);
        }
        else if(historyRepo.getOneHistory(ticket) != null){
            inValidMessageAlreadyUsed(barcode);
        }
    }

    public void inValidMessageTicketNotFound(Barcode barcode){
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Ticket Number not in the list");
        errorNum.setText(barcode.getRawValue());
        delay(error_cardView);
    }

    public void inValidMessageAlreadyUsed(Barcode barcode){
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Already used");
        errorNum.setText(barcode.getRawValue());
        delay(error_cardView);
    }

    public void inValidMessageAllTriesUsed(Barcode barcode)
    {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("All Tries Used");
        errorNum.setText(barcode.getRawValue());
        delay(error_cardView);
    }

    private void validTicket(Ticket barcode){
        String time = trackHistory();
        history(barcode);
        ticketRepo.updateTicketUseable(barcode);
        cardView.setVisibility(View.VISIBLE);
        delay(cardView);
        ticketNum.setText(barcode.getTicketNumber() + "( " + barcode.getUseable() + " )");
        tvName.setText(barcode.getTicketNumber());
        tv_lastCheck.setText(time);
        if(event != null) ticketType.setText(event.getName());
        else
            ticketType.setText(getEventName(barcode.getTicketNumber()));
        int ticketIndex = ticketList.indexOf(barcode);
        barcode.setUseable(barcode.getUseable() - 1);
        ticketList.set(ticketIndex,
                barcode);
        usedTicketsNumber++;
        root.checkedInCount.setText(usedTicketsNumber + "/"+ ticketList.size());
    }

    private void delay(){
        final Handler handler = new Handler();
        handler.postDelayed(this::configImageAnalysis,3000);
    }

    private String getEventName(String ticketNo){
        return eventRepo.getEventInfo(ticketNo);
    }

    private void delay(CardView cardView){
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        cardView.setVisibility(View.INVISIBLE);
                    });
                }
            }
        },3000);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void history(Ticket barcode)
    {
        Ticket ticket = ticketRepo.getTicket(barcode.getTicketNumber());
        History history = new History("Successful", trackHistory(),"No","Nem", false, event.getID(),ticket.getTicketNumber());
        historyRepo.insertHistory(history);
    }

}