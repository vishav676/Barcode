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

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Interfaces.OnFragmentInteraction;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.R;
import com.vishav.barcode.databinding.FragmentHomeBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class HomeFragment extends Fragment {

    private static final String TAG = "My Tag";
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
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;
    DatabaseHelper db;
    private FragmentHomeBinding root;
    List<Ticket> ticketList = new ArrayList<>();
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    @SuppressLint("NewApi")
    HashMap<String, String> result = new HashMap<>();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         db = new DatabaseHelper(getActivity());
    }

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

        Bundle bundle = getArguments();
        if(bundle != null) {
            ticketList = (List<Ticket>) bundle.getSerializable("ticketList");
        }
        tv_lastCheck = root.lastCheck;
        errorDetail = root.getRoot().findViewById(R.id.tvErrorDetail);
        calendar = Calendar.getInstance();
        root.checkedInCount.setText(result.size() + "/" + ticketList.size());
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
        OnFragmentInteraction listener = (OnFragmentInteraction)getActivity();
        assert listener != null;
        listener.onFragmentHistory(result);

        return root.getRoot();

    }


    @RequiresApi(api = Build.VERSION_CODES.P)
    private void setupCamera()
    {
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        cameraProviderFuture.addListener(()->{
            try {
                cameraProvider = cameraProviderFuture.get();
                options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_ALL_FORMATS)
                        .build();
                detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
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

    private void configImageAnalysis(){
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(getActivity()), image -> {
            @SuppressLint("UnsafeExperimentalUsageError") Image media = image.getImage();
            if(media!=null) {
                Image.Plane[] plane = media.getPlanes();
                if (plane.length >= 3) {
                    for (Image.Plane plane1 : plane) {
                        plane1.getBuffer().rewind();
                    }
                    int rotation = degreeToFirebaseRotation(image.getImageInfo()
                            .getRotationDegrees());
                    FirebaseVisionImage fromMediaImage = FirebaseVisionImage
                            .fromMediaImage(media, rotation);
                    processImage(fromMediaImage);
                }
            }

            image.close();
        });
    }

    private void processImage(FirebaseVisionImage visionImageFromFrame) {
        if(!isDetected)
        {
            detector.detectInImage(visionImageFromFrame)
                    .addOnSuccessListener(this::processResult)
                    .addOnFailureListener(e -> {
                    });
        }
    }

    private int degreeToFirebaseRotation(int degrees)
    {
        switch (degrees)
        {
            case 0:
                return FirebaseVisionImageMetadata.ROTATION_0;
            case 90:
                return FirebaseVisionImageMetadata.ROTATION_90;
            case 180:
                return FirebaseVisionImageMetadata.ROTATION_180;
            case 270:
                return FirebaseVisionImageMetadata.ROTATION_270;
            default:
                throw new IllegalArgumentException();
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
    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
        if (firebaseVisionBarcodes.size()>0)
        {
            isDetected = true;
            for (FirebaseVisionBarcode barcode : firebaseVisionBarcodes)
            {
                int value_type = barcode.getValueType();
                if (value_type == FirebaseVisionBarcode.TYPE_TEXT) {
                    imageAnalysis.clearAnalyzer();
                    if(ticketList.size() > 0){
                        validateTicket(barcode);
                    }
                    else if(db.searchTicket(barcode.getRawValue())
                            && !result.containsKey(barcode.getRawValue())){
                        validTicket(barcode);
                    }
                    else
                    {
                        if (!db.searchTicket(barcode.getRawValue()))
                        {
                            inValidMessageTicketNotFound(barcode);
                        }
                        else if(result.containsKey(barcode.getRawValue())){
                            inValidMessageAlreadyUsed(barcode);
                        }

                    }
                    delay();

                }
            }
            isDetected = false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void validateTicket(FirebaseVisionBarcode barcode){
        Ticket ticket = ticketList.stream().filter(x -> x.getTicketNumber().equals(barcode.getRawValue())).findAny().orElse(null);
        if(ticket != null && !result.containsKey(barcode.getRawValue())){
            validTicket(barcode);
        }
        else if(ticket == null){
            inValidMessageTicketNotFound(barcode);
        }
        else if(result.containsKey(barcode.getRawValue())){
            inValidMessageAlreadyUsed(barcode);
        }
    }

    public void inValidMessageTicketNotFound(FirebaseVisionBarcode barcode){
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Ticket Number not in the list");
        errorNum.setText(barcode.getRawValue());
        delay(error_cardView);
    }

    public void inValidMessageAlreadyUsed(FirebaseVisionBarcode barcode){
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Already used");
        errorDetail.setText(result.get(barcode.getRawValue()));
        errorNum.setText(barcode.getRawValue());
        delay(error_cardView);
    }

    private void validTicket(FirebaseVisionBarcode barcode){
        String time = trackHistory();
        result.put(barcode.getRawValue(), time);
        cardView.setVisibility(View.VISIBLE);
        delay(cardView);
        ticketNum.setText(barcode.getRawValue());
        tvName.setText(barcode.getRawValue());
        tv_lastCheck.setText(time);
        ticketType.setText(db.getEventInfo(barcode.getRawValue()));
        root.checkedInCount.setText(result.size() + "/"+ ticketList.size());
    }

    private void delay(){
        final Handler handler = new Handler();
        handler.postDelayed(this::configImageAnalysis,3000);
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
}